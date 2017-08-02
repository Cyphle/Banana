package com.banana.view.forms;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ExpenseForm {
  private long id;
  private long accountId;
  private long budgetId;
  @NotNull
  private String description;
  @NotNull
  private double amount;
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date expenseDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date debitDate;

  public ExpenseForm() {
  }

  public ExpenseForm(long accountId, long budgetId) {
    this.accountId = accountId;
    this.budgetId = budgetId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getAccountId() {
    return accountId;
  }

  public void setAccountId(long accountId) {
    this.accountId = accountId;
  }

  public long getBudgetId() {
    return budgetId;
  }

  public void setBudgetId(long budgetId) {
    this.budgetId = budgetId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Date getExpenseDate() {
    return expenseDate;
  }

  public void setExpenseDate(Date expenseDate) {
    this.expenseDate = expenseDate;
  }

  public Date getDebitDate() {
    return debitDate;
  }

  public void setDebitDate(Date debitDate) {
    this.debitDate = debitDate;
  }
}
