package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;
import com.banana.domain.models.Account;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SExpense;
import com.banana.infrastructure.orm.models.SUser;
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
public class BudgetControllerITests {
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
            .apply(springSecurity())
            .build();

    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.userRepository.save(this.fakeUser);

    SAccount account = new SAccount(this.fakeUser, "My account", "my-account", 2000, new Moment("2017-01-01").getDate());
    this.accountRepository.save(account);

    SBudget budget = new SBudget("Clopes", 200, new Moment("2017-01-01").getDate());
    budget.setAccount(account);
    this.budgetRepository.save(budget);

    SExpense expense = new SExpense("Monoprix", 40.0, new Moment("2017-06-20").getDate());
    expense.setBudget(budget);
    this.expenseRepository.save(expense);

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
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_create_budget() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");

    this.mvc.perform(post("/budgets/create")
            .param("accountId", new Long(sAccount.getId()).toString())
            .param("name", "Manger")
            .param("initialAmount", new Double(300.0).toString())
            .param("startDate", "2017-01-01")
            .param("endDate", "2018-06-01")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));

    Account account = this.accountService.getAccountBySlug("my-account");
    assertThat(account.getBudgets().size()).isEqualTo(2);
  }

  @Test
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_update_budget() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");
    SBudget sBudget = this.budgetRepository.findByUserUsernameAndAccountId(this.fakeUser.getUsername(), sAccount.getId()).get(0);

    this.mvc.perform(post("/budgets/update")
            .param("id", new Long(sBudget.getId()).toString())
            .param("accountId", new Long(sAccount.getId()).toString())
            .param("name", "Manger")
            .param("initialAmount", new Double(300.0).toString())
            .param("startDate", "2017-02-01")
            .param("endDate", "2018-06-01")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));

    Account account = this.accountService.getAccountBySlug("my-account");
    assertThat(account.getBudgets().size()).isEqualTo(2);
  }

  @Test
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_create_budget_expense() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");
    SBudget sBudget = this.budgetRepository.findByUserUsernameAndAccountId(this.fakeUser.getUsername(), sAccount.getId()).get(0);

    this.mvc.perform(post("/budgets/expenses/create")
            .param("accountId", new Long(sAccount.getId()).toString())
            .param("budgetId", new Long(sBudget.getId()).toString())
            .param("description", "G20")
            .param("amount", new Double(40.0).toString())
            .param("expenseDate", "2017-07-01")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));

    Account account = this.accountService.getAccountBySlug("my-account");
    assertThat(account.getBudgets().get(0).getExpenses().size()).isEqualTo(2);
  }

  @Test
  @WithMockUser(username = "john@doe.fr", roles = {"USER", "ADMIN"})
  public void should_update_budget_expense() throws Exception {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "my-account");
    SBudget sBudget = this.budgetRepository.findByUserUsernameAndAccountId(this.fakeUser.getUsername(), sAccount.getId()).get(0);
    SExpense sExpense = this.expenseRepository.findByBudgetId(sBudget.getId()).get(0);

    this.mvc.perform(post("/budgets/expenses/update")
            .param("id", new Long(sExpense.getId()).toString())
            .param("accountId", new Long(sAccount.getId()).toString())
            .param("budgetId", new Long(sBudget.getId()).toString())
            .param("description", "G20")
            .param("amount", new Double(100.0).toString())
            .param("expenseDate", "2017-07-01")
            .param("debitDate", "2017-07-05")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));

    Account account = this.accountService.getAccountBySlug("my-account");
    assertThat(account.getBudgets().get(0).getExpenses().size()).isEqualTo(1);
  }
}
