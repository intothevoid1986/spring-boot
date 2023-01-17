package it.irideos.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetricsApplication {

	public static void main(String[] args) {
		int returnCode = SpringApplication.exit(SpringApplication.run(MetricsApplication.class, args));
		System.exit(returnCode);
	}
}
