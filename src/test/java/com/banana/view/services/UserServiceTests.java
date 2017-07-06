package com.banana.view.services;

import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.models.SUserRole;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import com.banana.infrastructure.orm.repositories.SUserRoleRepository;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.banana.infrastructure.user.CustomUserDetails;
import com.banana.infrastructure.user.Roles;
import com.banana.view.storage.StorageService;

@RunWith(SpringRunner.class)
public class UserServiceTests {
  @MockBean
  private StorageService storageService;

  @MockBean
  private SUserRepository userRepository;

  @MockBean
  private SUserRoleRepository userRoleRepository;

  @MockBean
  private SAccountRepository accountRepository;

  private UserService userService;
  private SUser user;
  private SUserRole role;
  private MockMultipartFile profilePicture;

  @Configuration
  @EnableGlobalMethodSecurity(prePostEnabled = true)
  @ComponentScan(basePackageClasses = UserService.class)
  static class Config {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService());
    }

    @Bean
    public CustomUserDetailsService userDetailsService() {
      return new CustomUserDetailsService();
    }
  }

  static class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
      SUser user = new SUser("Doe", "John", "john@doe.fr", "johndoe");
      user.setUsername("john@doe.fr");
      List<String> roles = new ArrayList<String>();
      roles.add(Roles.ROLE_USER.toString());
      return new CustomUserDetails(user, roles);
    }
  }

  @Before
  public void setUp() {
    this.userService = new UserService(this.storageService, this.userRepository, this.userRoleRepository);

    this.profilePicture = new MockMultipartFile("file", "test.jpg", "text/plain", "Spring Framework".getBytes());
    this.user = new SUser("Moi", "Cyril", "john@doe.fr", "test");
    this.user.setId(1);

    this.role = new SUserRole();
    this.role.setUserId(this.user.getId());
    this.role.setRole(Roles.ROLE_USER.toString());
  }

  @Test
  @WithUserDetails
  public void should_get_authenticated_user() {
    SUser authenticatedUser = this.userService.getAuthenticatedUser();
    assertThat(authenticatedUser.getUsername()).isEqualTo("john@doe.fr");
  }

  @Test
  public void should_not_get_null_when_not_authenticated() {
    assertThat(this.userService.getAuthenticatedUser()).isNull();
  }

  @Test
  @WithMockUser(username = "John")
  public void should_check_user_is_not_anonymous() {
    assertThat(this.userService.isAuthenticated()).isTrue();
  }

  @Test
  public void should_check_user_is_anonymous() {
    assertThat(this.userService.isAuthenticated()).isFalse();
  }

  @Test
  public void should_create_user() {
    // GIVEN
    given(this.storageService.store(this.profilePicture)).willReturn("profilepicture.jpg");
    given(this.userRepository.save(this.user)).willReturn(this.user);
    // WHEN
    SUser createdUser = this.userService.createUser(this.user, this.profilePicture);
    // THEN
    assertThat(createdUser).isNotNull();
    assertThat(createdUser.getUsername()).isEqualTo("john@doe.fr");
    assertThat(createdUser.getPicture()).isEqualTo("profilepicture.jpg");
    assertThat(createdUser.getSlug()).isEqualTo("cyril.moi");
    assertThat(createdUser.getPassword()).isNotEqualTo("test");
  }

  @Test
  public void should_create_user_role() {
    // GIVEN
    given(this.userRoleRepository.save(any(SUserRole.class))).willReturn(this.role);
    // WHEN
    SUserRole createdRole = this.userService.createUserRoleUser(this.user);
    // THEN
    assertThat(createdRole.getRole()).isEqualTo("ROLE_USER");
    assertThat(createdRole.getUserId()).isEqualTo(1);
  }

  @Test
  public void shouldCreateUserWithAUserRole() {
    // GIVEN
    given(this.userRepository.save(any(SUser.class))).willReturn(user);
    given(this.userRoleRepository.save(any(SUserRole.class))).willReturn(role);
    // WHEN
    boolean isCreated = this.userService.createUserWithUserRole(user, profilePicture);
    // THEN
    assertThat(isCreated).isTrue();
  }
}
