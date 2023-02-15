package it.irideos.metrics.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.irideos.metrics.configurations.GnocchiConfig;
import it.irideos.metrics.configurations.OcloudAuth;
import it.irideos.metrics.models.ClusterModel;
import it.irideos.metrics.models.ImageModel;
import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.models.ResourceModel;
import it.irideos.metrics.models.UsageHourClusterModel;
import it.irideos.metrics.models.UsageHourModel;
import it.irideos.metrics.models.VMModel;
import it.irideos.metrics.repository.ClusterRepository;
import it.irideos.metrics.repository.ResourceRepository;
import it.irideos.metrics.repository.UsageHourRepository;
import it.irideos.metrics.service.ImageService;
import it.irideos.metrics.service.ResourceService;
import it.irideos.metrics.service.UsageHourClusterService;
import it.irideos.metrics.service.UsageHourService;
import it.irideos.metrics.service.VMService;
import it.irideos.metrics.utils.HttpUtils;
import it.irideos.metrics.utils.MetricsUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class VMController {

    private VMModel[] vmResources;
    private Double costH;
    private Double hourlyRate;
    private Long resourceForHour;
    private Long resourceId;
    private Long clusterId;
    Long totalRes;
    private String vcpu = "";
    private String flavorName = "";
    private String clusterName = "";
    private String displayName = "";
    private Timestamp timestamp;
    private Timestamp dtFrom;
    private Timestamp dtTo;
    Map<String, Long> cluster_id = new HashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OcloudAuth authToken;

    @Autowired
    private GnocchiConfig gnocchiConfig;

    @Autowired
    private VMService VmResourceService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UsageHourService usageHourService;

    @Autowired
    private UsageHourRepository usageHourRepository;

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private UsageHourClusterService usageHourClusterService;

    @PostConstruct
    private void getVMInstances() // call gnocchi api
            throws JsonMappingException, JsonProcessingException, ArrayIndexOutOfBoundsException,
            NotFoundException {

        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl + "/resource/instance";
        HttpHeaders headers = HttpUtils.createHttpHeaders(authToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            // map the response
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            vmResources = objectMapper.readValue(response.getBody(), VMModel[].class);
            log.info(vmResources);
        } catch (Exception e) {
            log.warn("Unexpected Error retrieving or parsing data: ", e);
        }

        // for every resource
        for (VMModel vmResource : vmResources) {
            ResourceModel p = vmResource.getResource();
            // call gnocchi api for retrieve metrics by vcpu and persist data into metrics
            List<MetricsModel> metrics = resourceService.getResourceForVcpu(p.getVcpus());
            if (metrics.equals(null)) {
                throw new RuntimeException("List Metrics - Metrics Not Found");
            }
            vmResource.getResource().setMetrics(metrics);

            // find image ref from table image for every resource
            List<ImageModel> images = imageService.getImageModel(vmResource);
            if (images.equals(null)) {
                throw new RuntimeException("List Images - Images Not Found");
            }

            // find cluster name for every resource by image ref
            Map<ClusterModel, String> cluster = imageService.getClusterMap(images, vmResource);
            if (cluster.equals(null)) {
                throw new RuntimeException("Cluster Map - Cluster Not Found");
            }
            cluster.forEach((k, v) -> {
                if (vmResource.getDisplayName().contains(v)) {
                    clusterName = "";
                    clusterName = v;
                    vmResource.setCluster(k);
                    // persist data into resource and measure table
                    VmResourceService.createVmResource(vmResource);
                }
            });
        }

        // retrieve all exists metrics from metrics table
        List<ResourceModel> list = resourceRepository.findAll();
        if (list.equals(null)) {
            throw new RuntimeException("Vcpus List - Vcpus Not Found");
        }

        // for every metrics...
        for (ResourceModel vcpus : list) {
            vcpu = vcpus.getVcpus();

            // find display name and timestamp for every vcpu
            List<Object[]> displayNameAndTimestamp = resourceRepository
                    .findDisplayNameAndTimestampByVcpus(vcpu);
            if (displayNameAndTimestamp.equals(null)) {
                throw new RuntimeException("Find - Display Name and Timestamp Not Found");
            }

            for (Object[] objNameAndTimestamp : displayNameAndTimestamp) {
                if (displayName != null || displayName != "" && timestamp != null && flavorName != null
                        || flavorName != "" && resourceId != null) {
                    displayName = "";
                    timestamp = null;
                    flavorName = "";
                    resourceId = null;
                    clusterId = null;
                    costH = 0.0;
                }

                displayName = (String) objNameAndTimestamp[0];
                timestamp = (Timestamp) objNameAndTimestamp[1];
                flavorName = (String) objNameAndTimestamp[2];
                resourceId = (Long) objNameAndTimestamp[3];
                clusterId = (Long) objNameAndTimestamp[4];

                // find clustername and cluster id for every resource
                List<Object[]> cluster = clusterRepository.findClusterNameById(clusterId);

                for (Object[] clusterNames : cluster) {
                    clusterName = "";
                    clusterName = (String) clusterNames[1];
                    cluster_id.put(clusterName, clusterId);
                }

                if (displayName.contains(clusterName)) {

                // find sum vcpu for every display name and timestamp grouped by falvor name
                Map<Long, String> sumVmForFalvorId = usageHourService.findVmAndFlavorByDisplayName(displayName,
                        timestamp);
                if (sumVmForFalvorId.equals(null)) {
                    throw new RuntimeException("Find - Sum of Resource For Hour Not Found");
                }

                    // map with flavor name and cost hourly (sum vm * hourly cost)
                    Map<String, Double> costHourForFlavor = new HashMap<>();
                    Map<String, Long> totResourceforHour = new HashMap<>();

                    sumVmForFalvorId.forEach((k, v) -> {
                        if (flavorName != null) {
                            flavorName = "";
                            resourceForHour = null;
                        }
                        flavorName = v;

                        // find hourly price for every flavor name
                        List<Object[]> costForFlavorName = resourceRepository.findPriceByFlavorName(flavorName);
                        if (costForFlavorName.equals(null)) {
                            throw new RuntimeException("Find - Hourly Price Not Found");
                        }
                        for (Object[] price : costForFlavorName) {
                            hourlyRate = 0.0;
                            hourlyRate = (Double) price[0];
                            Double cost = 0.0;
                            cost = k * hourlyRate;
                            costHourForFlavor.put(flavorName, cost);
                            totResourceforHour.put(flavorName, k);
                        }
                    });

                    if (clusterName != null && timestamp != null && resourceId != null) {
                        resourceForHour = null;
                        costH = 0.0;
                        resourceForHour = totResourceforHour.values().stream().mapToLong(Long::longValue).sum();
                        costH = costHourForFlavor.values().stream().mapToDouble(Double::doubleValue).sum();
                        BigDecimal.valueOf(costH).setScale(3, RoundingMode.HALF_UP);

                        // persist data into usage_hour table for every kubernetes resource founds
                        UsageHourModel usageHour = new UsageHourModel(1L, clusterName, costH,
                                resourceForHour, timestamp, resourceId, clusterId);
                        usageHour = usageHourService.createUsageHourly(usageHour);

                        log.info(usageHour);
                    }
                }
            }
        }
        // aggregate from usage hour table every resource for cluster
        cluster_id.forEach((k, v) -> {
            clusterId = null;
            clusterId = v;
            totalRes = null;
            clusterName = "";
            costH = 0.0;
            dtFrom = MetricsUtils.formatterInstantYesterdayToTimestamp();
            dtTo = MetricsUtils.formatterInstantNowToTimestamp();
            List<Object[]> usage = usageHourRepository.findUsageHourCluster(clusterId, dtFrom, dtTo);
            if (usageHourRepository.equals(null)) {
                throw new RuntimeException("Find - Usage Hour for Total Cluster Not Found");
            }

            // persist data into usage_hour_cluster table aggregate by cluster and with sum
            // of cost and vcpu
            for (Object[] usageHourForCluster : usage) {
                totalRes = (Long) usageHourForCluster[0];
                costH = (Double) usageHourForCluster[1];
                timestamp = (Timestamp) usageHourForCluster[2];
                clusterName = (String) usageHourForCluster[3];
                BigDecimal.valueOf(costH).setScale(3, RoundingMode.HALF_UP);
                UsageHourClusterModel usageHourCluster = new UsageHourClusterModel(1L, clusterName, costH, totalRes,
                        timestamp);
                usageHourCluster = usageHourClusterService.createUsageHourCluster(usageHourCluster);

                log.info(usageHourCluster);
            }
        });
    }
}
