package it.irideos.metrics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true) // -> this escape all unwanted fields from JSON, otherwise causing exception
@Entity(name = "metrics")
public class ResourceModel extends BaseModel {

  @Id
  @SequenceGenerator(name = "metrics_sequence", sequenceName = "metrics_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metrics_sequence")
  private Long id;

  @JoinColumn(name = "metrics")
  @JsonProperty(value = "vcpus") // -> this annotation map JSON field name with Java class field name
  private String vcpus;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "metrics_vcpus")
  @JsonIgnore
  private MetricsModel metrics;

}