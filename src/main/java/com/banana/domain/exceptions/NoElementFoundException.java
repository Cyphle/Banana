package com.banana.domain.exceptions;

public class NoElementFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NoElementFoundException(String message) { super(message); }

  public NoElementFoundException(String message, Throwable cause) { super(message, cause); }
}
