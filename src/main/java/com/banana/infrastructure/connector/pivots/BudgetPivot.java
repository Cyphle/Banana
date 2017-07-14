package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Budget;
import com.banana.infrastructure.orm.models.SBudget;

import java.util.ArrayList;
import java.util.List;

public class BudgetPivot {
  public static Budget fromInfrastructureToDomain(SBudget sBudget) {
    Budget budget = new Budget(sBudget.getName(), sBudget.getInitialAmount(), sBudget.getStartDate());
    if (sBudget.getId() > 0)
      budget.setId(sBudget.getId());
    return budget;
  }

  public static List<Budget> fromInfrastructureToDomain(List<SBudget> sBudgets) {
    List<Budget> budgets = new ArrayList<>();
    for (SBudget sBudget : sBudgets) {
      Budget budget = BudgetPivot.fromInfrastructureToDomain(sBudget);
      budgets.add(budget);
    }
    return budgets;
  }
}
