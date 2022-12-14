package it.irideos.metrics.configurations;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import it.irideos.metrics.models.OauthModel;
import it.irideos.metrics.models.UserModel;
import it.irideos.metrics.service.UserService;
import jakarta.annotation.PostConstruct;

@Configuration
public class OcloudAuth {

    @Autowired
    public OauthModel authConfig;
    @Autowired
    public UserService userService;

    public Token token = null;

    @PostConstruct
    public void auth() {
        String endpoint = authConfig.getEndpoint();
        String username = authConfig.getUserName();
        String password = authConfig.getPassword();
        String domainName = authConfig.getDomainName();
        // String projectId = authConfig.getProjectId();
        String region = authConfig.getRegion();

        Identifier domainIdentifier = Identifier.byName(domainName);

        // Identity V3 Authentication Example - change params to fit your needs!!
        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(username, password, domainIdentifier)
                .authenticate();
        os.useRegion(region);
        this.token = os.getToken();

        // User Example Usage
        UserModel user = new UserModel(1L, "Marco", true);

        // Persist User object to database through UserService
        user = userService.createUser(user);
    }

    public String getToken() {
        final String tokenToString;
        tokenToString = token.toString();
        return tokenToString;
    }
}
