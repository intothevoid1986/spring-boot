package it.irideos.metrics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.irideos.metrics.models.UsageHourClusterModel;

public interface UsageHourClusterRepository extends JpaRepository<UsageHourClusterModel, Long> {

    @Query(value = "SELECT MAX(uhc.total_resource) as MaxVm, MIN(uhc.total_resource) as MinVm,\r\n" + //
            "SUM(uhc.total_cost) as TotalCost\r\n" + //
            "FROM usage_hour_cluster uhc\r\n" + //
            "WHERE uhc.cluster_name = :cluster_name", nativeQuery = true)
    List<UsageHourClusterModel> findClusterDayCostByCluster(@Param("cluster_name") String clusterName);

}
