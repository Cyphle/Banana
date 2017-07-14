package com.banana.utilities;

import com.banana.infrastructure.connector.repositories.IBudgetRepository;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utils.Moment;

import java.util.ArrayList;
import java.util.List;

public class FakeBudgetRepository implements IBudgetRepository {
  public List<SBudget> getBudgetsOfUserAndAccount(SUser user, long accountId) {
    Moment today = new Moment();
    List<SBudget> budgets = new ArrayList<>();
    budgets.add(new SBudget("Budget one", 200, today.getDate()));
    budgets.add(new SBudget("Budget two", 300, today.getDate()));
    return budgets;
  }
}
