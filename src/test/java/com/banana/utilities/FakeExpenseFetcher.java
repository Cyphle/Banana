package com.banana.utilities;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.utils.Moment;

import java.util.ArrayList;
import java.util.List;

public class FakeExpenseFetcher implements IExpenseFetcher {
  public List<Expense> getExpensesOfAccount(Account account) {
    return this.getExpenses();
  }

  public List<Expense> getExpensesOfBudget(Budget budget) {
    return this.getExpenses();
  }

  public Expense createAccountExpense(long accountId, Expense expense) {
    return new Expense(1, "Courses", 24, (new Moment("2017-07-18")).getDate());
  }

  public Expense createBudgetExpense(long budgetId, Expense expense) {
    return new Expense(1, "Courses", 24, (new Moment("2017-07-18")).getDate());
  }

  public Expense updateBudgetExpense(long budgetId, Expense expense) {
    return expense;
  }

  public Expense updateAccountExpense(long accountId, Expense expense) {
    return expense;
  }

  private List<Expense> getExpenses() {
    Expense expenseOne = new Expense("Courses", 24, (new Moment("2017-07-10")).getDate());
    Expense expenseTwo = new Expense("Bar", 40, (new Moment("2017-08-01")).getDate());
    List<Expense> expenses = new ArrayList<>();
    expenses.add(expenseOne);
    expenses.add(expenseTwo);
    return expenses;
  }

  public boolean deleteExpense(Expense expense) {
    return true;
  }
}
