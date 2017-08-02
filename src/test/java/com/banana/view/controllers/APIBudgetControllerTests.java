package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;
import com.banana.infrastructure.orm.models.*;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.utilities.TestUtil;
import com.banana.utils.Moment;
import com.banana.view.services.AccountService;
import com.banana.view.services.BudgetService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BananaApplication.class, WebSecurityConfig.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class APIBudgetControllerTests {
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
    SExpense budgetExpenseTwo = new SExpense("Monoprix", 30, new Moment("2017-02-13").getDate());
    budgetExpenseTwo.setBudget(budgetOne);
    this.expenseRepository.save(budgetExpenseTwo);

    given(this.userService.isAuthenticated()).willReturn(true);
    given(this.userService.getAuthenticatedUser()).willReturn(this.fakeUser);
  }

  @After
  public void unset() {
    this.expenseRepository.deleteAll();
    this.budgetRepository.deleteAll();
    this.accountRepository.deleteAll();
    this.userRepository.deleteAll();
  }


  @Test
  public void should_delete_budget() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");
    SBudget sBudget = this.budgetRepository.findByUserUsernameAndAccountId(this.fakeUser.getUsername(), sAccount.getId()).get(0);

    this.mvc.perform(delete("/budgets/" + sAccount.getId() + "/" + sBudget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status", is(200)));
  }

  @Test
  public void should_delete_budget_expense() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");
    SBudget sBudget = this.budgetRepository.findByUserUsernameAndAccountId(this.fakeUser.getUsername(), sAccount.getId()).get(0);
    SExpense sExpense = this.expenseRepository.findByBudgetId(sBudget.getId()).get(0);

    this.mvc.perform(delete("/budgets/expenses/" + sAccount.getId() + "/" + sBudget.getId() + "/" + sExpense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status", is(200)));
  }
}
