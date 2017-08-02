package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;
import com.banana.domain.models.Account;
import com.banana.infrastructure.orm.models.*;
import com.banana.infrastructure.orm.repositories.*;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BananaApplication.class, WebSecurityConfig.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ChargeControllerITests {
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
            .apply(springSecurity())
            .build();

    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.userRepository.save(this.fakeUser);

    SAccount account = new SAccount(this.fakeUser, "My account", "my-account", 2000, new Moment("2017-01-01").getDate());
    this.accountRepository.save(account);

    SCharge charge = new SCharge("Internet", 40, new Moment("2017-01-01").getDate());
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
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_create_charge() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");

    this.mvc.perform(post("/charges/create")
            .param("accountId", new Long(sAccount.getId()).toString())
            .param("description", "Loyer")
            .param("amount", new Double(1200.0).toString())
            .param("startDate", "2013-01-01")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));

    Account account = this.accountService.getAccountBySlug("my-account");
    assertThat(account.getCharges().size()).isEqualTo(2);
  }

  @Test
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_update_charge() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");
    SCharge sCharge = this.chargeRepository.findByUserUsernameAndAccountId(this.fakeUser.getUsername(), sAccount.getId()).get(0);

    this.mvc.perform(post("/charges/update")
            .param("id", new Long(sCharge.getId()).toString())
            .param("accountId", new Long(sAccount.getId()).toString())
            .param("description", "Manger")
            .param("amount", new Double(300.0).toString())
            .param("startDate", "2017-02-01")
            .param("endDate", "2018-06-01")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));

    Account account = this.accountService.getAccountBySlug("my-account");
    assertThat(account.getCharges().size()).isEqualTo(2);
  }
}
