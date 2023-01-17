package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.VMModel;

public interface VmResourceRepository extends JpaRepository<VMModel, Long> {

}