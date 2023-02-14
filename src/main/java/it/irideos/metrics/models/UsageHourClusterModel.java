package it.irideos.metrics.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "usage_hour_cluster")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UsageHourClusterModel extends BaseModel {

    @Id
    @SequenceGenerator(name = "usage_hour_cluster_sequence", sequenceName = "usage_hour_cluster_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usage_hour_cluster_sequence")
    private Long Id;

    @Column(name = "clusterName")
    private String clusterName;

    @Column(name = "totalCost")
    private Double totalCost;

    @Column(name = "total_resource")
    private Long total_resource;

    @Column(name = "timestamp")
    private Timestamp timestamp;

}
