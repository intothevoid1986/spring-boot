package it.irideos.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.irideos.metrics.configurations.OcloudAuth;

@SpringBootApplication
public class MetricsApplication {

	@Autowired
	public static OcloudAuth auth;

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);
	}
}
