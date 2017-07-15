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
    if (sBudget.getEndDate() != null)
      budget.setEndDate(sBudget.getEndDate());
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

  public static SBudget fromDomainToInfrastructure(Budget budget) {
    SBudget sBudget = new SBudget(budget.getName(), budget.getInitialAmount(), budget.getStartDate());
    if (budget.getEndDate() != null)
      sBudget.setEndDate(budget.getEndDate());
    if (budget.getId() > 0)
      sBudget.setId(budget.getId());
    return sBudget;
  }
}
