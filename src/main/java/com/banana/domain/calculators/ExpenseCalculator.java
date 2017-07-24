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
import com.banana.utils.Moment;

public class ExpenseCalculator implements ExpensePort {
  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private IExpenseFetcher expenseFetcher;

  public ExpenseCalculator(IAccountFetcher accountFetcher, IBudgetFetcher budgetFetcher, IExpenseFetcher expenseFetcher) {
    this.accountFetcher = accountFetcher;
    this.budgetFetcher = budgetFetcher;
    this.expenseFetcher = expenseFetcher;
  }

  public Expense createExpense(User user, long accountId, long budgetId, Expense expense) throws CreationException, NoElementFoundException {
    if (this.isBudgetExpense(budgetId)) {
      this.verifyBudget(user, accountId, budgetId, expense);
      expense.setAmount(Math.abs(expense.getAmount()));
      return this.expenseFetcher.createBudgetExpense(budgetId, expense);
    } else {
      this.verifyAccount(user, accountId);
      return this.expenseFetcher.createAccountExpense(accountId, expense);
    }
  }

  public Expense updateExpense(User user, long accountId, long budgetId, Expense expense) throws CreationException, NoElementFoundException {
    if (expense.getId() > 0) {
      if (this.isBudgetExpense(budgetId)) {
        this.verifyBudget(user, accountId, budgetId, expense);
        expense.setAmount(Math.abs(expense.getAmount()));
        return this.expenseFetcher.updateBudgetExpense(budgetId, expense);
      } else {
        this.verifyAccount(user, accountId);
        return this.expenseFetcher.updateAccountExpense(accountId, expense);
      }
    } else
      throw new CreationException("Unknow expense");
  }

  private boolean doesAccountExists(Account myAccount) {
    return myAccount != null;
  }

  private boolean doesBudgetExists(Budget myBudget) {
    return myBudget != null;
  }

  private boolean isBudgetExpense(long budgetId) {
    return budgetId > 0;
  }

  private double getTotalExpense(Expense expense, Budget myBudget) {
    Moment expenseDate = new Moment(expense.getExpenseDate());
    return this.expenseFetcher
            .getExpensesOfBudget(myBudget)
            .stream()
            .filter(fetchExpense -> (new Moment(fetchExpense.getExpenseDate())).isInMonthOfYear(expenseDate.getMonthNumber(), expenseDate.getYear()))
            .map(Expense::getAmount)
            .reduce(0.0, (a, b) -> a + b);
  }

  private void verifyAccount(User user, long accountId) {
    Account myAccount = this.accountFetcher.getAccountByUserAndId(user, accountId);
    if (!this.doesAccountExists(myAccount))
      throw new NoElementFoundException("No account found with id : " + accountId);
  }

  private void verifyBudget(User user, long accountId, long budgetId, Expense expense) {
    Budget myBudget = this.budgetFetcher.getBudgetOfUserAndAccountById(user, accountId, budgetId);
    this.verifyBudgetExistence(budgetId, myBudget);
    this.verifyBudgetAmountIsNotExceeded(expense, myBudget);
  }

  private void verifyBudgetAmountIsNotExceeded(Expense expense, Budget myBudget) {
    double totalExpense = this.getTotalExpense(expense, myBudget);
    if (totalExpense + expense.getAmount() > myBudget.getInitialAmount())
      throw new CreationException("Budget amount has been exceeded. Total amount would be : " + (totalExpense + expense.getAmount()));
  }

  private void verifyBudgetExistence(long budgetId, Budget myBudget) {
    if (!this.doesBudgetExists(myBudget))
      throw new NoElementFoundException("No budget found with id " + budgetId);
  }
}
