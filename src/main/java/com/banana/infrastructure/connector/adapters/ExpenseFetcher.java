package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Expense;
import com.banana.infrastructure.connector.pivots.ExpensePivot;
import com.banana.infrastructure.connector.repositories.IBudgetRepository;
import com.banana.infrastructure.connector.repositories.IExpenseRepository;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SExpense;

import java.util.List;

public class ExpenseFetcher implements IExpenseFetcher {
  private IExpenseRepository expenseRepository;
  private IBudgetRepository budgetRepository;

  public ExpenseFetcher(IBudgetRepository budgetRepository, IExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
    this.budgetRepository = budgetRepository;
  }

  public List<Expense> getExpensesByBudgetId(long budgetId) {
    List<SExpense> sExpenses = this.expenseRepository.getExpensesByBudgetid(budgetId);
    return ExpensePivot.fromInfrastructureToDomain(sExpenses);
  }

  public Expense createExpense(long budgetId, Expense expense) {
    SBudget budget = this.budgetRepository.getBudgetById(budgetId);
    SExpense sExpense = ExpensePivot.fromDomainToInfrastructure(expense);
    sExpense.setBudget(budget);
    SExpense createdExpense = this.expenseRepository.createExpense(sExpense);
    return ExpensePivot.fromInfrastructureToDomain(createdExpense);
  }
}
