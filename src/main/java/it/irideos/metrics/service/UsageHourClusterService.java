package it.irideos.metrics.service;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.UsageHourClusterModel;
import it.irideos.metrics.repository.UsageHourClusterRepository;

/**
 * The class provide a service for crud operation in entity usage_hour_cluster
 * 
 * @author MarcoColaiuda
 */

@Service
public class UsageHourClusterService {

    @Autowired
    private UsageHourClusterRepository usageHourClusterRepository;

    public UsageHourClusterModel createUsageHourCluster(UsageHourClusterModel totalUsage) throws NotFoundException {
        return usageHourClusterRepository.save(totalUsage);
    }

}
