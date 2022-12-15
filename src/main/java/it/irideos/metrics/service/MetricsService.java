package it.irideos.metrics.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.MetricsModel;
import it.irideos.metrics.repository.MetricsRepository;
import lombok.Data;

@Service
@Data
public class MetricsService {

  @Autowired
  private MetricsRepository metricsRepository;

  private MetricsModel found = new MetricsModel();

  public MetricsModel listVmByVcpus(String vcpus) throws NotFoundException {
    List<MetricsModel> list = metricsRepository.findAll();
    MetricsModel model = new MetricsModel();
    model.setVcpus(vcpus);
    list.forEach(element -> {
      if (element.getVcpus().equals(vcpus)) {
        found = element;
      }
    });
    return found;
  }

}
