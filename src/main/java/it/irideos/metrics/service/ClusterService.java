package it.irideos.metrics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.ClusterModel;
import it.irideos.metrics.repository.ClusterRepository;

@Service
public class ClusterService {

    private Map<ClusterModel, String> cluster = new HashMap<ClusterModel, String>();

    @Autowired
    private ClusterRepository clusterRepository;

    public List<ClusterModel> findClusterName(String service, String displayName) {
        List<ClusterModel> clusterModel = new ArrayList<ClusterModel>();
        if (service != null && displayName != null) {
            clusterModel = clusterRepository.findClusterNameByService(service);
            for (ClusterModel clsName : clusterModel) {
                String clusterN = clsName.getCluster_name();
                if (displayName.contains(clusterN)) {
                    cluster.put(clsName, clusterN);
                    System.out.println("MAP ENTRY: " + cluster);
                }
            }
        }
        return clusterModel;
    }

    public Map<ClusterModel, String> getClusterMap() {
        return cluster;
    }
}
