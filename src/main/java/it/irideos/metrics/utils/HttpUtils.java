package it.irideos.metrics.utils;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import it.irideos.metrics.configurations.OcloudAuth;

/**
 * The class provide a util http method eventually shared in all application
 * 
 * @author MarcoColaiuda
 */

public class HttpUtils {
  // used for call gnocchi API
  public static HttpHeaders createHttpHeaders(OcloudAuth authToken) {
    String tokenString = authToken.token.getId();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenString);
    headers.add("X-Auth-Token", tokenString);
    return headers;
  }
}
