package com.banana.view.pivots;

import com.banana.domain.models.Expense;
import com.banana.view.forms.ExpenseForm;

public class ExpenseFormPivot {
  public static ExpenseForm fromDomainToView(Expense expense) {
    ExpenseForm expenseForm = new ExpenseForm();
    expenseForm.setDescription(expense.getDescription());
    expenseForm.setAmount(expense.getAmount());
    expenseForm.setExpenseDate(expense.getExpenseDate());
    if (expense.getDebitDate() != null) expenseForm.setDebitDate(expense.getDebitDate());
    if (expense.getId() > 0) expenseForm.setId(expense.getId());
    else expenseForm.setId(-1);
    return expenseForm;
  }

  public static Expense fromViewToDomain(ExpenseForm expenseForm) {
    Expense expense = new Expense(expenseForm.getDescription(), expenseForm.getAmount(), expenseForm.getExpenseDate());
    if (expenseForm.getId() > 0) expense.setId(expenseForm.getId());
    if (expenseForm.getDebitDate() != null) expense.setDebitDate(expenseForm.getDebitDate());
    return expense;
  }
}
