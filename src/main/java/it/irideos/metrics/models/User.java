package it.irideos.metrics.models;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity(name = "users")
@Data
public class User {

	@Id
	@SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
	private Long id;

	@Column(name = "name")
	private String firstName;

}
