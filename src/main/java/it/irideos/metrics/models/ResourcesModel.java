package it.irideos.metrics.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourcesModel extends BaseModel {

  @Id
  private Long identifyier;

  @JsonProperty(value = "id")
  private String id;
  @JsonProperty(value = "display_name")
  private String displayName;
  @JsonProperty(value = "flavor_name")
  private String flavorName;
  @JsonProperty(value = "metrics")
  private MetricsModel metrics;

}
