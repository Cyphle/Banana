package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;
import com.banana.infrastructure.orm.models.*;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.utilities.TestUtil;
import com.banana.utils.Moment;
import com.banana.view.services.AccountService;
import com.banana.view.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BananaApplication.class, WebSecurityConfig.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class APIChargeControllerTests {
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private SUserRoleRepository userRoleRepository;

  @MockBean
  private UserDetailsService userDetailsService;

  @Autowired
  private SAccountRepository accountRepository;

  @Autowired
  private SChargeRepository chargeRepository;

  @Autowired
  private SUserRepository userRepository;

  @Autowired
  private AccountService accountService;

  @MockBean
  private UserService userService;

  private SUser fakeUser;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.userRepository.save(this.fakeUser);

    SAccount account = new SAccount(this.fakeUser, "My account", "my-account", 2000, new Moment("2013-01-01").getDate());
    this.accountRepository.save(account);

    SCharge charge = new SCharge("Loyer", 1200, new Moment("2013-01-01").getDate());
    charge.setAccount(account);
    this.chargeRepository.save(charge);

    given(this.userService.isAuthenticated()).willReturn(true);
    given(this.userService.getAuthenticatedUser()).willReturn(this.fakeUser);
  }

  @After
  public void unset() {
    this.chargeRepository.deleteAll();
    this.accountRepository.deleteAll();
    this.userRepository.deleteAll();
  }


  @Test
  public void should_delete_charge() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");
    SCharge sCharge = this.chargeRepository.findByUserUsernameAndAccountId(this.fakeUser.getUsername(), sAccount.getId()).get(0);

    this.mvc.perform(delete("/charges/" + sAccount.getId() + "/" + sCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status", is(200)));
  }
}
