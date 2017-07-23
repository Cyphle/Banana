package com.banana.domain.adapters;

import com.banana.domain.models.Expense;

import java.util.List;

public interface IExpenseFetcher {
  List<Expense> getExpensesOfAccount(long accountId);
  List<Expense> getExpensesOfBudget(long budgetId);
  Expense createExpense(long budgetId, Expense expense);
  Expense updateBudgetExpense(long budgetId, Expense expense);
  Expense updateAccountExpense(long accountId, Expense expense);
}
