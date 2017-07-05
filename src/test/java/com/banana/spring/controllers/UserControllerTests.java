package com.banana.spring.controllers;

import com.banana.spring.models.SUser;
import com.banana.spring.repositories.SUserRepository;
import com.banana.spring.repositories.SUserRoleRepository;
import com.banana.spring.user.Roles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;
import com.banana.spring.user.CustomUserDetails;

import javax.servlet.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { BananaApplication.class, WebSecurityConfig.class })
@WebMvcTest(UserController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTests {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private Filter springSecurityFilterChain;

  @MockBean
  private SUserRepository userRepository;

  @MockBean
  private SUserRoleRepository userRoleRepository;

  @MockBean
  private UserDetailsService userDetailsService;

  private List<String> userRoles;
  private SUser fakeUser;
  private MockMultipartFile profilePicture;

  @Before
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();

    this.userRoles = new ArrayList<String>();
    this.userRoles.add(Roles.ROLE_USER.toString());
    this.fakeUser = new SUser("Moi", "Cyril", "john@doe.fr", "$2a$10$S4m5xx76BSzKTEqFUBdVGefJ4wUkSWa2F5WOMAf3Wl2OwfFASWtCW");
    this.profilePicture = new MockMultipartFile("file", "test.jpg", "text/plain", "Spring Framework".getBytes());
  }

  @Test
  public void should_get_login_page() throws Exception {
    this.mvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("user/login"))
            .andExpect(content().string(containsString("form")));
  }

  @Test
  public void should_login_user() throws Exception {
    // GIVEN
    given(this.userDetailsService.loadUserByUsername("cyril@moi.fr")).willReturn(new CustomUserDetails(this.fakeUser, this.userRoles));
    // WHEN THEN
    this.mvc.perform(formLogin("/login").user("cyril@moi.fr").password("test"))
            .andExpect(authenticated());
  }

  @Test
  public void should_get_register_page() throws Exception {
    this.mvc.perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(view().name("user/registration"))
            .andExpect(content().string(containsString("form")));
  }
}
