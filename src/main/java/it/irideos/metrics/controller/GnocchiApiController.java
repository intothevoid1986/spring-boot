package it.irideos.metrics.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.irideos.metrics.configurations.GnocchiConfig;
import it.irideos.metrics.configurations.OcloudAuth;
import it.irideos.metrics.models.ClusterModel;
import it.irideos.metrics.models.ImageModel;
import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.models.ResourcesForVcpusModel;
import it.irideos.metrics.models.VmResourcesModel;
import it.irideos.metrics.repository.ClusterRepository;
import it.irideos.metrics.repository.ImageRepository;
import it.irideos.metrics.service.MetricsService;
import it.irideos.metrics.service.ResourceForVcpusService;
import it.irideos.metrics.service.VmResourceService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class GnocchiApiController {

    private List<ImageModel> imageModels;
    private List<ClusterModel> clustModels;
    private VmResourcesModel[] vmResources;
    private List<ResourcesForVcpusModel> vcpResourcesModels;
    private List<String> vcpus = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OcloudAuth auhtToken;

    @Autowired
    private GnocchiConfig gnocchiConfig;

    @Autowired
    private VmResourceService VmResourceService;

    @Autowired
    private ResourceForVcpusService resourceForVcpusService;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ClusterRepository clusterRepository;

    @PostConstruct
    private void getGnocchiInstance() throws JsonMappingException, JsonProcessingException {
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl + "/resource/instance";
        String img = "";
        String img_ref = "";
        String srv = "";
        String clusterName = "";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // Creo istanza
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 
        try {
            // Mappo la risposta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            vmResources = objectMapper.readValue(response.getBody(), VmResourcesModel[].class);
        } catch (Exception e) {
            log.warn("Exception", e.getMessage());
        }

        for (VmResourcesModel vmResource : vmResources) {
            VmResourceService.createVmResource(vmResource);
            MetricsModel p = vmResource.getResource().getMetrics();
            if (vmResource.getImageRef() != null) {
                img_ref = vmResource.getImageRef();
                List<ImageModel> i = imageRepository.findByImageModels(img_ref);
                img = i.toString();
                imageModels = parseImage(img);
                for (ImageModel imageModel : imageModels) {
                    srv = imageModel.getService();
                    System.out.println("SERVICE: " + srv);
                    ArrayList<ClusterModel> clm = new ArrayList<>();
                    clm = clusterRepository.findNameByClusterService(srv);
                    clusterName = clm.toString();
                    // clustModels = objectMapper.readValue(clusterName, new
                    // TypeReference<List<ClusterModel>>() {
                    // });
                }
            }
            ;
            vcpus.add(p.getVcpus());
        }
        vcpus.forEach(vcpu -> {
            getResourceForVcpu(vcpu);
        });

        log.info(resourceForVcpusService);
    }

    private void getResourceForVcpu(String vcpu) {
        String dtFrom = formatterInstantYesterdayToString();
        String dtTo = formatterInstantNowToString();
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl +
                "/metric/{vcpu}/measures?aggregation=count&start=" + dtFrom + "&stop=" + dtTo;
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            MetricsModel metrics = metricsService.listVmByVcpus(vcpu);
            // Mappo la risposta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                    requestEntity, String.class,
                    vcpu);
            log.debug("measures response Body: " + response.getBody());
            vcpResourcesModels = objectMapper.readValue(response.getBody(), new TypeReference<List<ResourcesForVcpusModel>>() { });
            log.debug("measures list size: " + vcpResourcesModels.size());
            for (ResourcesForVcpusModel vcpResourcesModel : vcpResourcesModels) {
                vcpResourcesModel.setMetrics(metrics);

            }
        } catch (Exception e) {
            log.warn("Unexpected Error retrieving or parsing data: ", e);
        }
    }

    private String formatterInstantYesterdayToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME
                .withZone(ZoneId.from(ZoneOffset.UTC));
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        String formattedInstantToString = formatter.format(yesterday);
        String dt = formattedInstantToString.substring(0, 11);
        String hr = "00:00:00";
        String yesterdayDtTime = dt + "" + hr;
        return yesterdayDtTime;
    }

    private String formatterInstantNowToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME
                .withZone(ZoneId.from(ZoneOffset.UTC));
        Instant now = Instant.now();
        String formattedInstantNowToString = formatter.format(now);
        String dtNow = formattedInstantNowToString.substring(0, 11);
        String hr = "00:00:00";
        String NowDtTime = dtNow + "" + hr;
        return NowDtTime;
    }

    private HttpHeaders createHttpHeaders() {
        String tokenString = auhtToken.token.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenString);
        headers.add("X-Auth-Token", tokenString);
        return headers;
    }

    private List<ImageModel> parseImage(String value) throws NotFoundException {
        List<ImageModel> res = new ArrayList<>();

        if (value.equals("[]")) {
            // throw new NotFoundException();
            log.info(value);
        } else {
            ImageModel r = new ImageModel();
            String[] values = StringUtils.split(value, ", ");
            value = values[0];

            // Strip away square brackets
            String firstCleanValue = StringUtils.delete(values[0], "[");
            String firstClean = StringUtils.delete(firstCleanValue, "ImageModel");

            // // Strip away square brackets
            String firstResultString = StringUtils.delete(firstClean, "(");
            value = values[1];
            String secondCleanValue = StringUtils.delete(values[1], "]");
            String secondClean = StringUtils.delete(secondCleanValue, ")");
            String resultString = firstResultString + ", " + secondClean;

            // Convert comma separated String to String[]
            String[] s = StringUtils.commaDelimitedListToStringArray(resultString);
            String srv = String.valueOf(s[1].substring(9, s[1].length()));
            String iref = String.valueOf(s[2].substring(11, s[2].length()));

            // Map to Object
            r.setService(srv);
            r.setImage_ref(iref);
            res.add(r);
        }

        return res;
    }
}
