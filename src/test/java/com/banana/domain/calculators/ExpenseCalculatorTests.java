package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.NoElementFoundException;
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
  private Budget budgetOne;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
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
  public void should_throw_error_if_budget_expense_but_no_budget_found() {
    Mockito.doReturn(null).when(this.budgetFetcher).getBudgetOfUserAndAccountById(any(User.class), any(long.class), any(long.class));

    Expense expenseToUpdate = new Expense(1, "Bar", 40, (new Moment("2017-07-18")).getDate());

    try {
      Expense updatedExpense = this.expensePort.updateExpense(this.user, 1, 1, expenseToUpdate);
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
      Expense updatedExpense = this.expensePort.updateExpense(this.user, 1, 1, expenseToUpdate);
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
      Expense updatedExpense = this.expensePort.updateExpense(this.user, 1, -1, expenseToUpdate);
      fail("Should throw an error if there is no account for the expense update");
    } catch (NoElementFoundException e) {
      assertThat(e.getMessage()).contains("No account found");
    }
  }

  @Test
  public void should_update_an_account_expense() {
    Expense newExpense = new Expense(1, "My expense", 66, (new Moment("2017-07-17")).getDate());

    Expense createdExpense = this.expensePort.updateExpense(this.user, 1, -1, newExpense);

    assertThat(createdExpense.getId()).isGreaterThan(0);
  }
}
