package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.view.forms.BudgetForm;
import com.banana.view.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/budgets")
public class BudgetController {
  private BudgetService budgetService;

  @Autowired
  public BudgetController(BudgetService budgetService) {
    this.budgetService = budgetService;
  }

  @RequestMapping(value = "/create", method = RequestMethod.GET)
  public String getCreateForm() {
    return "budget/create-budget";
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createBudgetPost(@Valid BudgetForm budgetForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "budget/create-budget";

    Account account = this.budgetService.createBudget(budgetForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "budget/create-budget";
  }
}
