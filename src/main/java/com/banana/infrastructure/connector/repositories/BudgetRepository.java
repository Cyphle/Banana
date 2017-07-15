package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SBudgetRepository;
import com.banana.utils.Moment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BudgetRepository implements IBudgetRepository {
  private SBudgetRepository budgetRepository;

  @Autowired
  public BudgetRepository(SBudgetRepository budgetRepository) {
    this.budgetRepository = budgetRepository;
  }

  public List<SBudget> getBudgetsOfUserAndAccount(SUser user, long accountId) {
    return this.budgetRepository.findByUserUsernameAndAccountId(user.getUsername(), accountId);
  }

  public SBudget createBudget(SBudget budget) {
    Moment today = new Moment();
    budget.setCreationDate(today.getDate());
    budget.setUpdateDate(today.getDate());
    return this.budgetRepository.save(budget);
  }
}
