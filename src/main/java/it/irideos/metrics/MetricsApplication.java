package it.irideos.metrics;

import java.util.List;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.irideos.metrics.configurations.DataConfig;
import it.irideos.metrics.models.User;
import it.irideos.metrics.service.UserService;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MetricsApplication {

	private Token token = null;

	@Autowired
	public UserService userService;

	@Autowired
	public DataConfig config;

	// @Autowired
	// public OcloudAuth auth;

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);
	}

	@PostConstruct
	public void run() {
		// User Example Usage
		User user = new User(1L, "Marco", "Colaiuda");
		System.out.println("User Before: " + user.getFirstName());
		String endpoint = config.getEndpoint();
		String username = config.getUserName();
		String password = config.getPassword();
		String domainName = config.getDomainName();
		String region = config.getRegion();

		Identifier domainIdentifier = Identifier.byName(domainName);

		// Persist User object to database through UserService
		user = userService.createUser(user);
		System.out.println("User After: " + user.getFirstName());
		// System.out.println("oCloud Auth: " + auth.print());

		// IMPLEMENTARE MOTORE DI LOGGING!

		// Identity V3 Authentication Example - change params to fit your needs!!
		OSClient.OSClientV3 os = OSFactory.builderV3()
				.endpoint(endpoint)
				.credentials(username, password, domainIdentifier)
				.authenticate();
		os.useRegion(region);
		this.token = os.getToken();
		List<? extends Server> Servers = os.compute().servers().list();
		System.out.println("LISTA SERVER ACTIVE: " + Servers);
		System.out.println("TOKEN RILASCIATO: " + token);
	}
}
