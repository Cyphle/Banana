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
  private IUserRepository userRepository;
  private IAccountRepository accountRepository;
  private IBudgetRepository budgetRepository;

  public BudgetFetcher(IUserRepository userRepository, IAccountRepository accountRepository, IBudgetRepository budgetRepository) {
    this.userRepository = userRepository;
    this.accountRepository = accountRepository;
    this.budgetRepository = budgetRepository;
  }

  public List<Budget> getBudgetsOfUserAndAccount(User user, long accountId) {
    SUser sUser = UserPivot.fromDomainToInfrastructure(user);
    List<SBudget> sBudgets = this.budgetRepository.getBudgetsOfUserAndAccount(sUser, accountId);
    return BudgetPivot.fromInfrastructureToDomain(sBudgets);
  }

  public Budget createBudget(Account account, Budget budget) {
    SBudget sBudget = this.fromDomainToInfrastructure(account, budget);
    SBudget createdBudget = this.budgetRepository.createBudget(sBudget);
    return BudgetPivot.fromInfrastructureToDomain(createdBudget);
  }

  public Budget updateBudget(Account account, Budget budget) {
    SBudget budgetToUpdate = this.fromDomainToInfrastructure(account, budget);
    SBudget updatedBudget = this.budgetRepository.updateBudget(budgetToUpdate);
    return BudgetPivot.fromInfrastructureToDomain(updatedBudget);
  }

  private SBudget fromDomainToInfrastructure(Account account, Budget budget) {
    SUser user = this.userRepository.getUserByUsername(account.getUser().getUsername());
    SAccount sAccount = this.accountRepository.getAccountByUserAndId(user, account.getId());
    sAccount.setUser(user);
    SBudget sBudget = BudgetPivot.fromDomainToInfrastructure(budget);
    sBudget.setAccount(sAccount);
    return sBudget;
  }
}
