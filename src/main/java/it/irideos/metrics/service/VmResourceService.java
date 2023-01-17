package it.irideos.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.VmResourcesModel;
import it.irideos.metrics.repository.MetricsRepository;
import it.irideos.metrics.repository.VmResourceRepository;
import jakarta.transaction.Transactional;

@Service
public class VmResourceService {

    @Autowired
    private VmResourceRepository vmResourceRepository;

    @Autowired
    private MetricsRepository metricsRepository;

    @Transactional
    public VmResourcesModel createVmResource(VmResourcesModel vmResource) {

        if (metricsRepository.existsMetricsModelByVcpus(vmResource.getResource().getMetrics().getVcpus())) {
            return vmResource;
        }
        return vmResourceRepository.saveAndFlush(vmResource);
    }

    @Transactional
    public VmResourcesModel listVmResourceById(Long id) {
        return vmResourceRepository.findById(id).orElseThrow();
    }
}
