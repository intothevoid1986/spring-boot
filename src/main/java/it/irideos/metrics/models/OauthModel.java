package it.irideos.metrics.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("spring.openstack.auth")
public class OauthModel {

    public String endpoint;
    public String userName;
    public String password;
    public String tenantName;
    public String region;
    public String domainName;
    public String projectId;

}