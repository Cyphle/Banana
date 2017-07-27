package com.banana.view.controllers;

import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import com.banana.infrastructure.orm.repositories.SUserRoleRepository;
import com.banana.view.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.Filter;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class, WebSecurityConfig.class})
@WebMvcTest(MainController.class)
public class MainControllerTests {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private Filter springSecurityFilterChain;

  @MockBean
  private UserService userService;

  @MockBean
  private SUserRepository userRepository;

  @MockBean
  private SUserRoleRepository userRoleRepository;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .addFilters(springSecurityFilterChain)
            .build();
  }

  @Test
  public void should_get_homepage() throws Exception {
    this.mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"));
  }

  @Test
  @WithMockUser(username="john@doe.fr", roles={"USER","ADMIN"})
  public void should_get_homepage_with_authenticated_user() throws Exception {
    SUser user = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    user.setUsername(user.getEmail());
    given(this.userService.getAuthenticatedUser()).willReturn(user);

    this.mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(content().string(containsString("john@doe.fr")));
  }
}