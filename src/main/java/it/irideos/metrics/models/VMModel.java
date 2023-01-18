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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "resources")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VMModel extends BaseModel {

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

  @JsonProperty(value = "metrics")
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "resource_id")
  private ResourceModel resource;

  @JsonIgnore
  public Long getId() {
    return this.id;
  }

  @JsonIgnore
  public void setId(Long id) {
    this.id = id;
  }

}
