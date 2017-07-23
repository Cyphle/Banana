package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Expense;
import com.banana.infrastructure.connector.pivots.ExpensePivot;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IBudgetRepository;
import com.banana.infrastructure.connector.repositories.IExpenseRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SExpense;

import java.util.List;

public class ExpenseFetcher implements IExpenseFetcher {
  private IAccountRepository accountRepository;
  private IExpenseRepository expenseRepository;
  private IBudgetRepository budgetRepository;

  public ExpenseFetcher(IAccountRepository accountRepository, IBudgetRepository budgetRepository, IExpenseRepository expenseRepository) {
    this.accountRepository = accountRepository;
    this.expenseRepository = expenseRepository;
    this.budgetRepository = budgetRepository;
  }

  public List<Expense> getExpensesOfAccount(long accountId) {
    return null;
  }

  public List<Expense> getExpensesOfBudget(long budgetId) {
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

  public Expense updateBudgetExpense(long budgetId, Expense expense) {
    SBudget budget = this.budgetRepository.getBudgetById(budgetId);
    SExpense sExpense = ExpensePivot.fromDomainToInfrastructure(expense);
    sExpense.setBudget(budget);
    SExpense updatedExpense = this.expenseRepository.updateExpense(sExpense);
    return ExpensePivot.fromInfrastructureToDomain(updatedExpense);
  }

  public Expense updateAccountExpense(long accountId, Expense expense) {
    SAccount account = this.accountRepository.getAccountById(accountId);
    SExpense sExpense = ExpensePivot.fromDomainToInfrastructure(expense);
    sExpense.setAccount(account);
    SExpense updatedExpense = this.expenseRepository.updateExpense(sExpense);
    return ExpensePivot.fromInfrastructureToDomain(updatedExpense);
  }
}
