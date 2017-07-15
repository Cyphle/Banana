package com.banana.infrastructure.orm.models;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class SUser implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "username", unique = true)
  private String username;
  @Column(name = "slug", unique = true)
  private String slug;
  @Column(name = "lastname")
  private String lastname;
  @Column(name = "firstname")
  private String firstname;
  @Column(name = "email", unique = true)
  private String email;
  @Column(name = "password")
  private String password;
  @Column(name = "picture")
  private String picture;
  @Column(name = "is_deleted")
  private boolean isDeleted;

  public SUser() {
  }

  public SUser(SUser user) {
    this.id = user.id;
    this.username = user.username;
    this.slug = user.slug;
    this.lastname = user.lastname;
    this.firstname = user.firstname;
    this.email = user.email;
    this.password = user.password;
    this.picture = user.picture;
    this.isDeleted = user.isDeleted;
  }

  public SUser(String lastname, String firstname, String email) {
    this.lastname = lastname;
    this.firstname = firstname;
    this.slug = firstname + "." + lastname;
    this.email = email;
    this.username = email;
  }

  public SUser(String lastname, String firstname, String email, String password) {
    this(lastname, firstname, email);
    this.password = password;
    this.picture = "";
    this.isDeleted = false;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getSlug() {
    return this.slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getLastname() {
    return this.lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getFirstname() {
    return this.firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPicture() {
    return this.picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public boolean getEnabled() {
    return this.isDeleted;
  }

  public void setEnabled(boolean enabled) {
    this.isDeleted = enabled;
  }
}
