package com.banana.infrastructure.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import com.banana.infrastructure.orm.repositories.SUserRoleRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class CustomUserDetailsServiceTests {
  @MockBean
  private SUserRepository userRepository;

  @MockBean
  private SUserRoleRepository userRoleRepository;

  private CustomUserDetailsService customUserDetailsService;

  @Before
  public void setUp() {
    this.customUserDetailsService = new CustomUserDetailsService(this.userRepository, this.userRoleRepository);
  }

  @Test
  public void shouldLoadUserAndRolesByUsername() {
    SUser user = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    user.setUsername("john@doe.fr");
    List<String> roles = new ArrayList<String>();
    roles.add(Roles.ROLE_USER.toString());

    given(this.userRepository.findByUsername("johndoe")).willReturn(user);
    given(this.userRoleRepository.findRoleByUserName("johndoe")).willReturn(roles);

    UserDetails details = this.customUserDetailsService.loadUserByUsername("johndoe");
    assertThat(details.getUsername()).isEqualTo("john@doe.fr");
  }

  @Test(expected = UsernameNotFoundException.class)
  public void shouldThrowExceptionWhenUserNotFound() {
    given(this.userRepository.findByUsername("foobar")).willReturn(null);

    this.customUserDetailsService.loadUserByUsername("foobar");
  }
}
