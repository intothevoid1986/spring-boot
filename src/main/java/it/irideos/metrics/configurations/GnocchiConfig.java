package it.irideos.metrics.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("spring.gnocchi")
public class GnocchiConfig {

    public String gnocchiEndpoint;

    public String getGnocchiEndpoint() {
        return gnocchiEndpoint;
    }

    public void setGnocchiEndpoint(String gnocchiEndpoint) {
        this.gnocchiEndpoint = gnocchiEndpoint;
    }

    @Override
    public String toString() {
        return "{" +
                " gnocchiEndpoint='" + getGnocchiEndpoint() + "'" +
                "}";
    }

}
