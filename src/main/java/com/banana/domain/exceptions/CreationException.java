package com.banana.domain.exceptions;

public class CreationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CreationException(String message) { super(message); }
}