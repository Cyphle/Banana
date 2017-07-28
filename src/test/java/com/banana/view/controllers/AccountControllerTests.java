package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.utils.Moment;
import com.banana.view.forms.AccountForm;
import com.banana.view.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BananaApplication.class})
@WebMvcTest(AccountController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountControllerTests {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private AccountService accountService;

  private User user;
  private List<Account> accounts;
  private Account account;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    this.user = new User(1, "John", "Doe", "john@doe.fr");
    this.accounts = new ArrayList<>();
    this.accounts.add(new Account(1, this.user, "Account one", "account-one", 1000.0, new Moment("2016-01-01").getDate()));
    this.accounts.add(new Account(2, this.user, "Account two", "account-two", 2000.0, new Moment("2016-01-01").getDate()));
    this.account = new Account(1, this.user, "My account", "my-account", 2000, new Moment("2017-01-01").getDate());
  }

  @Test
  public void should_get_accounts_page() throws Exception {
    given(this.accountService.getAccountsOfUser()).willReturn(this.accounts);

    this.mvc.perform(get("/accounts"))
            .andExpect(status().isOk())
            .andExpect(view().name("account/accounts"))
            .andExpect(MockMvcResultMatchers.model().attribute("accounts", this.accounts));
  }

  @Test
  public void should_get_creation_page_for_account() throws Exception {
    this.mvc.perform(get("/accounts/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("account/create-account"));
  }

  @Test
  public void should_create_a_new_account() throws Exception {
    given(this.accountService.createAccount(any(AccountForm.class))).willReturn(this.account);

    this.mvc.perform(post("/accounts/create")
            .param("name", this.account.getName())
            .param("initialAmount", new Double(this.account.getInitialAmount()).toString())
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }

  @Test
  public void should_get_update_account_page() throws Exception {
    given(this.accountService.getAccountBySlug(any(String.class))).willReturn(this.account);

    this.mvc.perform(get("/accounts/update/my-account"))
            .andExpect(status().isOk())
            .andExpect(view().name("account/update-account"));
  }

  @Test
  public void should_update_an_account() throws Exception {
    given(this.accountService.updateAccount(any(AccountForm.class))).willReturn(this.account);

    this.mvc.perform(post("/accounts/update")
            .param("name", this.account.getName())
            .param("initialAmount", new Double(this.account.getInitialAmount()).toString())
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }
}
