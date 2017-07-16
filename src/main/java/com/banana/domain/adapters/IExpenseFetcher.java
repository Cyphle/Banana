package com.banana.domain.adapters;

import com.banana.domain.models.Expense;
import com.banana.domain.models.User;

import java.util.List;

public interface IExpenseFetcher {
  List<Expense> getExpensesOfUserAccountAndBudgetById(User user, long accountId, long budgetId);
}
