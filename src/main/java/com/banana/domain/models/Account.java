package com.banana.domain.models;

import java.util.Date;
import java.util.List;

public class Account {
  private long id;
  private User user;
  private String name;
  private String slug;
  private double initialAmount;
  private Date startDate;
  private List<Budget> budgets;
  private List<Charge> charges;
  private List<Expense> expenses;
  private List<Credit> credits;

  public Account(User user, String name, double initialAmount, Date startDate) {
    this.user = user;
    this.name = name;
    this.initialAmount = initialAmount;
    this.startDate = startDate;
  }

  public Account(long id, User user, String name, String slug, double initialAmount, Date startDate) {
    this(user, name, initialAmount, startDate);
    this.id = id;
    this.slug = slug;
  }

  public long getId() { return this.id; }

  public void setId(long id) { this.id = id; }

  public User getUser() { return this.user; };

  public String getName() {
    return this.name;
  }

  public void setName(String name) { this.name = name; }

  public String getSlug() { return this.slug; }

  public void setSlug(String slug) { this.slug = slug; }

  public double getInitialAmount() {
    return this.initialAmount;
  }

  public void setInitialAmount(double initialAmount) { this.initialAmount = initialAmount; }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public List<Budget> getBudgets() {
    return budgets;
  }

  public void setBudgets(List<Budget> budgets) {
    this.budgets = budgets;
  }

  public List<Charge> getCharges() {
    return charges;
  }

  public void setCharges(List<Charge> charges) {
    this.charges = charges;
  }

  public List<Expense> getExpenses() {
    return expenses;
  }

  public void setExpenses(List<Expense> expenses) {
    this.expenses = expenses;
  }

  public List<Credit> getCredits() {
    return credits;
  }

  public void setCredits(List<Credit> credits) {
    this.credits = credits;
  }
}
