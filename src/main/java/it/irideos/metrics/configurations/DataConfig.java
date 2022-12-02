package it.irideos.metrics.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("it.irideos.metrics.repository")
@EntityScan("it.irideos.metrics.models*")
@Configuration
public class DataConfig {

}