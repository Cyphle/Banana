package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.view.forms.ExpenseForm;
import com.banana.view.pivots.ExpenseFormPivot;
import com.banana.view.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {
  private ExpenseService expenseService;

  @Autowired
  public ExpenseController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @RequestMapping(value = "/create/{accountId}", method = RequestMethod.GET)
  public String createExpense(@PathVariable long accountId, Model model) {
    model.addAttribute("expenseForm", new ExpenseForm(accountId, -1));
    return "expense/create-expense";
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createExpensePost(@Valid ExpenseForm expenseForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "expense/create-expense";

    Account account = this.expenseService.createExpense(expenseForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "expense/create-expense";
  }

  @RequestMapping(value = "/update/{accountId}/{expenseId}", method = RequestMethod.GET)
  public String updateExpense(@PathVariable long accountId, @PathVariable long expenseId, Model model) {
    ExpenseForm expenseForm = ExpenseFormPivot.fromDomainToView(this.expenseService.getExpense(accountId, -1, expenseId));
    expenseForm.setAccountId(accountId);
    expenseForm.setBudgetId(-1);
    model.addAttribute("expenseForm", expenseForm);
    return "expense/update-expense";
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String updateExpensePost(@Valid ExpenseForm expenseForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "expense/update-expense";

    Account account = this.expenseService.updateExpense(expenseForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "expense/update-expense";
  }
}
