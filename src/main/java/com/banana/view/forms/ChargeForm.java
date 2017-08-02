package com.banana.view.forms;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ChargeForm {
  private long id;
  private long accountId;
  @NotNull
  private String description;
  @NotNull
  private double amount;
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;

  public ChargeForm() { }

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
