package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;
import com.banana.utils.Moment;
import com.banana.view.forms.BudgetForm;
import com.banana.view.services.AccountService;
import com.banana.view.services.BudgetService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
@WebMvcTest(BudgetController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class BudgetControllerTests {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private BudgetService budgetService;

  private User user;
  private Budget budget;
  private Account account;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    this.user = new User(1, "John", "Doe", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000, new Moment("2013-01-01").getDate());
    this.budget = new Budget(1, "Manger", 300, new Moment("2017-01-01").getDate());
  }

  @Test
  public void should_get_budget_creation_page() throws  Exception {
    this.mvc.perform(get("/budgets/create/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("budget/create-budget"));
  }

  @Test
  public void should_create_budget() throws Exception {
    given(this.budgetService.createBudget(any(BudgetForm.class))).willReturn(this.account);

    this.mvc.perform(post("/budgets/create")
            .param("name", this.budget.getName())
            .param("initialAmount", new Double(this.budget.getInitialAmount()).toString())
            .param("startDate", "01/01/2017")
            .param("endDate", "01/06/2018")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }

  @Test
  public void should_get_update_budget_page() throws Exception {
    given(this.budgetService.getBudget(any(long.class), any(long.class))).willReturn(this.budget);

    this.mvc.perform(get("/budgets/update/1/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("budget/update-budget"));
  }

  @Test
  public void should_update_budget() throws Exception {
    given(this.budgetService.updateBudget(any(BudgetForm.class))).willReturn(this.account);

    this.mvc.perform(post("/budgets/update")
            .param("id", "1")
            .param("accountId", "1")
            .param("name", this.budget.getName())
            .param("initialAmount", new Double(this.budget.getInitialAmount()).toString())
            .param("startDate", "01/01/2017")
            .param("endDate", "01/06/2018")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }

  @Test
  public void should_create_budget_expense() throws Exception {
    // TODO
  }

  @Test
  public void should_update_budget_expense() throws Exception {
    // TODO
  }
}
