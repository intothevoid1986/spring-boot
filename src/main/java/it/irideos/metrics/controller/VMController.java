package it.irideos.metrics.controller;

import java.sql.Timestamp;
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
import it.irideos.metrics.repository.ResourceRepository;
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
            imageService.getImageRef(vmResource);
            List<MetricsModel> metrics = resourceService.getResourceForVcpu(p.getVcpus());
            vmResource.getResource().setMetrics(metrics);
            List<Object[]> displayNameAndTimestamp = resourceRepository
                    .findDisplayNameAndTimestampByVcpus(p.getVcpus());
            for (Object[] objNameAndTimestamp : displayNameAndTimestamp) {
                String displayName = (String) objNameAndTimestamp[0];
                Timestamp timestamp = (Timestamp) objNameAndTimestamp[1];
                List<Object[]> countVmForFalvorId = usageHourService.findVmAndFlavorByDisplayName(displayName,
                        timestamp);
                for (Object[] objCountVmForFlavorId : countVmForFalvorId) {
                    Long vm = (Long) objCountVmForFlavorId[0];
                    String flavorId = (String) objCountVmForFlavorId[1];
                    System.out.println("COUNT VM E FLAVOR: " + vm + ", " + flavorId);
                    List<Object[]> costForFlavorName = resourceRepository.findPriceByFlavorName(flavorId);
                    for (Object[] hourlyRate : costForFlavorName) {
                        Double hourlyR = (Double) hourlyRate[0];
                        Double cost = vm * hourlyR;
                        System.out.println("COSTO ORARIO: " + cost);
                    }

                }
            }

        }

        log.info(resourceService);

        for (VMModel vmResource : vmResources) {
            VmResourceService.createVmResource(vmResource);
        }
    }
}
