package com.banana.domain.models;

public class User {
  private long id;
  private String lastname;
  private String firstname;
  private String username;

  public User(long id, String lastname, String firstname, String username) {
    this.id = id;
    this.lastname = lastname;
    this.firstname = firstname;
    this.username = username;
  }

  public long getId() { return this.id; }

  public String getLastname() { return this.lastname; }

  public String getFirstname() { return this.firstname; }

  public String getUsername() { return this.username; }
}
