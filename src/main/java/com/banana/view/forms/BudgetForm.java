package com.banana.view.forms;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class BudgetForm {
  private long id;
  @NotNull
  private long accountId;
  @NotNull
  private String name;
  @NotNull
  private float initialAmount;
  @NotNull
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private Date startDate;
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private Date endDate;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getInitialAmount() {
    return initialAmount;
  }

  public void setInitialAmount(float initialAmount) {
    this.initialAmount = initialAmount;
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
