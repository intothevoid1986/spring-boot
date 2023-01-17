package it.irideos.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.VMModel;
import it.irideos.metrics.repository.ResourceRepository;
import it.irideos.metrics.repository.VMRepository;
import jakarta.transaction.Transactional;

@Service
public class VMService {

    @Autowired
    private VMRepository vmResourceRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Transactional
    public VMModel createVmResource(VMModel vmResource) {

        if (resourceRepository.existsResourceModelByVcpus(vmResource.getResource().getVcpus())) {
            // TODO: gestire il caso di ritorno per evitare exceptions.
            return vmResource;
        }
        return vmResourceRepository.saveAndFlush(vmResource);
    }

    @Transactional
    public VMModel listVmResourceById(Long id) {
        return vmResourceRepository.findById(id).orElseThrow();
    }
}
