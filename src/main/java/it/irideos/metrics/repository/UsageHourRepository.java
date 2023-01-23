package it.irideos.metrics.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.irideos.metrics.models.UsageHourModel;

public interface UsageHourRepository extends JpaRepository<UsageHourModel, Long> {

        @Query(value = "SELECT SUM(me.vcpusnumber), r.flavor_name, r.resource_id FROM resources r\r\n" + //
            "join metrics mt on r.resource_id = mt.id\r\n" + //
            "join measure me on me.metrics_vcpus = mt.id\r\n" + //
            "join image im on im.image_ref = r.image_ref\r\n" + //
                    "WHERE r.display_name = :display_name and me.timestamp = :timestamp\r\n" + //
                        "group by r.flavor_name, r.resource_id", nativeQuery = true)
    List<Object[]> findVmAndFlavorIdByDisplayName(@Param("display_name") String displayName,
            @Param("timestamp") Timestamp timestamp);
}
