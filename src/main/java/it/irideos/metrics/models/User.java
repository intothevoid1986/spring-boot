package it.irideos.metrics.models;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity(name = "user")
@Data
public class User {

	@Id
	private Long id;

	@Column(name = "name")
	private String firstName;

}
