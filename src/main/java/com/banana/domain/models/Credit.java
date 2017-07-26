package com.banana.domain.models;

import java.util.Date;

public class Credit {
  private long id;
  private String description;
  private double amount;
  private Date creditDate;

  public Credit(String description, double amount, Date creditDate) {
    this.description = description;
    this.amount = amount;
    this.creditDate = creditDate;
  }

  public Credit(long id, String description, double amount, Date creditDate) {
    this(description, amount, creditDate);
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

  public Date getCreditDate() {
    return creditDate;
  }

  public void setCreditDate(Date creditDate) {
    this.creditDate = creditDate;
  }
}
