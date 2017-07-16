package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public interface IBudgetRepository {
  List<SBudget> getBudgetsOfUserAndAccount(SUser user, long accountId);
  SBudget createBudget(SBudget budget);
  SBudget updateBudget(SBudget budget);
}
