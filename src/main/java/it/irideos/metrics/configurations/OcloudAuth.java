package it.irideos.metrics.configurations;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import it.irideos.metrics.models.OauthModel;
import it.irideos.metrics.models.TenantModel;
import it.irideos.metrics.service.TenantService;
import jakarta.annotation.PostConstruct;

@Configuration
public class OcloudAuth {

    public Token token = null;

    @Autowired
    public OauthModel authConfig;

    @Autowired
    public TenantService tenantService;

    @PostConstruct
    public void auth() {
        String endpoint = authConfig.getEndpoint();
        String username = authConfig.getUserName();
        String password = authConfig.getPassword();
        String domainName = authConfig.getDomainName();
        String region = authConfig.getRegion();

        Identifier domainIdentifier = Identifier.byName(domainName);

        // Identity V3 Authentication on OpenStack
        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(username, password, domainIdentifier)
                .authenticate();
        os.useRegion(region);
        this.token = os.getToken();

        // persist username that login into tenant table
        TenantModel tenant = new TenantModel(1L, username, true);
        tenant = tenantService.createTenant(tenant);
    }

    // return token from Openstack auth
    public String getToken() {
        final String tokenToString;
        tokenToString = token.toString();
        return tokenToString;
    }
}
