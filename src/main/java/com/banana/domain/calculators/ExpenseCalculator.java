package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.domain.ports.ExpensePort;
import com.banana.domain.validators.AccountVerifier;
import com.banana.domain.validators.ExpenseVerifier;

import java.util.stream.Collectors;

public class ExpenseCalculator implements ExpensePort {
  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private IExpenseFetcher expenseFetcher;
  private AccountVerifier accountVerifier;
  private ExpenseVerifier expenseVerifier;

  public ExpenseCalculator(IAccountFetcher accountFetcher, IBudgetFetcher budgetFetcher, IExpenseFetcher expenseFetcher) {
    this.accountFetcher = accountFetcher;
    this.budgetFetcher = budgetFetcher;
    this.expenseFetcher = expenseFetcher;

    this.accountVerifier = new AccountVerifier(this.accountFetcher);
    this.expenseVerifier = new ExpenseVerifier(this.budgetFetcher, this.expenseFetcher);
  }

  public Expense getExpenseById(User user, long accountId, long budgetId, long expenseId) throws NoElementFoundException {
    if (this.isBudgetExpense(budgetId)) {
      this.expenseVerifier.verifyBudget(user, accountId, budgetId, expenseId);
      Budget myBudget = this.budgetFetcher.getBudgetOfUserAndAccountById(user, accountId, budgetId);
      return this.expenseFetcher.getExpensesOfBudget(myBudget).stream().filter(expense -> expense.getId() == expenseId).collect(Collectors.toList()).get(0);
    } else {
      this.accountVerifier.verifyAccount(user, accountId);
      Account account = this.accountFetcher.getAccountByUserAndId(user, accountId);
      return this.expenseFetcher.getExpensesOfAccount(account).stream().filter(expense -> expense.getId() == expenseId).collect(Collectors.toList()).get(0);
    }
  }

  public Expense createExpense(User user, long accountId, long budgetId, Expense expense) throws CreationException {
    if (this.isBudgetExpense(budgetId)) {
      this.expenseVerifier.verifyBudget(user, accountId, budgetId, expense);
      expense.setAmount(Math.abs(expense.getAmount()));
      return this.expenseFetcher.createBudgetExpense(budgetId, expense);
    } else {
      this.accountVerifier.verifyAccount(user, accountId);
      return this.expenseFetcher.createAccountExpense(accountId, expense);
    }
  }

  public Expense updateExpense(User user, long accountId, long budgetId, Expense expense) throws CreationException {
    if (expense.getId() > 0) {
      if (this.isBudgetExpense(budgetId)) {
        this.expenseVerifier.verifyBudget(user, accountId, budgetId, expense);
        expense.setAmount(Math.abs(expense.getAmount()));
        return this.expenseFetcher.updateBudgetExpense(budgetId, expense);
      } else {
        this.accountVerifier.verifyAccount(user, accountId);
        return this.expenseFetcher.updateAccountExpense(accountId, expense);
      }
    } else
      throw new NoElementFoundException("Unknown expense");
  }

  public boolean deleteExpense(User user, long accountId, long budgetId, Expense expense) {
    if (expense.getId() > 0) {
      if (this.isBudgetExpense(budgetId)) {
        this.expenseVerifier.verifyBudget(user, accountId, budgetId, expense);
      } else {
        this.accountVerifier.verifyAccount(user, accountId);
      }
      return this.expenseFetcher.deleteExpense(expense);
    } else
      throw new NoElementFoundException("Unknown expense");
  }

  private boolean isBudgetExpense(long budgetId) {
    return budgetId > 0;
  }
}
