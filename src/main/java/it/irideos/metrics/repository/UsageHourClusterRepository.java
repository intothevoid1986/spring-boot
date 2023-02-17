package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.UsageHourClusterModel;

public interface UsageHourClusterRepository extends JpaRepository<UsageHourClusterModel, Long> {

}
