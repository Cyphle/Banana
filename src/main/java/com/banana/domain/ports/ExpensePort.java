package com.banana.domain.ports;

import com.banana.domain.models.Expense;
import com.banana.domain.models.User;

public interface ExpensePort {
  Expense createExpense(User user, long accountId, long budgetId, Expense expense);
  Expense updateExpense(User user, long accountId, long budgetId, Expense expense);
}
