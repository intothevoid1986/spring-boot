package it.irideos.metrics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.irideos.metrics.models.ResourceModel;

public interface ResourceRepository extends JpaRepository<ResourceModel, Long> {
  boolean existsResourceModelByVcpus(String vcpu);

  @Query(value = "SELECT r.display_name, me.timestamp, r.flavor_name, r.resource_id FROM resources r\r\n" + //
      "join metrics mt on r.resource_id = mt.id\r\n" + //
      "join measure me on me.metrics_vcpus = mt.id\r\n" + //
      "WHERE mt.vcpus = :vcpus", nativeQuery = true)
  List<Object[]> findDisplayNameAndTimestampByVcpus(@Param("vcpus") String vcpu);

  @Query(value = "SELECT p.hourly_rate FROM price p WHERE p.flavor_name = :flavor_name", nativeQuery = true)
  List<Object[]> findPriceByFlavorName(@Param("flavor_name") String flavor_name);
}
