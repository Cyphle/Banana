package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.domain.ports.ExpensePort;
import com.banana.utilities.FakeAccountFetcher;
import com.banana.utilities.FakeBudgetFetcher;
import com.banana.utilities.FakeExpenseFetcher;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;

public class ExpenseCalculatorTests {
  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private IExpenseFetcher expenseFetcher;
  private ExpensePort expensePort;
  private User user;
  private Account account;
  private Budget budgetOne;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000, new Moment("2016-01-01").getDate());
    this.budgetOne = new Budget(2, "Budget one", 200, (new Moment()).getFirstDateOfMonth().getDate());

    this.accountFetcher = new FakeAccountFetcher();
    this.accountFetcher = Mockito.spy(this.accountFetcher);
    this.budgetFetcher = new FakeBudgetFetcher();
    this.budgetFetcher = Mockito.spy(this.budgetFetcher);
    this.expenseFetcher = new FakeExpenseFetcher();
    this.expenseFetcher = Mockito.spy(this.expenseFetcher);
    this.expensePort = new ExpenseCalculator(this.accountFetcher, this.budgetFetcher, this.expenseFetcher);
  }

  @Test
  public void should_throw_error_if_budget_for_expense_does_not_exists() {
    Expense newExpense = new Expense("My expense", 20, (new Moment()).getDate());
    Mockito.doReturn(null).when(this.budgetFetcher).getBudgetOfUserAndAccountById(any(User.class), any(long.class), any(long.class));

    try {
      this.expensePort.createExpense(this.user, 1, 1, newExpense);
      fail("Should throw error when budget does not exists");
    } catch (NoElementFoundException e) {
      assertThat(e.getMessage()).contains("No budget found with id");
    }
  }

  @Test
  public void should_throw_error_if_adding_expense_overflow_budget_amount() {
    Expense newExpense = new Expense("My expense", 300, (new Moment("2017-07-17")).getDate());
    Mockito.doReturn(this.budgetOne).when(this.budgetFetcher).getBudgetOfUserAndAccountById(any(User.class), any(long.class), any(long.class));

    try {
      this.expensePort.createExpense(this.user, 1, 1, newExpense);
      fail("Should throw error if total expenses are higher than budget amount");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("Budget amount has been exceeded. Total amount would be : 324");
    }
  }

  @Test
  public void should_create_a_new_budget_expense() {
    Expense newExpense = new Expense("My expense", 66, (new Moment("2017-07-17")).getDate());
    Mockito.doReturn(this.budgetOne).when(this.budgetFetcher).getBudgetOfUserAndAccountById(any(User.class), any(long.class), any(long.class));

    Expense createdExpense = this.expensePort.createExpense(this.user, 1, 1, newExpense);

    assertThat(createdExpense.getId()).isGreaterThan(0);
  }

  @Test
  public void should_throw_error_if_budget_expense_but_no_budget_found() {
    Mockito.doReturn(null).when(this.budgetFetcher).getBudgetOfUserAndAccountById(any(User.class), any(long.class), any(long.class));

    Expense expenseToUpdate = new Expense(1, "Bar", 40, (new Moment("2017-07-18")).getDate());

    try {
      this.expensePort.updateExpense(this.user, 1, 1, expenseToUpdate);
      fail("Should throw an error if there is no budget for the expense update");
    } catch (NoElementFoundException e) {
      assertThat(e.getMessage()).contains("No budget found");
    }
  }

  @Test
  public void should_throw_error_if_budget_expense_that_exceed_budget_amount() {
    Mockito.doReturn(this.budgetOne).when(this.budgetFetcher).getBudgetOfUserAndAccountById(any(User.class), any(long.class), any(long.class));

    Expense expenseToUpdate = new Expense(1, "Bar", 300, (new Moment("2017-07-18")).getDate());

    try {
      this.expensePort.updateExpense(this.user, 1, 1, expenseToUpdate);
      fail("Should throw error if total expenses are higher than budget amount");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("Budget amount has been exceeded. Total amount would be : 324");
    }
  }

  @Test
  public void should_update_a_budget_expense() {
    Expense newExpense = new Expense(1, "My expense", 66, (new Moment("2017-07-17")).getDate());
    Mockito.doReturn(this.budgetOne).when(this.budgetFetcher).getBudgetOfUserAndAccountById(any(User.class), any(long.class), any(long.class));

    Expense createdExpense = this.expensePort.updateExpense(this.user, 1, 1, newExpense);

    assertThat(createdExpense.getId()).isGreaterThan(0);
  }

  @Test
  public void should_throw_error_if_account_expense_but_not_account_found() {
    Mockito.doReturn(null).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));

    Expense expenseToUpdate = new Expense(1, "Bar", 40, (new Moment("2017-07-18")).getDate());

    try {
      this.expensePort.updateExpense(this.user, 1, -1, expenseToUpdate);
      fail("Should throw an error if there is no account for the expense update");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("No account for user and id");
    }
  }

  @Test
  public void should_update_an_account_expense() {
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));

    Expense newExpense = new Expense(1, "My expense", 66, (new Moment("2017-07-17")).getDate());

    Expense createdExpense = this.expensePort.updateExpense(this.user, 1, -1, newExpense);

    assertThat(createdExpense.getId()).isGreaterThan(0);
  }

  @Test
  public void should_delete_an_account_expense() {
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));

    Expense newExpense = new Expense(1, "My expense", 66, (new Moment("2017-07-17")).getDate());

    boolean isDeleted = this.expensePort.deleteExpense(this.user, 1, -1, newExpense);

    assertThat(isDeleted).isTrue();
  }

  @Test
  public void should_delete_an_budget_expense() {
    Mockito.doReturn(this.budgetOne).when(this.budgetFetcher).getBudgetOfUserAndAccountById(any(User.class), any(long.class), any(long.class));

    Expense newExpense = new Expense(1, "My expense", 66, (new Moment("2017-07-17")).getDate());

    boolean isDeleted = this.expensePort.deleteExpense(this.user, 1, 1, newExpense);

    assertThat(isDeleted).isTrue();
  }
}
