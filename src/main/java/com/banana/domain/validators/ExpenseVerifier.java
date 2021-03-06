package com.banana.domain.validators;

import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.utils.Moment;

import java.util.stream.Collectors;

public class ExpenseVerifier {
  private IBudgetFetcher budgetFetcher;
  private IExpenseFetcher expenseFetcher;

  public ExpenseVerifier(IBudgetFetcher budgetFetcher, IExpenseFetcher expenseFetcher) {
    this.budgetFetcher = budgetFetcher;
    this.expenseFetcher = expenseFetcher;
  }

  public void verifyBudget(User user, long accountId, long budgetId, Expense expense) throws CreationException, NoElementFoundException {
    Budget myBudget = this.budgetFetcher.getBudgetOfUserAndAccountById(user, accountId, budgetId);
    this.verifyBudgetExistence(budgetId, myBudget);
    this.verifyBudgetAmountIsNotExceeded(expense, myBudget);
  }

  public void verifyBudget(User user, long accountId, long budgetId, long expenseId) throws NoElementFoundException {
    Budget myBudget = this.budgetFetcher.getBudgetOfUserAndAccountById(user, accountId, budgetId);
    this.verifyBudgetExistence(budgetId, myBudget);
    Expense myExpense = this.expenseFetcher.getExpensesOfBudget(myBudget).stream().filter(expense -> expense.getId() == expenseId).collect(Collectors.toList()).get(0);
    if (myExpense == null)
      throw new NoElementFoundException("No expense found with id : " + expenseId);

  }

  private void verifyBudgetAmountIsNotExceeded(Expense expense, Budget myBudget) throws CreationException {
    double totalExpense = this.getTotalExpense(expense, myBudget);
    if (totalExpense + expense.getAmount() > myBudget.getInitialAmount())
      throw new CreationException("Budget amount has been exceeded. Total amount would be : " + (totalExpense + expense.getAmount()));
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

  private void verifyBudgetExistence(long budgetId, Budget myBudget) {
    if (!this.doesBudgetExists(myBudget))
      throw new NoElementFoundException("No budget found with id " + budgetId);
  }

  private boolean doesBudgetExists(Budget myBudget) {
    return myBudget != null;
  }
}
