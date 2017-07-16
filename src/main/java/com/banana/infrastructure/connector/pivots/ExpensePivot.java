package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Expense;
import com.banana.infrastructure.orm.models.SExpense;

import java.util.ArrayList;
import java.util.List;

public class ExpensePivot {
  public static Expense fromInfrastructureToDomain(SExpense sExpense) {
    if (sExpense != null) {
      Expense expense = new Expense(sExpense.getDescription(), sExpense.getAmount(), sExpense.getExpenseDate());
      if (sExpense.getId() > 0)
        expense.setId(sExpense.getId());
      if (sExpense.getDebitDate() != null)
        expense.setDebitDate(sExpense.getDebitDate());
      return expense;
    } else
      return null;
  }

  public static List<Expense> fromInfrastructureToDomain(List<SExpense> sExpenses) {
    List<Expense> expenses = new ArrayList<>();
    for (SExpense sExpense : sExpenses) {
      expenses.add(ExpensePivot.fromInfrastructureToDomain(sExpense));
    }
    return expenses;
  }
}
