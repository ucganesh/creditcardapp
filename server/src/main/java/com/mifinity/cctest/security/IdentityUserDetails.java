package com.mifinity.cctest.security;

import io.micronaut.security.authentication.UserDetails;

import java.util.Collection;

/**
 * Customer UserDetails entity to capture User Id, identity name and roles
 */
public class IdentityUserDetails extends UserDetails {

  private Long userId;

  private String name;

  public IdentityUserDetails(String username, Collection<String> roles) {
    super(username, roles);
  }


  public IdentityUserDetails(String username, Collection<String> roles, Long userId, String name) {
    super(username, roles);
    this.userId = userId;
    this.name = name;
  }

  public Long getUserId() {
    return userId;
  }

  public IdentityUserDetails setUserId(final Long userId) {
    this.userId = userId;
    return this;
  }

  public String getName() {
    return name;
  }

  public IdentityUserDetails setName(final String name) {
    this.name = name;
    return this;
  }
}
