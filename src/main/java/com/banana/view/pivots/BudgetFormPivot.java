package com.banana.view.pivots;

import com.banana.domain.models.Budget;
import com.banana.view.forms.BudgetForm;

public class BudgetFormPivot {
  public static BudgetForm fromDomainToView(Budget budget) {
    BudgetForm budgetForm = new BudgetForm();
    if (budget.getId() > 0) budgetForm.setId(budget.getId());
    else budgetForm.setId(-1);
    budgetForm.setName(budget.getName());
    budgetForm.setInitialAmount(budget.getInitialAmount());
    budgetForm.setStartDate(budget.getStartDate());
    if (budget.getEndDate() != null) budgetForm.setEndDate(budget.getEndDate());
    return budgetForm;
  }

  public static Budget fromViewToDomain(BudgetForm budgetForm) {
    Budget budget = new Budget(budgetForm.getName(), budgetForm.getInitialAmount(), budgetForm.getStartDate());
    if (budgetForm.getId() > 0) budget.setId(budgetForm.getId());
    if (budgetForm.getEndDate() != null) budget.setEndDate(budgetForm.getEndDate());
    return budget;
  }
}
