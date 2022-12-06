package it.irideos.metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.irideos.metrics.configurations.GnocchiConfig;
import it.irideos.metrics.configurations.OcloudAuth;

public class getGnocchiApi {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private OcloudAuth auhtToken;

    @Autowired
    private GnocchiConfig gnocchiConfig;

    public HttpHeaders getRequestHeaderBearer(String uri) {

        final String AUTHORIZATION_HEADER = "Authorization";
        String tokenString = auhtToken.GetToken();
        HttpHeaders headers = new HttpHeaders();
        String gnocchiUrl = gnocchiConfig.getGnocchiEndpoint();
        String url = gnocchiUrl + "/resource/instance";
        // String url =
        // "https://gnocchi.it-mil1.entercloudsuite.com/v1/resource/instance";
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(AUTHORIZATION_HEADER, "Bearer " + tokenString);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.printf("RESPONSE: ", response);
        return headers;
    }
}
