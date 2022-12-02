package it.irideos.metrics;

import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.openstack.OSFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.irideos.metrics.models.User;

@SpringBootApplication
public class MetricsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);

		// User Example Usage
		User user = new User(1L, "Andrea");
		System.out.println(user.getFirstName());

		// Identity V2 Authentication Example - change params to fit your needs!!
		OSClientV2 os = OSFactory.builderV2()
				.endpoint("http://127.0.0.1:5000/v2.0")
				.credentials("admin", "sample")
				.tenantName("admin")
				.authenticate();
	}
}
