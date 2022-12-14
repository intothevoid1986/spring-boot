package it.irideos.metrics.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "resource_vcpus")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResoucesForVcpusModel extends BaseModel {

    @Id
    @SequenceGenerator(name = "vcpu_sequence", sequenceName = "vcpus_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vcpus_sequence")
    private Long id;

    @JsonProperty(value = "timestamp")
    private Date timestamp;

    @JsonProperty(value = "tipo")
    private String tipo;

    @JsonProperty(value = "flavor_name")
    private String flavorname;

    @JsonProperty(value = "vcpusnumber")
    private Integer vcpusnumber;

}
