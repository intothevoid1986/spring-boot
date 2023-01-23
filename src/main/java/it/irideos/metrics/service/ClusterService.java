package it.irideos.metrics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.ClusterModel;
import it.irideos.metrics.repository.ClusterRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClusterService {

    // private List<ClusterModel> clusterModels;

    @Autowired
    private ClusterRepository clusterRepository;

    public List<ClusterModel> findClusterName(String service, String displayName) {
        List<ClusterModel> clusterName = new ArrayList<ClusterModel>();

        if (service != null) {
            clusterName = clusterRepository.findClusterNameByService(service);
            List<ClusterModel> result = clusterName.stream()
                    .filter(a -> Objects.equals(a.cluster_name, "Kube-Test-2"))
                    .collect(Collectors.toList());
            System.out.println("FILTERED CLUSTER NAME: " + result);
            log.info(clusterName.toString());
        }
        return clusterName;
    }
}
