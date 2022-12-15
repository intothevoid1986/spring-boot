package it.irideos.metrics.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import it.irideos.metrics.configurations.GnocchiConfig;
import it.irideos.metrics.configurations.OcloudAuth;
import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.models.ResourcesForVcpusModel;
import it.irideos.metrics.models.VmResourcesModel;
import it.irideos.metrics.service.ResourceForVcpusService;
import it.irideos.metrics.service.VmResourceService;
import jakarta.annotation.PostConstruct;

@RestController
public class GnocchiApiController {

    private VmResourcesModel[] vmResources;
    private List<ResourcesForVcpusModel> vcpResourcesModels;

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
            getResourceForVcpu();
        }

        VmResourcesModel tmp = VmResourceService.listVmResourceById(Long.valueOf("3"));
        System.out.println("TMP:\n" + tmp);
    }

    private void getResourceForVcpu() {
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl +
                "/metric/{vcpu}/measures?aggregation=count&start=2022-11-30T14:00&stop=2022-12-01T14:00";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // Creo istanza
        // ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Mappo la risposta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                    requestEntity, String.class,
                    vcpus);
            // vcpResourcesModels = objectMapper.readValue(response.getBody(),
            // ResourcesForVcpusModel[].class);
            // System.out.println("VCPU RESOURCE RES:" + vcpResourcesModels);
            vcpResourcesModels = parse(response.getBody());
            for (ResourcesForVcpusModel vcpResourcesModel : vcpResourcesModels) {
                System.out.println("\n\n" + vcpResourcesModel.toString());
                resourceForVcpusService.createResourceForVcpus(vcpResourcesModel);
            }
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

    private List<ResourcesForVcpusModel> parse(String value) throws NotFoundException {
        List<ResourcesForVcpusModel> res = new ArrayList<>();

        if (value.equals("[]")) {
            throw new NotFoundException();
        }

        while (value.indexOf("], ") != -1) {
            ResourcesForVcpusModel r = new ResourcesForVcpusModel();
            String[] values = StringUtils.split(value, "], ");
            value = values[1];
            // Strip away square brackets
            String cleanValue = StringUtils.delete(values[0], "[");
            cleanValue = StringUtils.delete(cleanValue, "]");
            // Convert comma separated String to String[]
            String[] s = StringUtils.commaDelimitedListToStringArray(cleanValue);

            // Strip away " from date field
            String date = StringUtils.delete(s[0], String.valueOf('"'));

            // Map to Object
            Instant time = Instant.parse(date);
            r.setTimestamp(time.atZone(ZoneId.systemDefault()));
            r.setGranularity(Double.valueOf(s[1]));
            r.setVcpusnumber(Double.valueOf(s[2]));
            res.add(r);
        }

        return res;
    }
}
