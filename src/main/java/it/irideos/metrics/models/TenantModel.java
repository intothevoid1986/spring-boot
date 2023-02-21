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

/**
 * The class provide a model for map entity tenant
 * 
 * @author MarcoColaiuda
 */

@Entity(name = "tenant")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class TenantModel extends BaseModel {

	@Id
	@SequenceGenerator(name = "tenant_sequence", sequenceName = "tenant_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenant_sequence")
	private Long id;

	@Column(name = "tenant_name")
	private String name;

	@Column(name = "enabled")
	private Boolean enabled;

}
