package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.VmResourcesModel;

public interface VmResourceRepository extends JpaRepository<VmResourcesModel, Long> {

}