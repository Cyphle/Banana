package com.banana.view.controllers;

import com.banana.utils.Status;
import com.banana.view.services.BudgetService;
import com.banana.view.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
public class APIBudgetController {
  private BudgetService budgetService;
  private ExpenseService expenseService;

  @Autowired
  public APIBudgetController(BudgetService budgetService, ExpenseService expenseService) {
    this.budgetService = budgetService;
    this.expenseService = expenseService;
  }

  /**
   *
   * @param accountId
   * @param budgetId
   * @param date in format yyyy-MM-dd
   * @return Status
   */
  @RequestMapping(value = "/{accountId}/{budgetId}", method = RequestMethod.DELETE)
  public Status deleteBudget(@PathVariable long accountId, @PathVariable long budgetId, @RequestParam(required = false) String date) {
    return new Status(this.budgetService.deleteBudget(accountId, budgetId, date), "Delete budget of id : " + budgetId);
  }

  @RequestMapping(value = "/expenses/{accountId}/{budgetId}/{expenseId}", method = RequestMethod.DELETE)
  public Status deleteBudgetExpense(@PathVariable long accountId, @PathVariable long budgetId, @PathVariable long expenseId) {
    return new Status(this.expenseService.deleteExpense(accountId, budgetId, expenseId), "Delete expense of id : " + expenseId);
  }
}
