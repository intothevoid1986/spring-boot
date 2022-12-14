package it.irideos.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.ResoucesForVcpusModel;
import it.irideos.metrics.repository.ResourceForVcpusRepository;
import jakarta.transaction.Transactional;

@Service
public class ResourceForVcpusService {

    @Autowired
    ResourceForVcpusRepository resourceForVcpusRepository;

    @Transactional
    public ResoucesForVcpusModel createResourceForVcpus(ResoucesForVcpusModel vcpusResource) {
        return resourceForVcpusRepository.save(vcpusResource);
    }

}
