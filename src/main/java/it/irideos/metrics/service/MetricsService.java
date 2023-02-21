package it.irideos.metrics.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.repository.MetricsRepository;
import jakarta.transaction.Transactional;
import lombok.Data;

/**
 * The class provide a service for crud operation in entity metrics
 * 
 * @author MarcoColaiuda
 */

@Service
@Data
public class MetricsService {

  @Autowired
  private MetricsRepository metricsRepository;

  @Transactional
  public List<MetricsModel> listMetrics(List<String> vcpus) throws NotFoundException, RuntimeException {
    return metricsRepository.findAll();
  }
}
