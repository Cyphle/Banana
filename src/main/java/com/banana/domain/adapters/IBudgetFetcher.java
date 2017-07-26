package com.banana.domain.adapters;

import com.banana.domain.models.Budget;
import com.banana.domain.models.User;

import java.util.List;

public interface IBudgetFetcher {
  List<Budget> getBudgetsOfUserAndAccount(User user, long accountId);
  Budget getBudgetOfUserAndAccountById(User user, long accountId, long budgetId);
  Budget createBudget(long accountId, Budget budget);
  Budget updateBudget(long accountId, Budget budget);
}
