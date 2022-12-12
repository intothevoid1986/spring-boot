package it.irideos.metrics.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("spring.gnocchi")
public class GnocchiConfig {

    public String endpoint;

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String Endpoint) {
        this.endpoint = Endpoint;
    }

    @Override
    public String toString() {
        return "{" +
                " gnocchiEndpoint='" + getEndpoint() + "'" +
                "}";
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
