package it.irideos.metrics;

import java.util.List;

import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.model.compute.Server;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.irideos.metrics.models.User;
import it.irideos.metrics.service.UserService;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MetricsApplication {

	@Autowired
	public UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);

	}

	@PostConstruct
	public void run() {
		// User Example Usage
		User user = new User(1L, "Marco", "Colaiuda");
		System.out.println("User Before: " + user.getFirstName());

		// Persist User object to database through UserService
		user = userService.createUser(user);
		System.out.println("User After: " + user.getFirstName());

		// TODO: IMPLEMENTARE MOTORE DI LOGGING!

		// Identity V2 Authentication Example - change params to fit your needs!!
		OSClientV2 os = OSFactory.builderV2()
				.endpoint("https://api.it-mil1.entercloudsuite.com//v2.0")
				.credentials("marco.colaiuda@irideos.it", "Hondafour750!!")
				.tenantName("marco.colaiuda@irideos.it")
				.authenticate();
		os.useRegion("it-mil1");
		List<? extends Server> eastServers = os.compute().servers().list();
		System.out.println(eastServers);

	}
}
