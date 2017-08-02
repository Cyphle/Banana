package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.utils.Moment;
import com.banana.view.forms.ExpenseForm;
import com.banana.view.services.BudgetService;
import com.banana.view.services.ExpenseService;
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
@WebMvcTest(ExpenseController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ExpenseControllerTests {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private ExpenseService expenseService;

  private User user;
  private Account account;
  private Expense expense;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    this.user = new User(1, "John", "Doe", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000, new Moment("2013-01-01").getDate());
    this.expense = new Expense(1, "Bar", 40, new Moment("2017-07-03").getDate());
  }

  @Test
  public void should_get_expense_creation_page() throws  Exception {
    this.mvc.perform(get("/expenses/create/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("expense/create-expense"));
  }

  @Test
  public void should_create_account_expense() throws Exception {
    given(this.expenseService.createExpense(any(ExpenseForm.class))).willReturn(this.account);

    this.mvc.perform(post("/expenses/create")
            .param("accountId", "1")
            .param("budgetId", "-1")
            .param("description", this.expense.getDescription())
            .param("amount", new Double(this.expense.getAmount()).toString())
            .param("expenseDate", "2017-03-01")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }

  @Test
  public void should_get_update_budget_page() throws Exception {
    given(this.expenseService.getExpense(any(long.class), any(long.class), any(long.class))).willReturn(this.expense);

    this.mvc.perform(get("/expenses/update/1/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("expense/update-expense"));
  }

  @Test
  public void should_update_account_expense() throws Exception {
    given(this.expenseService.updateExpense(any(ExpenseForm.class))).willReturn(this.account);

    this.mvc.perform(post("/expenses/update")
            .param("id", "1")
            .param("accountId", "1")
            .param("budgetId", "-1")
            .param("description", this.expense.getDescription())
            .param("amount", new Double(this.expense.getAmount()).toString())
            .param("expenseDate", "2017-03-03")
            .param("debitDate", "2017-03-06")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }
}
