package it.irideos.metrics.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class OcloudAuth {

    @Autowired
    public DataConfig authConfig;

    String endp = authConfig.getEndpoint();

    @Bean
    public String print() {
        return endp;
    }

}
