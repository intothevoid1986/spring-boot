package it.irideos.metrics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.ClusterModel;

public interface ClusterRepository extends JpaRepository<ClusterModel, Long> {
    List<ClusterModel> findClusterNameByService(String service);
}
