package it.irideos.metrics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.irideos.metrics.models.ClusterModel;

public interface ClusterRepository extends JpaRepository<ClusterModel, Long> {
    @Query(value = "SELECT c.id, c.cluster_name, c.service FROM cluster c WHERE c.service = :service", nativeQuery = true)
    List<ClusterModel> findClusterNameByService(@Param("service") String service);

    @Query(value = "SELECT c.id, c.cluster_name FROM cluster c WHERE c.id = :id", nativeQuery = true)
    List<Object[]> findClusterNameById(@Param("id") Long clusterId);

}
