package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.User;
import com.banana.infrastructure.orm.models.SUser;

public class UserPivot {
  public static SUser fromDomainToInfrastructure(User user) {
    if (user != null) {
      SUser sUser = new SUser(user.getLastname(), user.getFirstname(), user.getUsername());
      if (user.getId() > 0) sUser.setId(user.getId());
      return sUser;
    } else
      return null;
  }

  public static User fromInfrastructureToDomain(SUser sUser) {
    if (sUser != null) {
      User user = new User(sUser.getId(), sUser.getLastname(), sUser.getFirstname(), sUser.getUsername());
      return user;
    } else
      return null;
  }
}
