package com.banana.domain.models;

import java.util.Date;

public class Budget {
  private long id;
  private String name;
  private double initialAmount;
  private Date startDate;
  private Date endDate;

  public Budget(String name, double initialAmount, Date startDate) {
    this.name = name;
    this.initialAmount = initialAmount;
    this.startDate = startDate;
  }

  public Budget(long id, String name, double initialAmount, Date startDate) {
    this(name, initialAmount, startDate);
    this.id = id;
  }

  public long getId() { return this.id; }

  public void setId(long id) { this.id = id; }

  public String getName() {
    return this.name;
  }

  public void setName(String name) { this.name = name; }

  public double getInitialAmount() {
    return this.initialAmount;
  }

  public void setInitialAmount(double initialAmount) { this.initialAmount = initialAmount; }

  public Date getStartDate() { return this.startDate; }

  public void setStartDate(Date startDate) { this.startDate = startDate; }

  public Date getEndDate() { return this.endDate; }

  public void setEndDate(Date endDate) { this.endDate = endDate; }
}
