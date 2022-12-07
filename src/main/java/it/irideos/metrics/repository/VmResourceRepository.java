package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.irideos.metrics.models.VmResourceModel;

@Repository
public interface VmResourceRepository extends JpaRepository<VmResourceModel, Long> {

}