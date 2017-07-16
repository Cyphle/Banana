package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.IExpenseRepository;

import java.util.List;

public class ExpenseFetcher implements IExpenseFetcher {
  private IExpenseRepository expenseRepository;

  public ExpenseFetcher(IExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  public List<Expense> getExpensesOfUserAccountAndBudgetById(User user, long accountId, long budgetId) {
    return null;
  }
}
