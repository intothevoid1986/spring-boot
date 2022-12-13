package it.irideos.metrics.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true) // -> this escape all unwanted fields from JSON, otherwise causing exception
public class MetricsModel extends BaseModel {

  @JsonProperty(value = "vcpus") // -> this annotation map JSON field name with Java class field name
  private String vcpus;
}
