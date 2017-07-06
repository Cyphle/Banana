package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.User;
import com.banana.infrastructure.orm.models.SUser;

public class UserPivot {
  public static SUser fromDomainToInfrastructure(User user) {
    SUser sUser = new SUser(user.getLastname(), user.getFirstname(), user.getUsername());
    return sUser;
  }

  public static User fromInfrastructureToDomain(SUser sUser) {
    User user = new User(sUser.getLastname(), sUser.getFirstname(), sUser.getUsername());
    return user;
  }
}
