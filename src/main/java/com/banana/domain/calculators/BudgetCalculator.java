package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;
import com.banana.domain.ports.BudgetPort;

import java.util.List;

public class BudgetCalculator implements BudgetPort {
  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;

  public BudgetCalculator(IAccountFetcher accountFetcher, IBudgetFetcher budgetFetcher) {
    this.accountFetcher = accountFetcher;
    this.budgetFetcher = budgetFetcher;
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

  public Budget updateBudget(User user, long accountId, Budget budget) throws CreationException {
    List<Budget> budgets = this.budgetFetcher.getBudgetsOfUserAndAccount(user, accountId);

  /*
    First check that there is an existing budget for user and account id with this id
    Should update budget
    -> check that budget initial amount is not negative
    -> check that budget id belongs to account id and user user
    -> check that budget name does not already exists
    -> update
   */
    return null;
  }
}
