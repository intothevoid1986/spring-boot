package it.irideos.metrics.controller;

import java.util.Collections;

import org.openstack4j.model.identity.v3.Token;
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
import it.irideos.metrics.mapper.GnocchiApiMapper;
import it.irideos.metrics.models.TokenModel;
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
            // https://stackoverflow.com/questions/68744766/how-to-map-httpresponse-in-a-object-java
            
            //Creo istanza di ObjectMapper (Jakson)
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Mappo la risposta in oggeto Token
            Token token = objectMapper.readValue(response.getBody(), Token.class)
            
            // Mappo token in TokenModel
            TokenModel model = GnocchiApiMapper.INSTANCE
                    .tokenToTokenModel(token);
            
                    // Stampo
                    System.out.println(model.toString());

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
