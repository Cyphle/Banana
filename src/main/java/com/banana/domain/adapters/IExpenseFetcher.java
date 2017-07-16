package com.banana.domain.adapters;

import com.banana.domain.models.Expense;

import java.util.List;

public interface IExpenseFetcher {
  List<Expense> getExpensesByBudgetid(long budgetId);
}
