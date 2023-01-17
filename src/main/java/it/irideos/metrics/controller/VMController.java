package it.irideos.metrics.controller;

import java.util.List;

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
import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.models.ResourceModel;
import it.irideos.metrics.models.VMModel;
import it.irideos.metrics.service.ResourceService;
import it.irideos.metrics.service.VMService;
import it.irideos.metrics.utils.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class VMController {

    private VMModel[] vmResources;

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

    @PostConstruct
    private void getVMInstances() throws JsonMappingException, JsonProcessingException {
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl + "/resource/instance";
        HttpHeaders headers = HttpUtils.createHttpHeaders(authToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // Mappo la risposta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            vmResources = objectMapper.readValue(response.getBody(), VMModel[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (VMModel vmResource : vmResources) {
            ResourceModel p = vmResource.getResource();

            List<MetricsModel> metrics = resourceService.getResourceForVcpu(p.getVcpus());
            // TODO: debuggare i loop per capire come vengono "appese" le metriche
            // all'oggetto padre.
            metrics.forEach(metric -> {
                vmResource.getResource().setMetrics(metric);
            });
        }

        log.info(resourceService);

        for (VMModel vmResource : vmResources) {
            VmResourceService.createVmResource(vmResource);
        }
    }
}
