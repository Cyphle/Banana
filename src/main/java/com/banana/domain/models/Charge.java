package com.banana.domain.models;

import java.util.Date;

public class Charge {
  private long id;
  private String description;
  private double amount;
  private Date startDate;
  private Date endDate;

  public Charge(String description, double amount, Date startDate) {
    this.description = description;
    this.amount = amount;
    this.startDate = startDate;
  }

  public Charge(long id, String description, double amount, Date startDate) {
    this(description, amount, startDate);
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
}
