package it.irideos.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.UserModel;
import it.irideos.metrics.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Transactional
  public UserModel createUser(UserModel user) {
    return userRepository.save(user);
  }
}
