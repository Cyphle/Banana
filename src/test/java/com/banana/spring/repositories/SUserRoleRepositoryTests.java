package com.banana.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.banana.spring.models.SUser;
import com.banana.spring.models.SUserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.banana.spring.user.Roles;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SUserRoleRepositoryTests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  SUserRepository userRepository;

  @Autowired
  private SUserRoleRepository userRoleRepository;

  @Test
  public void should_get_user_roles_from_username() {
    SUser user = new SUser("Mac", "Do", "mac@do.fr", "macdo");
    user.setUsername("macdo");
    this.entityManager.persist(user);
    SUser dbUser = this.userRepository.findByUsername("macdo");
    SUserRole role = new SUserRole();
    role.setUserId(dbUser.getId());
    role.setRole(Roles.ROLE_USER.toString());
    this.entityManager.persist(role);

    List<String> roles = this.userRoleRepository.findRoleByUserName("macdo");
    assertThat(roles.size()).isEqualTo(1);
    assertThat(roles.get(0)).isEqualTo(Roles.ROLE_USER.toString());
  }
}
