package it.irideos.metrics.models;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "measure")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class ResourcesForVcpusModel extends BaseModel {

    @Id
    @SequenceGenerator(name = "vcpu_sequence", sequenceName = "vcpus_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vcpus_sequence")
    @JsonIgnore
    private Long id;

    @Basic
    @JsonProperty(index = 0)
    private ZonedDateTime timestamp;

    @JsonProperty(index = 1)
    private Double granularity;

    @JsonProperty(index = 2)
    private Double vcpusnumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "metrics_vcpus")
    @JsonIgnore
    private MetricsModel metrics;
}
