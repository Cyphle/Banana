package com.banana.utilities;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Expense;
import com.banana.utils.Moment;

import java.util.ArrayList;
import java.util.List;

public class FakeExpenseFetcher implements IExpenseFetcher {
  public List<Expense> getExpensesByBudgetId(long budgetId) {
    Expense expenseOne = new Expense("Courses", 24, (new Moment("2017-07-10")).getDate());
    Expense expenseTwo = new Expense("Bar", 40, (new Moment("2017-08-01")).getDate());
    List<Expense> expenses = new ArrayList<>();
    expenses.add(expenseOne);
    expenses.add(expenseTwo);
    return expenses;
  }

  public Expense createExpense(long budgetId, Expense expense) {
    return new Expense(1, "Courses", 24, (new Moment("2017-07-18")).getDate());
  }

  public Expense updateBudgetExpense(long budgetId, Expense expense) {
    return expense;
  }

  public Expense updateAccountExpense(long accountId, Expense expense) {
    return expense;
  }
}
