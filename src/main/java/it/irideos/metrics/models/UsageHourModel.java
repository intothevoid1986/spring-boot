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

@Entity(name = "usage_hour")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UsageHourModel extends BaseModel {

    @Id
    @SequenceGenerator(name = "usageHourly_sequence", sequenceName = "usageHourly_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usageHourly_sequence")
    private Long Id;

    @Column(name = "cluster_name")
    private String cluster_name;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "resource_total")
    private Long resource_total;

    @Column(name = "time_slot")
    private Timestamp time_slot;

    @Column(name = "resource_id")
    private Long resource_id;

    @Column(name = "cluster_id")
    private Long cluster_id;
}
