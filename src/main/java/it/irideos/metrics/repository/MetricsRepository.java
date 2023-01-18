package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.MetricsModel;

public interface MetricsRepository extends JpaRepository<MetricsModel, Long> {

}
