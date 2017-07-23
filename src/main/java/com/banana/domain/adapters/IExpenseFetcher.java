package com.banana.domain.adapters;

import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;

import java.util.List;

public interface IExpenseFetcher {
  List<Expense> getExpensesOfAccount(Account account);
  List<Expense> getExpensesOfBudget(Budget budget);
  Expense createBudgetExpense(long budgetId, Expense expense);
  Expense createAccountExpense(long accountId, Expense expense);
  Expense updateBudgetExpense(long budgetId, Expense expense);
  Expense updateAccountExpense(long accountId, Expense expense);
}
