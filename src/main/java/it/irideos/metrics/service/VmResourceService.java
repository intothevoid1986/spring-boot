package it.irideos.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.VmResource;
import it.irideos.metrics.repository.VmResourceRepository;
import jakarta.transaction.Transactional;

@Service
public class VmResourceService {

    @Autowired
    private VmResourceRepository vmResourceRepository;

    @Transactional
    public VmResource createVmResource(VmResource vmResource) {
        return vmResourceRepository.save(vmResource);
    }
}
