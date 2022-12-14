package it.irideos.metrics.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.irideos.metrics.configurations.GnocchiConfig;
import it.irideos.metrics.configurations.OcloudAuth;
import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.models.VmResourcesModel;
import it.irideos.metrics.service.VmResourceService;
import jakarta.annotation.PostConstruct;

@RestController
public class GnocchiApiController {

    private VmResourcesModel[] vmResources;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OcloudAuth auhtToken;

    @Autowired
    private GnocchiConfig gnocchiConfig;

    @Autowired
    private VmResourceService VmResourceService;

    Map<String, String> vcpus = new HashMap<>();

    @PostConstruct
    private void getGnocchiInstance() {
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl + "/resource/instance";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // Creo istanza
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Mappo la risposta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            vmResources = objectMapper.readValue(response.getBody(), VmResourcesModel[].class);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        for (VmResourcesModel vmResource : vmResources) {
            VmResourceService.createVmResource(vmResource);
            MetricsModel p = vmResource.getMetrics();
            vcpus.put("vcpu", p.getVcpus());
            GetResourceForVcpu();
        }

        VmResourcesModel tmp = VmResourceService.listVmResourceById(Long.valueOf("3"));
        System.out.println("TMP:\n" + tmp);
    }

    private void GetResourceForVcpu() {
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl +
                "/metric/{vcpu}/measures?aggregation=count&start=2022-11-30T14:00&stop=2022-11-30T15:00";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // Creo istanza
        // ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Mappo la risposta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                    requestEntity, String.class,
                    vcpus);
            System.out.println("VCPU RESOURCE RES:" + response.getBody());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
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
}
