package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.UpdateException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.domain.ports.BudgetPort;
import com.banana.domain.validators.AccountVerifier;
import com.banana.utils.Moment;

import java.util.List;
import java.util.stream.Collectors;

public class BudgetCalculator implements BudgetPort {
  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private IExpenseFetcher expenseFetcher;
  private AccountVerifier accountVerifier;

  public BudgetCalculator(IAccountFetcher accountFetcher, IBudgetFetcher budgetFetcher, IExpenseFetcher expenseFetcher) {
    this.accountFetcher = accountFetcher;
    this.budgetFetcher = budgetFetcher;
    this.expenseFetcher = expenseFetcher;

    this.accountVerifier = new AccountVerifier(this.accountFetcher);
  }

  public Budget getBudgetById(User user, long accountId, long budgetId) {
    return this.budgetFetcher.getBudgetOfUserAndAccountById(user, accountId, budgetId);
  }

  public Budget createBudget(User user, long accountId, Budget budget) throws CreationException {
    this.accountVerifier.verifyAccount(user, accountId);
    if (this.isInitialAmountNegative(budget))
      throw new CreationException("Budget initial amount cannot be negative");
    else {
      budget.setStartDate(new Moment(budget.getStartDate()).getFirstDateOfMonth().getDate());
      return this.budgetFetcher.createBudget(accountId, budget);
    }
  }

  public Budget updateBudget(User user, long accountId, Budget budget) throws UpdateException {
    this.accountVerifier.verifyAccount(user, accountId);
    List<Budget> budgets = this.budgetFetcher
            .getBudgetsOfUserAndAccount(user, accountId)
            .stream()
            .filter(fetchedBudget -> fetchedBudget.getId() == budget.getId())
            .collect(Collectors.toList());
    if (budgets.size() == 0)
      throw new UpdateException("No budget found with id " + budget.getId());
    else {
      if (this.isInitialAmountNegative(budget))
        throw new UpdateException("Budget initial amount cannot be negative");
      else {
        Account account = this.accountFetcher.getAccountByUserAndId(user, accountId);
        Budget oldBudget = budgets.get(0);

        if (oldBudget.getInitialAmount() != budget.getInitialAmount()) {
          Budget newBudget = this.updateBudgetAmount(budget, account, oldBudget);
          return this.budgetFetcher.createBudget(accountId, newBudget);
        } else {
          Budget budgetToUpdate = this.updateBudgetProperties(budget, oldBudget, budget);
          return this.budgetFetcher.updateBudget(accountId, budgetToUpdate);
        }
      }
    }
  }

  public Budget deleteBudget(User user, long accountId, Budget budget) {
    this.accountVerifier.verifyAccount(user, accountId);
    Moment newEndDate = new Moment(budget.getEndDate()).getLastDateOfMonth();
    return this.budgetFetcher.updateBudget(accountId, this.updateBudgetEndDate(budget, newEndDate));
  }

  private Budget updateBudgetProperties(Budget budget, Budget oldBudget, Budget budgetToUpdate) {
    Moment oldStartDate = new Moment(oldBudget.getStartDate());
    Moment oldEndDate = null;
    if (oldBudget.getEndDate() != null) oldEndDate = new Moment(oldBudget.getEndDate());

    Moment newStartDate = new Moment(budget.getStartDate()).getFirstDateOfMonth().getFirstDateOfMonth();
    Moment newEndDate = null;
    if (budget.getEndDate() != null) newEndDate = new Moment(budget.getEndDate()).getLastDateOfMonth();

    if (oldStartDate.compareTo(newStartDate) != 0)
      budgetToUpdate = this.updateBudgetStartDate(budget, newStartDate);
    if ((oldEndDate == null && newEndDate != null) || (oldEndDate != null && newEndDate == null) || (oldEndDate != null && newEndDate != null && oldEndDate.compareTo(newEndDate) != 0))
      budgetToUpdate = this.updateBudgetEndDate(budget, newEndDate);

    return budgetToUpdate;
  }

  private Budget updateBudgetAmount(Budget budget, Account account, Budget oldBudget) {
    Moment oldBudgetEndDate = (new Moment(budget.getStartDate())).getLastDayOfPreviousMonth();
    oldBudget.setEndDate(oldBudgetEndDate.getDate());
    this.budgetFetcher.updateBudget(account.getId(), oldBudget);
    Budget newBudget = new Budget(budget.getName(), budget.getInitialAmount(), new Moment(budget.getStartDate()).getFirstDateOfMonth().getDate());
    if (budget.getEndDate() != null) newBudget.setEndDate(budget.getEndDate());
    return newBudget;
  }

  private Budget updateBudgetEndDate(Budget budget, Moment newEndDate) {
    if (newEndDate != null) {
      final Moment endDateCompare = new Moment(budget.getEndDate()).getLastDateOfMonth();
      List<Expense> expenses = this.expenseFetcher.getExpensesOfBudget(budget)
              .stream()
              .filter(expense -> {
                Moment expenseDate = new Moment(expense.getExpenseDate());
                return expenseDate.compareTo(endDateCompare) > 0;
              })
              .collect(Collectors.toList());
      for (Expense expense : expenses) {
        this.expenseFetcher.deleteExpense(expense);
      }
    }
    budget.setEndDate(newEndDate.getDate());
    return budget;
  }

  private Budget updateBudgetStartDate(Budget budget, Moment newStartDate) {
    List<Expense> expenses = this.expenseFetcher.getExpensesOfBudget(budget)
            .stream()
            .filter(expense -> {
              Moment expenseDate = new Moment(expense.getExpenseDate());
              return expenseDate.compareTo(newStartDate) < 0;
            })
            .collect(Collectors.toList());
    for (Expense expense : expenses) {
      this.expenseFetcher.deleteExpense(expense);
    }
    budget.setStartDate(newStartDate.getDate());
    return budget;
  }

  private boolean isInitialAmountNegative(Budget budget) {
    return budget.getInitialAmount() < 0;
  }
}
