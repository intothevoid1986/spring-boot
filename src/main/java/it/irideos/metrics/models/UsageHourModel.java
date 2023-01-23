package it.irideos.metrics.models;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "usage_hour")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQuery(name = "UsageHourModel.findAll", query = "SELECT u FROM UsageHourModel u")
public class UsageHourModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long Id;

    @Column(name = "cluster_name")
    private String cluster_name;

    @Column(name = "cost")
    private Long cost;

    @Column(name = "resource_total")
    private int resource_total;

    @Column(name = "time_slot")
    private ZonedDateTime time_slot;
}
