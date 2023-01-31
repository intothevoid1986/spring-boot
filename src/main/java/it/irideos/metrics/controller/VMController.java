package it.irideos.metrics.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.models.ResourceModel;
import it.irideos.metrics.models.UsageHourModel;
import it.irideos.metrics.models.VMModel;
import it.irideos.metrics.repository.ResourceRepository;
import it.irideos.metrics.service.ClusterService;
import it.irideos.metrics.service.ImageService;
import it.irideos.metrics.service.ResourceService;
import it.irideos.metrics.service.UsageHourService;
import it.irideos.metrics.service.VMService;
import it.irideos.metrics.utils.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class VMController {

    private VMModel[] vmResources;
    private Long resourceForHour;
    private String clusterName = "";

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
    private ClusterService clusterService;

    @PostConstruct
    private void getVMInstances() throws JsonMappingException, JsonProcessingException {
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl + "/resource/instance";
        HttpHeaders headers = HttpUtils.createHttpHeaders(authToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            vmResources = objectMapper.readValue(response.getBody(), VMModel[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (VMModel vmResource : vmResources) {
            ResourceModel p = vmResource.getResource();
            List<MetricsModel> metrics = resourceService.getResourceForVcpu(p.getVcpus());
            vmResource.getResource().setMetrics(metrics);
        }

        for (VMModel vmResource : vmResources) {
            imageService.getImageRef(vmResource);
            Map<ClusterModel, String> cluster = clusterService.getClusterMap();
            cluster.forEach((k, v) -> {
                if (vmResource.getDisplayName().contains(v)) {
                    clusterName = v;
                    vmResource.setCluster(k);
                }
            });

            VmResourceService.createVmResource(vmResource);
            List<Object[]> displayNameAndTimestamp = resourceRepository
                    .findDisplayNameAndTimestampByVcpus(vmResource.getResource().getVcpus());
            for (Object[] objNameAndTimestamp : displayNameAndTimestamp) {
                String displayName = (String) objNameAndTimestamp[0];
                Timestamp timestamp = (Timestamp) objNameAndTimestamp[1];

                List<Object[]> totResourceForHour = usageHourService.findTotResourceForHour(displayName, timestamp);
                for (Object[] totalRes : totResourceForHour) {
                    resourceForHour = (Long) totalRes[0];
                }

                List<Object[]> sumVmForFalvorId = usageHourService.findVmAndFlavorByDisplayName(displayName,
                        timestamp);
                for (Object[] objCountVmForFlavorId : sumVmForFalvorId) {
                    String flavorName = (String) objCountVmForFlavorId[1];
                    Long resourceId = (Long) objCountVmForFlavorId[2];

                    List<Object[]> costForFlavorName = resourceRepository.findPriceByFlavorName(flavorName);
                    for (Object[] price : costForFlavorName) {
                        Double hourlyRate = (Double) price[0];
                        Double costH = resourceForHour * hourlyRate;

                        UsageHourModel usageHour = new UsageHourModel(1L, clusterName, costH,
                                resourceForHour, timestamp, resourceId);
                        usageHour = usageHourService.createUsageHourly(usageHour);
                        log.info(usageHourService);
                    }
                }
            }
        }
    }
}
