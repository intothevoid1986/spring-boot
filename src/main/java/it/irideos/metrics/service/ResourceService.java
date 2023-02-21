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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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

/**
 * The class provide a service for crud operation in entity resources
 * 
 * @author MarcoColaiuda
 */

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

    // verify that resource exist into table
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

    // call gnocchi api
    public List<MetricsModel> getResourceForVcpu(String vcpu)
            throws JsonMappingException, JsonProcessingException, NotFoundException {
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

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                    requestEntity, String.class,
                    vcpu);
            List<MetricsModel> metricsModelList = objectMapper.readValue(response.getBody(),
                    new TypeReference<List<MetricsModel>>() {
                    });
            return metricsModelList;
        } catch (Exception e) {
            log.warn("Unexpected Error retrieving or parsing data: ", e);
        }
        return null;
    }

    public List<Object[]> getDisplayNameAndTimestamp(String vcpu) {
        List<Object[]> displayNameAndTimestampByVcpu = resourceRepository
                .findDisplayNameAndTimestampByVcpus(vcpu);

        return displayNameAndTimestampByVcpu;

    }

    public List<Object[]> getPriceByFlavorName(String flavor) {
        List<Object[]> priceByFlavor = resourceRepository.findPriceByFlavorName(flavor);

        return priceByFlavor;
    }
}
