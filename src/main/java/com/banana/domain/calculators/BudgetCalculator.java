package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.ports.BudgetPort;

import java.util.List;

public class BudgetCalculator implements BudgetPort {
  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;

  public BudgetCalculator(IAccountFetcher accountFetcher, IBudgetFetcher budgetFetcher) {
    this.accountFetcher = accountFetcher;
    this.budgetFetcher = budgetFetcher;
  }

  public Budget createBudget(Account account, Budget budget) throws CreationException {
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
