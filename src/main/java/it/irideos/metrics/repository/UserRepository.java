package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.irideos.metrics.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

}
