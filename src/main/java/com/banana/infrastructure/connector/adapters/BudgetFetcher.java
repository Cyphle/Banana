package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.pivots.BudgetPivot;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IBudgetRepository;
import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public class BudgetFetcher implements IBudgetFetcher {
  private IAccountRepository accountRepository;
  private IBudgetRepository budgetRepository;

  public BudgetFetcher(IAccountRepository accountRepository, IBudgetRepository budgetRepository) {
    this.accountRepository = accountRepository;
    this.budgetRepository = budgetRepository;
  }

  public List<Budget> getBudgetsOfUserAndAccount(User user, long accountId) {
    SUser sUser = UserPivot.fromDomainToInfrastructure(user);
    List<SBudget> sBudgets = this.budgetRepository.getBudgetsOfUserAndAccount(sUser, accountId);

    // TODO should get budget expenses
    /*
        SHOULD GET EXPENSES
     */

    return BudgetPivot.fromInfrastructureToDomain(sBudgets);
  }

  public Budget getBudgetOfUserAndAccountById(User user, long accountId, long budgetId) {
    SUser sUser = UserPivot.fromDomainToInfrastructure(user);
    SBudget sBudget = this.budgetRepository.getBudgetOfUserAndAccountById(sUser, accountId, budgetId);
    return BudgetPivot.fromInfrastructureToDomain(sBudget);
  }

  public Budget createBudget(long accountId, Budget budget) {
    SBudget sBudget = this.fromDomainToInfrastructure(accountId, budget);
    SBudget createdBudget = this.budgetRepository.createBudget(sBudget);
    return BudgetPivot.fromInfrastructureToDomain(createdBudget);
  }

  public Budget updateBudget(long accountId, Budget budget) {
    SBudget budgetToUpdate = this.fromDomainToInfrastructure(accountId, budget);
    SBudget updatedBudget = this.budgetRepository.updateBudget(budgetToUpdate);
    return BudgetPivot.fromInfrastructureToDomain(updatedBudget);
  }

  private SBudget fromDomainToInfrastructure(long accountId, Budget budget) {
    SAccount sAccount = this.accountRepository.getAccountById(accountId);
    SBudget sBudget = BudgetPivot.fromDomainToInfrastructure(budget);
    sBudget.setAccount(sAccount);
    return sBudget;
  }
}
