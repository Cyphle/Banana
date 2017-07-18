package com.banana.domain.calculators;

import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.domain.ports.ExpensePort;

public class ExpenseCalculator implements ExpensePort {
  public Expense updateExpense(User user, long accountId, long budgetId, Expense expense) {
    /*
      - if budgetId <= 0 ==> it is an account expense
      - else ==> it is a budget expense
          -> if budget, check that it does not exceed budget
     */
    return  null;
  }
}
