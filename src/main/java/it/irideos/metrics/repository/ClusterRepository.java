package it.irideos.metrics.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.irideos.metrics.models.ClusterModel;

@Repository
public interface ClusterRepository extends JpaRepository<ClusterModel, Long> {

    @Query(value = "SELECT id, cluster_name, service FROM cluster WHERE service = :service", nativeQuery = true)
    ArrayList<ClusterModel> findNameByClusterService(@Param("service") String srv);
}
