package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.irideos.metrics.models.VmResource;

@Repository
public interface VmResourceRepository extends JpaRepository<VmResource, Long> {

}