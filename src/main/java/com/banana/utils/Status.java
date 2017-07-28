package com.banana.utils;

public class Status {
  private int status;
  private String message;

  public Status(boolean status, String message) {
    if (status) {
      this.status = 200;
      this.message = message;
    }
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
