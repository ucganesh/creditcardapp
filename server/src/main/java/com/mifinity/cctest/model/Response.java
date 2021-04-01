package com.mifinity.cctest.model;

/**
 * Standard response object for HTTP requests
 */
public class Response {
  private int code;
  private String message;

  public Response(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public Response() {}

  public void setCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
