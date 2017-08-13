package com.banana.view.controllers;

import com.banana.utils.Status;
import com.banana.view.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
public class APIExpenseController {
  private ExpenseService expenseService;

  @Autowired
  public APIExpenseController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @RequestMapping(value = "/delete/{accountId}/{expenseId}", method = RequestMethod.GET)
  public Status deleteBudgetExpense(@PathVariable long accountId, @PathVariable long expenseId) {
    return new Status(this.expenseService.deleteExpense(accountId, -1, expenseId), "Delete expense of id : " + expenseId);
  }
}
