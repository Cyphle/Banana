package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.domain.models.Expense;
import com.banana.infrastructure.connector.pivots.ExpensePivot;
import com.banana.view.forms.BudgetForm;
import com.banana.view.forms.ExpenseForm;
import com.banana.view.pivots.BudgetFormPivot;
import com.banana.view.pivots.ExpenseFormPivot;
import com.banana.view.services.BudgetService;
import com.banana.view.services.ExpenseService;
import com.banana.view.services.UserService;
import com.sun.org.apache.regexp.internal.RE;
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
@RequestMapping("/budgets")
public class BudgetController {
  private BudgetService budgetService;
  private ExpenseService expenseService;
  private UserService userService;

  @Autowired
  public BudgetController(UserService userService, BudgetService budgetService, ExpenseService expenseService) {
    this.userService = userService;
    this.budgetService = budgetService;
    this.expenseService = expenseService;
  }

  @RequestMapping(value = "/create/{accountId}", method = RequestMethod.GET)
  public String createBudget(@PathVariable long accountId, Model model) {
    BudgetForm budgetForm = new BudgetForm();
    budgetForm.setAccountId(accountId);
    model.addAttribute("budgetForm", budgetForm);
    model.addAttribute("user", this.userService.getAuthenticatedUser());
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

  @RequestMapping(value = "/update/{accountId}/{budgetId}", method = RequestMethod.GET)
  public String updateBudget(@PathVariable long accountId, @PathVariable long budgetId, Model model) {
    if (!model.containsAttribute("budget")) {
      BudgetForm budgetForm = BudgetFormPivot.fromDomainToView(this.budgetService.getBudget(accountId, budgetId));
      model.addAttribute("budgetForm", budgetForm);
    }
    return "budget/update-budget";
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String updateBudgetPost(@Valid BudgetForm budgetForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "budget/update-budget";

    Account account = this.budgetService.updateBudget(budgetForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "budget/update-budget";
  }

  @RequestMapping(value = "/expenses/create/{accountId}/{budgetId}", method = RequestMethod.GET)
  public String createExpense(@PathVariable long accountId, @PathVariable long budgetId, Model model) {
    model.addAttribute("expenseForm", new ExpenseForm(accountId, budgetId));
    return "expense/create-expense";
  }

  @RequestMapping(value = "/expenses/create", method = RequestMethod.POST)
  public String createExpensePost(@Valid ExpenseForm expenseForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "expense/create-expense";

    Account account = this.expenseService.createExpense(expenseForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "expense/create-expense";
  }

  @RequestMapping(value = "/expenses/update/{accountId}/{budgetId}/{expenseId}", method = RequestMethod.GET)
  public String updateExpense(@PathVariable long accountId, @PathVariable long budgetId, @PathVariable long expenseId, Model model) {
    ExpenseForm expenseForm = ExpenseFormPivot.fromDomainToView(this.expenseService.getExpense(accountId, budgetId, expenseId));
    expenseForm.setAccountId(accountId);
    if (budgetId > 0) expenseForm.setBudgetId(budgetId);
    model.addAttribute("expenseForm", expenseForm);
    return "expense/update-expense";
  }

  @RequestMapping(value = "/expenses/update", method = RequestMethod.POST)
  public String updateExpensePost(@Valid ExpenseForm expenseForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "expense/update-expense";

    Account account = this.expenseService.updateExpense(expenseForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "expense/update-expense";
  }
}
