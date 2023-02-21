package it.irideos.metrics.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

/**
 * The class provide a model for map auth requestin OpenStack
 * 
 * @author MarcoColaiuda
 */

@Data
@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:openstack.properties")
@ConfigurationProperties("auth")
public class OauthModel {

    public String endpoint;
    public String userName;
    public String password;
    public String tenantName;
    public String region;
    public String domainName;
    public String projectId;

}