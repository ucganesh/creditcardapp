package com.mifinity.cctest.exceptions;

/**
 * Exceptions related to account management
 */
public class AccountException extends RuntimeException {

  public AccountException(String message, Throwable cause) {
    super(message, cause);
  }

}
