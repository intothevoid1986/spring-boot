package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.ResourceModel;

public interface ResourceRepository extends JpaRepository<ResourceModel, Long> {
  boolean existsResourceModelByVcpus(String vcpu);
}
