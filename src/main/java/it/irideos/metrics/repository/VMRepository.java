package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.VMModel;

public interface VMRepository extends JpaRepository<VMModel, Long> {

}