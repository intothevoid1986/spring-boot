package it.irideos.metrics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.irideos.metrics.models.ClusterModel;

public interface ClusterRepository extends JpaRepository<ClusterModel, Long> {
    @Query(value = "SELECT c.cluster_name FROM cluster c WHERE c.service = :service", nativeQuery = true)
    List<Object[]> findClusterNameByService(@Param("service") String service);

    @Query(value = "SELECT cl.service FROM cluster cl WHERE cl.cluster_name = :cluster_name", nativeQuery = true)
    List<Object[]> findServiceByClusterName(@Param("cluster_name") String clName);
}
