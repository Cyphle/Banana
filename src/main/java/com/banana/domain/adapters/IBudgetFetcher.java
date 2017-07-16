package com.banana.domain.adapters;

import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;

import java.util.List;

public interface IBudgetFetcher {
  List<Budget> getBudgetsOfUserAndAccount(User user, long accountId);
  Budget createBudget(Account account, Budget budget);
  Budget updateBudget(Account account, Budget budget);
}
