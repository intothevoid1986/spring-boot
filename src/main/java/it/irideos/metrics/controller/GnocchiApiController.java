package it.irideos.metrics.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import it.irideos.metrics.configurations.GnocchiConfig;
import it.irideos.metrics.configurations.OcloudAuth;
import it.irideos.metrics.mapper.GnocchiApiMapper;
import jakarta.annotation.PostConstruct;

@RestController
public class GnocchiApiController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private OcloudAuth auhtToken;

    @Autowired
    private GnocchiConfig gnocchiConfig;

    @PostConstruct
    private void getGnocchiInstance() {
        try {
            String body = "";
            String gnocchiUrl = gnocchiConfig.getEndpoint();
            String url = gnocchiUrl + "/resource/instance";
            System.out.println("Gnocchi Url Compose: " + url);
            HttpHeaders headers = createHttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            System.out.printf("RESPONSE: ", response.getBody());

            // FIXME: Questo si incazza perché si aspetta un tipo Token, mentre response
            // è una stringa. Guarda qui per la soluzione:
            // https://auth0.com/blog/how-to-automatically-map-jpa-entities-into-dtos-in-spring-boot-using-mapstruct/
            GnocchiApiMapper.INSTANCE.tokenToTokenModel();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private HttpHeaders createHttpHeaders() {
        String tokenString = auhtToken.getToken();
        tokenString = extracted(tokenString);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenString);
        headers.add("X-Auth-Token", tokenString);
        System.out.println("Gnocchi- GetToken: " + tokenString);
        return headers;
    }

    private String extracted(String tokenString) {
        return tokenString.substring(17, 200);
    }
}
