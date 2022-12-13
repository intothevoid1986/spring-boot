package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.VmResourceModel;

public interface VmResourceRepository extends JpaRepository<VmResourceModel, Long> {

}