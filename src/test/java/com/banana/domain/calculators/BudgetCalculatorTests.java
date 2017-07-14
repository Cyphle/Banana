package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;
import com.banana.domain.ports.BudgetPort;
import com.banana.utilities.FakeAccountFetcher;
import com.banana.utilities.FakeBudgetFetcher;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;

public class BudgetCalculatorTests {
  private User user;
  private Account account;

  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private BudgetPort budgetPort;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.account = new Account(user, "Account", 1000);

    this.accountFetcher = new FakeAccountFetcher();
    this.accountFetcher = Mockito.spy(this.accountFetcher);
    this.budgetFetcher = new FakeBudgetFetcher();
    this.budgetFetcher = Mockito.spy(this.budgetFetcher);
    this.budgetPort = new BudgetCalculator(this.accountFetcher, this.budgetFetcher);
  }

  @Test
  public void should_create_a_new_budget() {
    Moment today = new Moment();
    Budget newBudget = new Budget("My budget", 200, today.getFirstDateOfMonth().getDate());

    Budget createdBudget = this.budgetPort.createBudget(this.account, newBudget);

    assertThat(createdBudget.getId()).isGreaterThan(0);
  }

  @Test
  public void should_throw_exception_if_budget_name_already_exists() {
    Moment today = new Moment();
    Budget newBudget = new Budget("My budget", 200, today.getFirstDateOfMonth().getDate());
    List<Budget> budgets = new ArrayList<>();
    budgets.add(new Budget("My budget", 300, today.getFirstDateOfMonth().getDate()));
    Mockito.doReturn(budgets).when(this.budgetFetcher).getBudgetsOfUserAndAccount(any(User.class), any(long.class));

    try {
      Budget createdBudget = this.budgetPort.createBudget(this.account, newBudget);
      fail("Should throw creation exception with negative amoun");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("Cannot create multiple budgets with the same name");
    }
  }

  @Test
  public void should_throw_exception_if_budget_initial_amount_is_negative_when_creation() {
    Moment today = new Moment();

    Budget newBudget = new Budget("My budget", -200, today.getFirstDateOfMonth().getDate());

    try {
      Budget createdBudget = this.budgetPort.createBudget(this.account, newBudget);
      fail("Should throw creation exception with negative amoun");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("Budget initial amount cannot be negative");
    }
  }
}
