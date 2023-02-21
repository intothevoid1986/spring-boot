package it.irideos.metrics.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.UsageHourModel;
import it.irideos.metrics.repository.UsageHourRepository;
import jakarta.transaction.Transactional;
import lombok.Data;

/**
 * The class provide a service for crud operation in entity usage_hour
 * 
 * @author MarcoColaiuda
 */

@Service
@Data
public class UsageHourService {
    Long sumVcpu;
    String flavor;

    @Autowired
    private UsageHourRepository usageHourRepository;

    public Map<Long, String> findVmAndFlavorByDisplayName(String displayName, Timestamp timestamp)
            throws NotFoundException {
        Map<Long, String> vcpusMap = new HashMap<Long, String>();
        if (displayName != null && timestamp != null) {
            List<Object[]> usageHourModels = usageHourRepository.findVmAndFlavorIdByDisplayName(displayName, timestamp);
            for (Object[] vcpu : usageHourModels) {
                sumVcpu = null;
                flavor = "";
                sumVcpu = (Long) vcpu[0];
                flavor = (String) vcpu[1];
                if (sumVcpu != null && flavor != null) {
                    vcpusMap.put(sumVcpu, flavor);
                }
            }
        }
        return vcpusMap;
    }

    @Transactional
    public UsageHourModel createUsageHourly(UsageHourModel usage) throws NotFoundException, RuntimeException {
        return usageHourRepository.save(usage);
    }

    public List<Object[]> getUsageHourCluster(Long clusterId, Timestamp dtFrom, Timestamp dtTo) {
        List<Object[]> usageHourCluster = usageHourRepository.findUsageHourCluster(clusterId, dtFrom, dtTo);

        return usageHourCluster;
    }

}
