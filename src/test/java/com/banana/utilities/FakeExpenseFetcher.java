package com.banana.utilities;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Expense;
import com.banana.utils.Moment;

import java.util.ArrayList;
import java.util.List;

public class FakeExpenseFetcher implements IExpenseFetcher {
  public List<Expense> getExpensesByBudgetid(long budgetId) {
    Expense expenseOne = new Expense("Courses", 24, (new Moment("2017-07-10")).getDate());
    Expense expenseTwo = new Expense("Bar", 40, (new Moment("2017-08-01")).getDate());
    List<Expense> expenses = new ArrayList<>();
    expenses.add(expenseOne);
    expenses.add(expenseTwo);
    return expenses;
  }
}
