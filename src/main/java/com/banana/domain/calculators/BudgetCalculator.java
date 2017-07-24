package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.exceptions.UpdateException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.domain.ports.BudgetPort;
import com.banana.utils.Moment;

import java.util.List;
import java.util.stream.Collectors;

public class BudgetCalculator implements BudgetPort {
  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private IExpenseFetcher expenseFetcher;

  public BudgetCalculator(IAccountFetcher accountFetcher, IBudgetFetcher budgetFetcher, IExpenseFetcher expenseFetcher) {
    this.accountFetcher = accountFetcher;
    this.budgetFetcher = budgetFetcher;
    this.expenseFetcher = expenseFetcher;
  }

  public Budget createBudget(User user, long accountId, Budget budget) throws CreationException {
    Account account = this.accountFetcher.getAccountByUserAndId(user, accountId);
    if (account == null)
      throw new CreationException("No account for user and id : " + accountId);
    else {
      if (this.isInitialAmountNegative(budget))
        throw new CreationException("Budget initial amount cannot be negative");
      else {
        return this.budgetFetcher.createBudget(account, budget);
      }
    }
  }

  public Budget updateBudget(User user, long accountId, Budget budget) throws NoElementFoundException, UpdateException {
    List<Budget> budgets = this.budgetFetcher
            .getBudgetsOfUserAndAccount(user, accountId)
            .stream()
            .filter(fetchedBudget -> fetchedBudget.getId() == budget.getId())
            .collect(Collectors.toList());
    if (budgets.size() == 0)
      throw new NoElementFoundException("No budget found with id " + budget.getId());
    else {
      if (this.isInitialAmountNegative(budget))
        throw new UpdateException("Budget initial amount cannot be negative");
      else {
        Account account = this.accountFetcher.getAccountByUserAndId(user, accountId);
        Budget oldBudget = budgets.get(0);

        Moment oldStartDate = new Moment(oldBudget.getStartDate());
        Moment oldEndDate = null;
        if (oldBudget.getEndDate() != null) oldEndDate = new Moment(oldBudget.getEndDate());
        Moment newStartDate = new Moment(budget.getStartDate()).getFirstDateOfMonth().getFirstDateOfMonth();
        Moment newEndDate = null;
        if (budget.getEndDate() != null) newEndDate = new Moment(budget.getEndDate()).getLastDateOfMonth();

        if (oldBudget.getInitialAmount() != budget.getInitialAmount()) {
          return this.updateBudgetAmount(budget, account, oldBudget);
        } else if (oldStartDate.compareTo(newStartDate) != 0) {
          return this.updateBudgetStartDate(budget, account, newStartDate);
        } if ((oldEndDate == null && newEndDate != null) || (oldEndDate != null && newEndDate == null) || (oldEndDate != null && newEndDate != null && oldEndDate.compareTo(newEndDate) != 0)) {
          return this.updateBudgetEndDate(budget, account, newEndDate);
        } else {
          return this.budgetFetcher.updateBudget(account, budget);
        }
      }
    }
  }

  private Budget updateBudgetEndDate(Budget budget, Account account, Moment newEndDate) {
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
        this.expenseFetcher.deleteBudgetExpense(budget.getId(), expense);
      }
    }
    budget.setEndDate(newEndDate.getDate());
    return this.budgetFetcher.updateBudget(account, budget);
  }

  private Budget updateBudgetStartDate(Budget budget, Account account, Moment newStartDate) {
    List<Expense> expenses = this.expenseFetcher.getExpensesOfBudget(budget)
                                                .stream()
                                                .filter(expense -> {
                                                  Moment expenseDate = new Moment(expense.getExpenseDate());
                                                  return expenseDate.compareTo(newStartDate) < 0;
                                                })
                                                .collect(Collectors.toList());
    for (Expense expense : expenses) {
      this.expenseFetcher.deleteBudgetExpense(budget.getId(), expense);
    }
    budget.setStartDate(newStartDate.getDate());
    return this.budgetFetcher.updateBudget(account, budget);
  }

  private Budget updateBudgetAmount(Budget budget, Account account, Budget oldBudget) {
    Moment oldBudgetEndDate = (new Moment(budget.getStartDate())).getLastDayOfPrecedingMonth();
    oldBudget.setEndDate(oldBudgetEndDate.getDate());
    this.budgetFetcher.updateBudget(account, oldBudget);
    Budget newBudget = new Budget(budget.getName(), budget.getInitialAmount(), new Moment(budget.getStartDate()).getFirstDateOfMonth().getDate());
    if (budget.getEndDate() != null) newBudget.setEndDate(budget.getEndDate());
    return this.budgetFetcher.updateBudget(account, newBudget);
  }

  private boolean isInitialAmountNegative(Budget budget) {
    return budget.getInitialAmount() < 0;
  }
}
