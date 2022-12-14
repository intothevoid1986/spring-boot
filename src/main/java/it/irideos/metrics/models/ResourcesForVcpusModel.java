package it.irideos.metrics.models;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourcesForVcpusModel extends BaseModel {

    @Id
    @SequenceGenerator(name = "vcpu_sequence", sequenceName = "vcpus_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vcpus_sequence")
    private Long id;

    @JsonProperty(value = "timestamp")
    private ZonedDateTime timestamp;

    @JsonProperty(value = "granularity")
    private Double granularity;

    @JsonProperty(value = "vcpusnumber")
    private Double vcpusnumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "metrics_vcpus")
    private MetricsModel metrics;
}
