package com.banana.domain.models;

public class User {
  private String lastname;
  private String firstname;
  private String username;

  public User(String lastname, String firstname, String username) {
    this.lastname = lastname;
    this.firstname = firstname;
    this.username = username;
  }

  public String getLastname() { return this.lastname; }

  public String getFirstname() { return this.firstname; }

  public String getUsername() { return this.username; }
}
