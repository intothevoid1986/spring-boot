package it.irideos.metrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.irideos.metrics.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
