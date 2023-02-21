package it.irideos.metrics.service;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.VMModel;
import it.irideos.metrics.repository.ResourceRepository;
import it.irideos.metrics.repository.VMRepository;
import jakarta.transaction.Transactional;

/**
 * The class provide a service for crud operation in entity resources
 * 
 * @author MarcoColaiuda
 */

@Service
public class VMService {

    @Autowired
    private VMRepository vmResourceRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Transactional
    public VMModel createVmResource(VMModel vmResource) throws NotFoundException, RuntimeException {
        if (resourceRepository.existsResourceModelByVcpus(vmResource.getResource().getVcpus())) {
            return vmResource;
        }
        return vmResourceRepository.saveAndFlush(vmResource);
    }

    @Transactional
    public VMModel listVmResourceById(Long id) throws NotFoundException, RuntimeException {
        return vmResourceRepository.findById(id).orElseThrow();
    }
}
