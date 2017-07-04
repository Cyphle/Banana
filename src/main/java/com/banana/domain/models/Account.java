package com.banana.domain.models;

public class Account {
  private long id;
  private User user;
  private String name;
  private double initialAmount;

  public Account(long id, User user, String name, double initialAmount) {
    this.id = id;
    this.user = user;
    this.name = name;
    this.initialAmount = initialAmount;
  }

  public long getId() { return this.id; }

  public String getName() {
    return this.name;
  }

  public double getInitialAmount() {
    return this.initialAmount;
  }
}
