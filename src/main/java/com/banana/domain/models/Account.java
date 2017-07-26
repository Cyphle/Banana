package com.banana.domain.models;

import java.util.Date;

public class Account {
  private long id;
  private User user;
  private String name;
  private String slug;
  private double initialAmount;
  private Date startDate;

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
}
