package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.UpdateException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;
import com.banana.domain.ports.BudgetPort;
import com.banana.utilities.FakeAccountFetcher;
import com.banana.utilities.FakeBudgetFetcher;
import com.banana.utilities.FakeExpenseFetcher;
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
  private IExpenseFetcher expenseFetcher;
  private BudgetPort budgetPort;
  private List<Budget> budgets;
  private Budget budgetOne;
  private Budget budgetTwo;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.account = new Account(user, "Account", 1000);

    this.budgetOne = new Budget(2, "Budget one", 200, (new Moment("2017-01-01")).getFirstDateOfMonth().getDate());
    this.budgetTwo = new Budget(3, "Budget two", 300, (new Moment("2017-02-01")).getFirstDateOfMonth().getDate());
    this.budgets = new ArrayList<>();
    this.budgets.add(this.budgetOne);
    this.budgets.add(this.budgetTwo);

    this.accountFetcher = new FakeAccountFetcher();
    this.accountFetcher = Mockito.spy(this.accountFetcher);
    this.budgetFetcher = new FakeBudgetFetcher();
    this.budgetFetcher = Mockito.spy(this.budgetFetcher);
    this.expenseFetcher = new FakeExpenseFetcher();
    this.expenseFetcher = Mockito.spy(this.expenseFetcher);
    this.budgetPort = new BudgetCalculator(this.accountFetcher, this.budgetFetcher, this.expenseFetcher);
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));
  }

  @Test
  public void should_create_a_new_budget() {
    Moment today = new Moment();
    Budget newBudget = new Budget("My budget", 200, today.getFirstDateOfMonth().getDate());

    Budget createdBudget = this.budgetPort.createBudget(this.user, 1, newBudget);

    assertThat(createdBudget.getId()).isGreaterThan(0);
  }

  @Test
  public void should_throw_exception_if_budget_initial_amount_is_negative_when_creation() {
    Moment today = new Moment();

    Budget newBudget = new Budget("My budget", -200, today.getFirstDateOfMonth().getDate());

    try {
      Budget createdBudget = this.budgetPort.createBudget(this.user, 1, newBudget);
      fail("Should throw creation exception with negative amoun");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("Budget initial amount cannot be negative");
    }
  }

  @Test
  public void should_throw_exception_if_there_is_no_budget_with_this_id_for_user() {
    Mockito.doReturn(this.budgets).when(this.budgetFetcher).getBudgetsOfUserAndAccount(any(User.class), any(long.class));
    Budget budgetToUpdate = new Budget(1,"My budget", 200, (new Moment()).getFirstDateOfMonth().getDate());

    try {
      Budget updatedBudget = this.budgetPort.updateBudget(this.user, 1, budgetToUpdate);
      fail("Should throw no element found exception if there is no budget with this id for the given account and user");
    } catch (UpdateException e) {
      assertThat(e.getMessage()).contains("No budget found with id");
    }
  }

  @Test
  public void should_update_budget_name() {
    Budget budgetToUpdate = new Budget(2,"Budget updated", 200, (new Moment("2017-01-01")).getDate());

    Mockito.doReturn(this.budgets).when(this.budgetFetcher).getBudgetsOfUserAndAccount(any(User.class), any(long.class));

    Budget updatedBudget = this.budgetPort.updateBudget(this.user, 1, budgetToUpdate);

    Moment startDate = new Moment(updatedBudget.getStartDate());

    assertThat(updatedBudget.getId()).isEqualTo(budgetToUpdate.getId());
    assertThat(updatedBudget.getName()).isEqualTo("Budget updated");
    assertThat(updatedBudget.getInitialAmount()).isEqualTo(200);
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(1);
    assertThat(startDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_update_budget_amount() {
    Budget budgetToUpdate = new Budget(2,"Budget updated", 100, (new Moment("2017-10-01")).getDate());

    Mockito.doReturn(this.budgets).when(this.budgetFetcher).getBudgetsOfUserAndAccount(any(User.class), any(long.class));

    Budget updatedBudget = this.budgetPort.updateBudget(this.user, 1, budgetToUpdate);

    Moment startDate = new Moment(updatedBudget.getStartDate());

    assertThat(updatedBudget.getId()).isNotEqualTo(budgetToUpdate.getId());
    assertThat(updatedBudget.getName()).isEqualTo("Budget updated");
    assertThat(updatedBudget.getInitialAmount()).isEqualTo(100);
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(10);
    assertThat(startDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_update_budget_start_date() {
    Budget budgetToUpdate = new Budget(2,"Budget one", 200, (new Moment("2017-10-01")).getDate());
    budgetToUpdate.setStartDate((new Moment("2017-12-31")).getDate());

    Mockito.doReturn(this.budgets).when(this.budgetFetcher).getBudgetsOfUserAndAccount(any(User.class), any(long.class));

    Budget updatedBudget = this.budgetPort.updateBudget(this.user, 1, budgetToUpdate);
    Moment startDate = new Moment(updatedBudget.getStartDate());

    assertThat(updatedBudget.getId()).isEqualTo(budgetToUpdate.getId());
    assertThat(updatedBudget.getName()).isEqualTo("Budget one");
    assertThat(updatedBudget.getInitialAmount()).isEqualTo(200);
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(12);
    assertThat(startDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_update_budget_end_date() {
    Budget budgetToUpdate = new Budget(2,"Budget one", 200, (new Moment("2017-01-01")).getDate());
    budgetToUpdate.setEndDate((new Moment("2017-12-20")).getDate());

    Mockito.doReturn(this.budgets).when(this.budgetFetcher).getBudgetsOfUserAndAccount(any(User.class), any(long.class));

    Budget updatedBudget = this.budgetPort.updateBudget(this.user, 1, budgetToUpdate);
    Moment endDate = new Moment(updatedBudget.getEndDate());

    assertThat(updatedBudget.getId()).isEqualTo(budgetToUpdate.getId());
    assertThat(updatedBudget.getName()).isEqualTo("Budget one");
    assertThat(updatedBudget.getInitialAmount()).isEqualTo(200);
    assertThat(endDate.getDayOfMonth()).isEqualTo(31);
    assertThat(endDate.getMonthNumber()).isEqualTo(12);
    assertThat(endDate.getYear()).isEqualTo(2017);
  }

  /*
  ADD EXpense should throw error if adding expense is over budget amount
   */
}
