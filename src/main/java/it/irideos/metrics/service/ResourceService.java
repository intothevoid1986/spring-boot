package it.irideos.metrics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.irideos.metrics.configurations.GnocchiConfig;
import it.irideos.metrics.configurations.OcloudAuth;
import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.models.ResourcesModel;
import it.irideos.metrics.utils.HttpUtils;
import it.irideos.metrics.utils.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResourceService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GnocchiConfig gnocchiConfig;

    @Autowired
    private OcloudAuth authToken;

    @Autowired
    private MetricsService metricsService;

    // @Autowired
    // ResourceForVcpusRepository resourceForVcpusRepository;

    // @Transactional
    // public ResourcesForVcpusModel createResourceForVcpus(ResourcesForVcpusModel
    // vcpusResource) {
    // return resourceForVcpusRepository.saveAndFlush(vcpusResource);
    // }

    public void getResourceForVcpu(String vcpu) {
        String dtFrom = Utils.formatterInstantYesterdayToString();
        String dtTo = Utils.formatterInstantNowToString();
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl +
                "/metric/{vcpu}/measures?aggregation=count&start=" + dtFrom + "&stop=" + dtTo;
        HttpHeaders headers = HttpUtils.createHttpHeaders(authToken);
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
            List<ResourcesModel> vcpResourcesModels = objectMapper.readValue(response.getBody(),
                    new TypeReference<List<ResourcesModel>>() {
                    });
            log.debug("measures list size: " + vcpResourcesModels.size());
            for (ResourcesModel vcpResourcesModel : vcpResourcesModels) {
                vcpResourcesModel.setMetrics(metrics);

            }
        } catch (Exception e) {
            log.warn("Unexpected Error retrieving or parsing data: ", e);
        }
    }

}
