package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.domain.models.*;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.orm.models.*;
import com.banana.utilities.TestUtil;
import com.banana.utils.Moment;
import com.banana.view.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
@WebMvcTest(APIAccountController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class APIAccountControllerTests {
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private AccountService accountService;

  private Account account;
  private SUser suser;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    this.suser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.account = new Account(UserPivot.fromInfrastructureToDomain(this.suser), "My account", 2000, new Moment("2013-01-01").getDate());
    this.account.setId(1);

    List<Budget> budgets = new ArrayList<>();
    budgets.add(new Budget("Manger", 300, new Moment("2017-01-01").getDate()));
    budgets.add(new Budget("Clopes", 200, new Moment("2017-02-01").getDate()));

    List<Expense> budgetExpenses = new ArrayList<>();
    budgetExpenses.add(new Expense("G20", 20, new Moment("2017-01-12").getDate()));
    budgetExpenses.add(new Expense("Monoprix", 30, new Moment("2017-02-13").getDate()));
    budgets.get(0).setExpenses(budgetExpenses);
    this.account.setBudgets(budgets);

    List<Charge> charges = new ArrayList<>();
    charges.add(new Charge("Loyer", 1200, new Moment("2016-01-01").getDate()));
    charges.add(new Charge("Internet", 40, new Moment("2017-01-01").getDate()));
    this.account.setCharges(charges);

    List<Expense> expenses = new ArrayList<>();
    expenses.add(new Expense("Bar", 50, new Moment("2017-07-20").getDate()));
    expenses.add(new Expense("Retrait", 60, new Moment("2017-07-23").getDate()));
    this.account.setExpenses(expenses);

    List<Credit> credits = new ArrayList<>();
    credits.add(new Credit("Salaire", 2400, new Moment("2017-06-30").getDate()));
    this.account.setCredits(credits);
  }

  @Test
  public void should_get_an_account_in_json_format() throws Exception {
    given(this.accountService.getAccountBySlug(any(String.class))).willReturn(this.account);

    this.mvc.perform(get("/accounts/my-account"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id", is(1)))
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
}
