package it.irideos.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.VmResourcesModel;
import it.irideos.metrics.repository.VmResourceRepository;
import jakarta.transaction.Transactional;

@Service
public class VmResourceService {

    @Autowired
    private VmResourceRepository vmResourceRepository;

    @Transactional
    public VmResourcesModel createVmResource(VmResourcesModel vmResource) {
        return vmResourceRepository.saveAndFlush(vmResource);
    }

    @Transactional
    public VmResourcesModel listVmResourceById(Long id) {
        return vmResourceRepository.findById(id).orElseThrow();
    }
}
