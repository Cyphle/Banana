package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SUser;

public interface IUserRepository {
  SUser getUserByUsername(String username);
}
