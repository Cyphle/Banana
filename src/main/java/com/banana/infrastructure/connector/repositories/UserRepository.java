package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepository implements IUserRepository {
  private SUserRepository userRepository;

  @Autowired
  public UserRepository(SUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public SUser getUserByUsername(String username) {
    SUser user = this.userRepository.findByUsername(username);
    return this.userRepository.findByUsername(username);
  }
}
