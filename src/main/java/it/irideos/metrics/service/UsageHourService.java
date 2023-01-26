package it.irideos.metrics.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.UsageHourModel;
import it.irideos.metrics.repository.UsageHourRepository;
import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
public class UsageHourService {

    @Autowired
    private UsageHourRepository usageHourRepository;

    public List<Object[]> findVmAndFlavorByDisplayName(String displayName, Timestamp timestamp) {
        List<Object[]> usageHourModels = new ArrayList<Object[]>();
        if (displayName != null && timestamp != null) {
            usageHourModels = usageHourRepository.findVmAndFlavorIdByDisplayName(displayName, timestamp);
        }
        return usageHourModels;
    }

    public List<Object[]> findTotResourceForHour(String displayName, Timestamp timestamp) {
        List<Object[]> usageHourModels = new ArrayList<Object[]>();
        if (displayName != null && timestamp != null) {
            usageHourModels = usageHourRepository.findTotResourceForHourByTime(displayName, timestamp);
        }
        return usageHourModels;
    }

    @Transactional
    public UsageHourModel createUsageHourly(UsageHourModel usage) {
        return usageHourRepository.save(usage);
    }

}
