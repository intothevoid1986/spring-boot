package it.irideos.metrics.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.ClusterModel;
import it.irideos.metrics.repository.ClusterRepository;

@Service
public class ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    public Map<ClusterModel, String> findClusterName(String service, String displayName)
            throws NotFoundException, RuntimeException {
        Map<ClusterModel, String> cluster = new HashMap<ClusterModel, String>();
        if (service != null && displayName != null) {
            List<ClusterModel> clusterModel = clusterRepository.findClusterNameByService(service);
            for (ClusterModel clsName : clusterModel) {
                String name = clsName.getClusterName();
                if (displayName.contains(name)) {
                    cluster.put(clsName, name);
                }
            }
        }
        return cluster;
    }
}
