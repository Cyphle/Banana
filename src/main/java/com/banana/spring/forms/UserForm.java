package com.banana.spring.forms;

import com.banana.spring.models.SUser;
import org.hibernate.validator.constraints.Email;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {
  @NotNull
  private String lastname;
  @NotNull
  private String firstname;
  @NotNull
  @Email
  private String email;
  @NotNull
  @Size(min=2, max=100)
  private String password;

  private MultipartFile picture;

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

  public MultipartFile getPicture() {
    return this.picture;
  }

  public void setPicture(MultipartFile picture) {
    this.picture = picture;
  }

  public SUser toModel() {
    return new SUser(this.lastname, this.firstname, this.email, this.password);
  }
}
