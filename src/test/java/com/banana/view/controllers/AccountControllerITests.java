package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;
import com.banana.domain.models.Account;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import com.banana.infrastructure.orm.repositories.SUserRoleRepository;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.containsString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BananaApplication.class, WebSecurityConfig.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountControllerITests {
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private SUserRoleRepository userRoleRepository;

  @MockBean
  private UserDetailsService userDetailsService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private SAccountRepository accountRepository;

  @Autowired
  private SUserRepository userRepository;

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

    SAccount accountOne = new SAccount(this.fakeUser, "Account one", "account-one", 2000, new Moment("2017-01-01").getDate());
    this.accountRepository.save(accountOne);
    SAccount accountTwo = new SAccount(this.fakeUser, "Account two", "account-two", 3000, new Moment("2017-01-01").getDate());
    this.accountRepository.save(accountTwo);

    given(this.userService.isAuthenticated()).willReturn(true);
    given(this.userService.getAuthenticatedUser()).willReturn(this.fakeUser);
  }

  @After
  public void unset() {
    this.accountRepository.deleteAll();
    this.userRepository.deleteAll();
  }

  @Test
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_get_accounts_page() throws Exception {
    this.mvc.perform(get("/accounts"))
            .andExpect(status().isOk())
            .andExpect(view().name("account/accounts"))
            .andExpect(content().string(containsString("account-one")))
            .andExpect(content().string(containsString("account-two")));
  }

  @Test
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_post_new_account() throws Exception {
    this.mvc.perform(post("/accounts/create")
            .param("name", "Fake Account")
            .param("initialAmount", new Double(2000.0f).toString())
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/fake-account"));

    List<Account> accounts = this.accountService.getAccountsOfUser();
    assertThat(accounts.size()).isEqualTo(3);
  }

  @Test
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_post_update_account() throws Exception {
    SAccount myAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "account-one");

    this.mvc.perform(post("/accounts/update")
            .param("id", new Long(myAccount.getId()).toString())
            .param("name", "Account update")
            .param("initialAmount", new Double(3000.0).toString())
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/account-update"));

    Account updatedAccount = this.accountService.getAccountsOfUser().stream().filter(account -> account.getName() == "Account update").collect(Collectors.toList()).get(0);
    assertThat(updatedAccount.getSlug()).isEqualTo("account-update");
    assertThat(updatedAccount.getInitialAmount()).isEqualTo(3000);
  }
}
