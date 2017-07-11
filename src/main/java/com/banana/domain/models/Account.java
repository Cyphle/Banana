package com.banana.domain.models;

public class Account {
  private long id;
  private User user;
  private String name;
  private String slug;
  private double initialAmount;

  public Account(User user, String name, double initialAmount) {
    this.user = user;
    this.name = name;
    this.initialAmount = initialAmount;
  }

  public Account(long id, User user, String name, String slug, double initialAmount) {
    this(user, name, initialAmount);
    this.id = id;
    this.slug = slug;
  }

  public long getId() { return this.id; }

  public void setId(long id) { this.id = id; }

  public User getUser() { return this.user; };

  public String getName() {
    return this.name;
  }

  public String getSlug() { return this.slug; }

  public void setSlug(String slug) { this.slug = slug; }

  public double getInitialAmount() {
    return this.initialAmount;
  }
}
