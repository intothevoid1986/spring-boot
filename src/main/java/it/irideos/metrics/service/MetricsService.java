package it.irideos.metrics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.repository.MetricsRepository;
import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
public class MetricsService {

  @Autowired
  private MetricsRepository metricsRepository;

  @Transactional
  public List<MetricsModel> listMetrics(List<String> vcpus) {
    return metricsRepository.findAll();
  }
}
