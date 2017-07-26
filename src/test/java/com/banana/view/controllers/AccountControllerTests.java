package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.utils.Moment;
import com.banana.view.services.AccountService;
import com.banana.view.controllers.AccountController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
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

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    this.user = new User(1, "John", "Doe", "john@doe.fr");
    this.accounts = new ArrayList<>();
    this.accounts.add(new Account(1, this.user, "Account one", "account-one", 1000.0, new Moment("2016-01-01").getDate()));
    this.accounts.add(new Account(2, this.user, "Account two", "account-two", 2000.0, new Moment("2016-01-01").getDate()));
  }

  @Test
  public void should_get_page_that_displays_accounts() throws Exception {
    given(this.accountService.getAccountsOfUser()).willReturn(this.accounts);

    this.mvc.perform(get("/accounts"))
            .andExpect(status().isOk())
            .andExpect(view().name("account/accounts"))
            .andExpect(MockMvcResultMatchers.model().attribute("accounts", this.accounts));
  }
}
