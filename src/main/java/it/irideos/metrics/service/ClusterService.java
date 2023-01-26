package it.irideos.metrics.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.repository.ClusterRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClusterService {

    public ArrayList<String> clusterN = new ArrayList<String>();

    @Autowired
    private ClusterRepository clusterRepository;

    public List<Object[]> findClusterName(String service, String displayName) {
        List<Object[]> clusterName = new ArrayList<Object[]>();

        if (service != null && displayName != null) {
            clusterName = clusterRepository.findClusterNameByService(service);
            for (Object[] clsName : clusterName) {
                String name = "";
                name = (String) clsName[0];
                String dName = "";
                dName = displayName.substring(0, 4);
                String clName = "";
                clName = name.substring(0, 4);
                if (dName.compareToIgnoreCase(clName) == 0) {
                    clusterN.add(name);
                }
            }
            log.info(clusterName.toString());
        }
        return clusterName;
    }
}
