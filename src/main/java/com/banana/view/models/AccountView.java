package com.banana.view.models;

import java.util.Date;

public class AccountView {
  private long id;
  private String name;
  private String slug;
  private double initialAmount;
  private Date startDate;
  private double beginMonthAmount;
  private double currentAmount;
  private double freeAmount;

  public AccountView(long id, String name, String slug, double initialAmount, Date startDate) {
    this.id = id;
    this.name = name;
    this.slug = slug;
    this.initialAmount = initialAmount;
    this.startDate = startDate;
  }

  public long getId() { return this.id; }

  public void setId(long id) { this.id = id; }

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

  public double getBeginMonthAmount() {
    return beginMonthAmount;
  }

  public void setBeginMonthAmount(double beginMonthAmount) {
    this.beginMonthAmount = beginMonthAmount;
  }

  public double getCurrentAmount() {
    return currentAmount;
  }

  public void setCurrentAmount(double currentAmount) {
    this.currentAmount = currentAmount;
  }

  public double getFreeAmount() {
    return freeAmount;
  }

  public void setFreeAmount(double freeAmount) {
    this.freeAmount = freeAmount;
  }
}
