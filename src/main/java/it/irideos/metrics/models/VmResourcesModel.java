package it.irideos.metrics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "resources")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VmResourcesModel extends BaseModel {

  @Id
  @SequenceGenerator(name = "vm_sequence", sequenceName = "vm_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vm_sequence")
  private Long id;

  @JsonProperty(value = "id")
  private String identifier;

  @JsonProperty(value = "display_name")
  private String displayName;

  @JsonProperty(value = "flavor_name")
  private String flavorName;

  @JsonProperty(value = "image_ref")
  private String imageRef;

  @JsonProperty(value = "type")
  private String type;

  @JsonProperty(value = "resource")
  private ResourcesForVcpusModel resource;

  @JsonIgnore
  public Long getId() {
    return this.id;
  }

  @JsonIgnore
  public void setId(Long id) {
    this.id = id;
  }

}
