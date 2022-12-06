package it.irideos.metrics.models;

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

@Entity(name = "vm_resource")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class VmResource extends BaseModel {

    @Id
    @SequenceGenerator(name = "vm_sequence", sequenceName = "vm_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vm_sequence")
    private Long id;

    @Column(name = "display_name")
    private String display_name;

    @Column(name = "flavor_name")
    private String flavor_name;

    @Column(name = "vcpus")
    private String vcpus;

}
