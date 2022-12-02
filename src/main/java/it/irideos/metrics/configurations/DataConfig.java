package it.irideos.metrics.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("it.irideos.metrics.repository")
@EntityScan("it.irideos.metrics.models*")
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("spring.opestack.auth")
public class DataConfig {

    public String endpoint;
    public String userName;
    public String password;
    public String tenantName;
    public String region;
    public String domainName;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String Endpoint) {
        this.endpoint = Endpoint;
    }

    @Override
    public String toString() {
        return "{" +
                " endpoint='" + getEndpoint() + "'" +
                ", userName='" + getUserName() + "'" +
                ", password='" + getPassword() + "'" +
                ", tenantName='" + getTenantName() + "'" +
                ", region='" + getRegion() + "'" +
                ", domainName='" + getDomainName() + "'" +
                "}";
    }

}