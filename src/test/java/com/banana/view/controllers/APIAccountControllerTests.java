package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;
import com.banana.domain.models.*;
import com.banana.infrastructure.connector.pivots.UserPivot;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BananaApplication.class, WebSecurityConfig.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class APIAccountControllerTests {
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
  private SBudgetRepository budgetRepository;

  @Autowired
  private SExpenseRepository expenseRepository;

  @Autowired
  private SChargeRepository chargeRepository;

  @Autowired
  private SCreditRepository creditRepository;

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

    SBudget budgetOne = new SBudget("Manger", 300, new Moment("2017-01-01").getDate());
    budgetOne.setAccount(account);
    this.budgetRepository.save(budgetOne);
    SBudget budgetTwo = new SBudget("Clopes", 200, new Moment("2017-02-01").getDate());
    budgetTwo.setAccount(account);
    this.budgetRepository.save(budgetTwo);

    SExpense budgetExpenseOne = new SExpense("G20", 20, new Moment("2017-01-12").getDate());
    budgetExpenseOne.setBudget(budgetOne);
    this.expenseRepository.save(budgetExpenseOne);
    SExpense budgetExenseTwo = new SExpense("Monoprix", 30, new Moment("2017-02-13").getDate());
    budgetExenseTwo.setBudget(budgetOne);
    this.expenseRepository.save(budgetExenseTwo);

    SCharge chargeOne = new SCharge("Loyer", 1200, new Moment("2016-01-01").getDate());
    chargeOne.setAccount(account);
    this.chargeRepository.save(chargeOne);
    SCharge chargeTwo = new SCharge("Internet", 40, new Moment("2017-01-01").getDate());
    chargeTwo.setAccount(account);
    this.chargeRepository.save(chargeTwo);

    SExpense expenseOne = new SExpense("Bar", 50, new Moment("2017-07-20").getDate());
    expenseOne.setAccount(account);
    this.expenseRepository.save(expenseOne);
    SExpense expenseTwo = new SExpense("Retrait", 60, new Moment("2017-07-23").getDate());
    expenseTwo.setAccount(account);
    this.expenseRepository.save(expenseTwo);

    SCredit creditOne = new SCredit("Salaire", 2400, new Moment("2017-06-30").getDate());
    creditOne.setAccount(account);
    this.creditRepository.save(creditOne);

    given(this.userService.isAuthenticated()).willReturn(true);
    given(this.userService.getAuthenticatedUser()).willReturn(this.fakeUser);
  }

  @After
  public void unset() {
    this.creditRepository.deleteAll();
    this.chargeRepository.deleteAll();
    this.expenseRepository.deleteAll();
    this.budgetRepository.deleteAll();
    this.accountRepository.deleteAll();
    this.userRepository.deleteAll();
  }

  @Test
  public void should_get_an_account_in_json_format() throws Exception {
    this.mvc.perform(get("/api/accounts/my-account").with(user("john@doe.fr")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.user.username", is("john@doe.fr")))
            .andExpect(jsonPath("$.name", is("My account")))
            .andExpect(jsonPath("$.initialAmount", is(2000.0)))
            .andExpect(jsonPath("$.budgets", hasSize(2)))
            .andExpect(jsonPath("$.budgets[0].name", is("Manger")))
            .andExpect(jsonPath("$.budgets[0].initialAmount", is(300.0)))
            .andExpect(jsonPath("$.budgets[0].expenses", hasSize(2)))
            .andExpect(jsonPath("$.budgets[0].expenses[0].description", is("G20")))
            .andExpect(jsonPath("$.budgets[0].expenses[0].amount", is(20.0)))
            .andExpect(jsonPath("$.budgets[0].expenses[1].description", is("Monoprix")))
            .andExpect(jsonPath("$.budgets[0].expenses[1].amount", is(30.0)))
            .andExpect(jsonPath("$.budgets[1].name", is("Clopes")))
            .andExpect(jsonPath("$.budgets[1].initialAmount", is(200.0)))
            .andExpect(jsonPath("$.budgets[1].expenses", hasSize(0)))
            .andExpect(jsonPath("$.charges", hasSize(2)))
            .andExpect(jsonPath("$.charges[0].description", is("Loyer")))
            .andExpect(jsonPath("$.charges[0].amount", is(1200.0)))
            .andExpect(jsonPath("$.charges[1].description", is("Internet")))
            .andExpect(jsonPath("$.charges[1].amount", is(40.0)))
            .andExpect(jsonPath("$.expenses", hasSize(2)))
            .andExpect(jsonPath("$.expenses[0].description", is("Bar")))
            .andExpect(jsonPath("$.expenses[0].amount", is(50.0)))
            .andExpect(jsonPath("$.expenses[1].description", is("Retrait")))
            .andExpect(jsonPath("$.expenses[1].amount", is(60.0)))
            .andExpect(jsonPath("$.credits", hasSize(1)))
            .andExpect(jsonPath("$.credits[0].description", is("Salaire")))
            .andExpect(jsonPath("$.credits[0].amount", is(2400.0)));
  }

  @Test
  public void should_delete_an_account() throws Exception {
    SAccount myAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");

    this.mvc.perform(delete("/api/accounts/" + myAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status", is(200)));
  }
}
