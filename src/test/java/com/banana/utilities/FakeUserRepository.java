package com.banana.utilities;

import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.infrastructure.orm.models.SUser;

public class FakeUserRepository implements IUserRepository {
  public SUser getUserByUsername(String username) {
    SUser user = new SUser("Doe", "John", "john@doe.fr");
    return user;
  }
}
