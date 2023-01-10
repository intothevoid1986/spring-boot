package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.TenantModel;

public interface UserRepository extends JpaRepository<TenantModel, Long> {

}
