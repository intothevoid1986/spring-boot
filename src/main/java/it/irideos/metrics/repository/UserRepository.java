package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.irideos.metrics.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

}
