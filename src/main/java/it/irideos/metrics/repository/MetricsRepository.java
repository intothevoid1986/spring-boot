package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.MetricsModel;

public interface MetricsRepository extends JpaRepository<MetricsModel, Long> {

    // @Query(value = "SELECT id, vcpus FROM metrics WHERE vcpus = :vcpus",
    // nativeQuery = true)
    // List<MetricsModel> findByVcpu(@Param("vcpus") String vcpu);
}
