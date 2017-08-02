package com.banana.view.forms;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class CreditForm {
  private long id;
  private long accountId;
  @NotNull
  private String description;
  @NotNull
  private double amount;
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date creditDate;

  public CreditForm() { }

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

  public Date getCreditDate() {
    return creditDate;
  }

  public void setCreditDate(Date creditDate) {
    this.creditDate = creditDate;
  }
}
