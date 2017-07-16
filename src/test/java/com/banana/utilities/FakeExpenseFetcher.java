package com.banana.utilities;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;

import java.util.ArrayList;
import java.util.List;

public class FakeExpenseFetcher implements IExpenseFetcher {
  public List<Expense> getExpensesOfUserAccountAndBudgetById(User user, long accountId, long budgetId) {
    return new ArrayList<Expense>();
  }
}
