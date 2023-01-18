package it.irideos.metrics.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.irideos.metrics.models.ClusterModel;

public interface ClusterRepository extends JpaRepository<ClusterModel, Long> {

    @Query(value = "SELECT id, service, cluster_name FROM cluster WHERE service = :service", nativeQuery = true)
    ArrayList<ClusterModel> findClusterNameByService(@Param("service") String srv);
}
