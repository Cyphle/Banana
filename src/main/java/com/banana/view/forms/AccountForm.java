package com.banana.view.forms;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class AccountForm {
  private long id;
  @NotNull
  @Size(min=2, max=30)
  private String name;
  @NotNull
  private double initialAmount;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getInitialAmount() {
    return this.initialAmount;
  }

  public void setInitialAmount(double initialAmount) {
    this.initialAmount = initialAmount;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
}
