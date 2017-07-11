package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.User;
import com.banana.infrastructure.orm.models.SUser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPivotTests {
  @Test
  public void should_pivot_user_from_domain_format_to_infrastructure() {
    User user = new User(1, "Doe", "John", "john@doe.fr");

    SUser sUser = UserPivot.fromDomainToInfrastructure(user);

    assertThat(sUser.getLastname()).isEqualTo("Doe");
    assertThat(sUser.getFirstname()).isEqualTo("John");
    assertThat(sUser.getEmail()).isEqualTo("john@doe.fr");
    assertThat(sUser.getUsername()).isEqualTo("john@doe.fr");
  }

  @Test
  public void should_pivot_user_from_infrastructure_to_domain() {
    SUser sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");

    User user = UserPivot.fromInfrastructureToDomain(sUser);

    assertThat(user.getFirstname()).isEqualTo("John");
    assertThat(user.getLastname()).isEqualTo("Doe");
    assertThat(user.getUsername()).isEqualTo("john@doe.fr");
  }
}
