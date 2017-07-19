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

  public Expense updateExpense(User user, long accountId, long budgetId, Expense expense) {
    if (this.isBudgetExpense(budgetId)) {
      Budget myBudget = this.budgetFetcher.getBudgetOfUserAndAccountById(user, accountId, budgetId);
      if (this.doesBudgetExists(myBudget)) {
        Moment expenseDate = new Moment(expense.getExpenseDate());
        double totalExpense = this.expenseFetcher
                                  .getExpensesByBudgetId(budgetId)
                                  .stream()
                                  .filter(fetchExpense -> (new Moment(fetchExpense.getExpenseDate())).isInMonthOfYear(expenseDate.getMonthNumber(), expenseDate.getYear()))
                                  .map(Expense::getAmount)
                                  .reduce(0.0, (a, b) -> a + b);
        if (totalExpense + expense.getAmount() > myBudget.getInitialAmount())
          throw new CreationException("Budget amount has been exceeded. Total amount would be : " + (totalExpense + expense.getAmount()));
        expense.setAmount(Math.abs(expense.getAmount()));
        return this.expenseFetcher.updateBudgetExpense(budgetId, expense);
      } else
        throw new NoElementFoundException("No budget found with id : " + budgetId);
    } else {
      Account myAccount = this.accountFetcher.getAccountByUserAndId(user, accountId);
      if (this.doesAccountExists(myAccount)) {
        return this.expenseFetcher.updateAccountExpense(accountId, expense);
      } else
        throw new NoElementFoundException("No account found with id : " + accountId);
    }
    /*
      - if budgetId <= 0 ==> it is an account expense
      - else ==> it is a budget expense
          -> if budget, check that it does not exceed budget
     */
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
}
