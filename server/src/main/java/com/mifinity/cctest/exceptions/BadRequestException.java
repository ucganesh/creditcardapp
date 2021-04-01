package com.mifinity.cctest.exceptions;

/**
 * Exceptions related to account management
 */
public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }

}
