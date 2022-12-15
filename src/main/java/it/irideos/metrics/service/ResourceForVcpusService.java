package it.irideos.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.ResourcesForVcpusModel;
import it.irideos.metrics.repository.ResourceForVcpusRepository;
import jakarta.transaction.Transactional;

@Service
public class ResourceForVcpusService {

    @Autowired
    ResourceForVcpusRepository resourceForVcpusRepository;

    @Transactional
    public ResourcesForVcpusModel createResourceForVcpus(ResourcesForVcpusModel vcpusResource) {
        return resourceForVcpusRepository.saveAndFlush(vcpusResource);
    }

}
