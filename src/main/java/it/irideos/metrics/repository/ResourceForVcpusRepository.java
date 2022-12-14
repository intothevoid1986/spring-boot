package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.ResoucesForVcpusModel;

public interface ResourceForVcpusRepository extends JpaRepository<ResoucesForVcpusModel, Long> {

}
