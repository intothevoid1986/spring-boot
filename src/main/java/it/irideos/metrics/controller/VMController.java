package it.irideos.metrics.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.irideos.metrics.configurations.GnocchiConfig;
import it.irideos.metrics.configurations.OcloudAuth;
import it.irideos.metrics.models.ImageModel;
import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.models.VMModel;
import it.irideos.metrics.service.ResourceService;
import it.irideos.metrics.service.VmResourceService;
import it.irideos.metrics.utils.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class VMController {

    // private List<ImageModel> imageModels;
    // private List<ClusterModel> clustModels;
    // private List<ResourcesModel> vcpResourcesModels;
    private VMModel[] vmResources;
    private List<String> vcpus = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OcloudAuth authToken;

    @Autowired
    private GnocchiConfig gnocchiConfig;

    @Autowired
    private VmResourceService VmResourceService;

    @Autowired
    private ResourceService resourceService;

    // @Autowired
    // private MetricsService metricsService;

    // @Autowired
    // private ImageRepository imageRepository;

    // @Autowired
    // private ClusterRepository clusterRepository;

    @PostConstruct
    private void getVMInstances() throws JsonMappingException, JsonProcessingException {
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl + "/resource/instance";
        // String img = "";
        // String img_ref = "";
        // String srv = "";
        // String clusterName = "";
        HttpHeaders headers = HttpUtils.createHttpHeaders(authToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // Creo istanza
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // Mappo la risposta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            vmResources = objectMapper.readValue(response.getBody(), VMModel[].class);
        } catch (Exception e) {
            log.warn("Exception", e.getMessage());
        }

        for (VMModel vmResource : vmResources) {
            VmResourceService.createVmResource(vmResource);
            MetricsModel p = vmResource.getResource().getMetrics();

            vcpus.add(p.getVcpus());
        }
        vcpus.forEach(vcpu -> {
            resourceService.getResourceForVcpu(vcpu);
        });

        log.info(resourceService);
    }
}
