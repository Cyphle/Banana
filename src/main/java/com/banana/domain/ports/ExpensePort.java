package com.banana.domain.ports;

import com.banana.domain.models.Expense;
import com.banana.domain.models.User;

public interface ExpensePort {
  Expense getExpenseById(User user, long accountId, long budgetId, long expenseId);
  Expense createExpense(User user, long accountId, long budgetId, Expense expense);
  Expense updateExpense(User user, long accountId, long budgetId, Expense expense);
  boolean deleteExpense(User user, long accountId, long budgetId, Expense expense);
}
