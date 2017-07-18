package com.banana.domain.models;

import java.util.Date;

public class Expense {
  private long id;
  private String description;
  private double amount;
  private Date expenseDate;
  private Date debitDate;

  public Expense(String description, double amount, Date expenseDate) {
    this.description = description;
    this.amount = amount;
    this.expenseDate = expenseDate;
  }

  public Expense(long id, String description, double amount, Date expenseDate) {
    this(description, amount, expenseDate);
    this.id = id;
  }

  public long getId() { return this.id; }

  public void setId(long id) { this.id = id; }

  public String getDescription() { return this.description; }

  public void setDescription(String description) { this.description = description; }

  public double getAmount() { return this.amount; }

  public void setAmount(double amount) { this.amount = amount; }

  public Date getExpenseDate() { return this.expenseDate; }

  public Date getDebitDate() { return this.debitDate; }

  public void setDebitDate(Date debitDate) { this.debitDate = debitDate; }
}
