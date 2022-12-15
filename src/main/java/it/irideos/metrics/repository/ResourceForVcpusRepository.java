package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.ResourcesForVcpusModel;

public interface ResourceForVcpusRepository extends JpaRepository<ResourcesForVcpusModel, Long> {

}
