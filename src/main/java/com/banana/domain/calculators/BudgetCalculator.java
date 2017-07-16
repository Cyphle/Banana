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
      if (budget.getInitialAmount() < 0)
        throw new CreationException("Budget initial amount cannot be negative");
      else {
        List<Budget> budgets = this.budgetFetcher.getBudgetsOfUserAndAccount(account.getUser(), account.getId());
        for (Budget checkBudget : budgets) {
          if (checkBudget.getName() == budget.getName())
            throw new CreationException("Cannot create multiple budgets with the same name");
        }
        return this.budgetFetcher.createBudget(account, budget);
      }
    }
  }

  public Budget updateBudget(User user, long accountId, Budget budget) throws NoElementFoundException, UpdateException {
    List<Budget> budgets = this.budgetFetcher.getBudgetsOfUserAndAccount(user, accountId);
    if (budgets.size() == 0)
      throw new NoElementFoundException("No budget found for account " + accountId);
    else {
      List<Budget> doesExist = budgets.stream().filter(fetchedBudget -> fetchedBudget.getId() == budget.getId()).collect(Collectors.toList());
      if (doesExist.size() == 0)
        throw new NoElementFoundException("No budget found with id " + budget.getId());
      else {
        if (doesExist.get(0).getName() == budget.getName())
          throw new UpdateException("A budget with this name already exists");
        else if (budget.getInitialAmount() < 0)
          throw new UpdateException("Budget initial amount cannot be negative");
        else {
          Account account = this.accountFetcher.getAccountByUserAndId(user, accountId);
          return this.budgetFetcher.updateBudget(account, budget);
        }
      }
    }
  }

  public Expense addExpense(User user, long accountId, long budgetId, Expense expense) {
    Budget myBudget = this.budgetFetcher.getBudgetOfUserAndAccountById(user, accountId, budgetId);
    if (myBudget == null)
      throw new NoElementFoundException("No budget found with id " + budgetId);
    else {
      List<Expense> expenses = this.expenseFetcher.getExpensesByBudgetid(budgetId);
    }
    /*
      -> get budget to check if exists
      -> set amount to absolute
      -> save
     */
    return null;
  }

  /*
    FOR UPDATE
      Il faudra prendre en compte de supprimer les dépenses dans les cas de modification de start date et end date

   */
}
