package it.irideos.metrics.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

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
import it.irideos.metrics.models.ResourceModel;
import it.irideos.metrics.repository.ResourceRepository;
import it.irideos.metrics.utils.HttpUtils;
import it.irideos.metrics.utils.MetricsUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private GnocchiConfig gnocchiConfig;

    @Autowired
    private OcloudAuth authToken;

    @Autowired
    private RestTemplate restTemplate;

    private ResourceModel found = new ResourceModel();

    public ResourceModel listVmByVcpus(String vcpus) throws NotFoundException {
        List<ResourceModel> list = resourceRepository.findAll();
        ResourceModel model = new ResourceModel();
        model.setVcpus(vcpus);
        list.forEach(element -> {
            if (element.getVcpus().equals(vcpus)) {
                found = element;
            }
        });
        return found;
    }

    public List<MetricsModel> getResourceForVcpu(String vcpu) {
        String dtFrom = MetricsUtils.formatterInstantYesterdayToString();
        String dtTo = MetricsUtils.formatterInstantNowToString();
        String gnocchiUrl = gnocchiConfig.getEndpoint();
        String url = gnocchiUrl +
                "/metric/{vcpu}/measures?aggregation=count&start=" + dtFrom + "&stop=" + dtTo;
        HttpHeaders headers = HttpUtils.createHttpHeaders(authToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ResourceModel resourceModel = this.listVmByVcpus(vcpu);
            log.info(resourceModel.toString());
            // Mappo la risposta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                    requestEntity, String.class,
                    vcpu);
            log.debug("measures response Body: " + response.getBody());
            List<MetricsModel> metricsModelList = objectMapper.readValue(response.getBody(),
                    new TypeReference<List<MetricsModel>>() {
                    });
            log.debug("measures list size: " + metricsModelList.size());
            // for (ResourceModel resourcesModel : resourceModelList) {
            // resourcesModel.setMetrics(resourceModel);

            // }
            return metricsModelList;
        } catch (Exception e) {
            log.warn("Unexpected Error retrieving or parsing data: ", e);
        }
        return null;
    }
}
