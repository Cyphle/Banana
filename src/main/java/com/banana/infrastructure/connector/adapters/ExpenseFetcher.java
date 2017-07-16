package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Expense;
import com.banana.infrastructure.connector.pivots.ExpensePivot;
import com.banana.infrastructure.connector.repositories.IExpenseRepository;
import com.banana.infrastructure.orm.models.SExpense;

import java.util.List;

public class ExpenseFetcher implements IExpenseFetcher {
  private IExpenseRepository expenseRepository;

  public ExpenseFetcher(IExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  public List<Expense> getExpensesByBudgetid(long budgetId) {
    List<SExpense> sExpenses = this.expenseRepository.getExpensesByBudgetid(budgetId);
    return ExpensePivot.fromInfrastructureToDomain(sExpenses);
  }
}
