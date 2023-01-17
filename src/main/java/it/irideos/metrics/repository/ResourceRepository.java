package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.ResourcesModel;

public interface ResourceRepository extends JpaRepository<ResourcesModel, Long> {

}
