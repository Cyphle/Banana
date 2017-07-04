package com.banana.spring.models;

import javax.persistence.*;

@Entity
@Table(name="user_roles")
public class SUserRole {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="id")
  private long id;
  @Column(name="user_id")
  private long userId;
  @Column(name="role")
  private String role;

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
