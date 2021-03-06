package com.banana.utilities;

import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;

import java.util.ArrayList;
import java.util.List;

public class FakeBudgetFetcher implements IBudgetFetcher {
  public List<Budget> getBudgetsOfUserAndAccount(User user, long accountId) {
    return new ArrayList<>();
  }

  public Budget getBudgetOfUserAndAccountById(User user, long accountId, long budgetId) {
    return null;
  }

  public Budget createBudget(long accountId, Budget budget) {
    budget.setId(1);
    return budget;
  }

  public Budget updateBudget(long accountId, Budget budget) {
    return budget;
  }

  public boolean deleteBudget(Budget budget) {
    return true;
  }
}
