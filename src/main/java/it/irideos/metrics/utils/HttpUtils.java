package it.irideos.metrics.utils;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import it.irideos.metrics.configurations.OcloudAuth;

public class HttpUtils {
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
