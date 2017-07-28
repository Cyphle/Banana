package com.banana.domain.ports;

import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;

public interface BudgetPort {
  Budget getBudgetById(User user, long accountId, long budgetId);
  Budget createBudget(User user, long accountId, Budget budget);
  Budget updateBudget(User user, long accountId, Budget budget);
  Budget deleteBudget(User user, long accountId, Budget budget);
}
