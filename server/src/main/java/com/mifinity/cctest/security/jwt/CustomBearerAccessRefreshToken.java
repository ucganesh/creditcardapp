package com.mifinity.cctest.security.jwt;

import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

import java.util.Collection;

/**
 * Encapsulates AccessRefreshToken
 */
public class CustomBearerAccessRefreshToken extends BearerAccessRefreshToken {

  private Long userId;
  private String name;

  public CustomBearerAccessRefreshToken() { }

  public CustomBearerAccessRefreshToken(Long userId,
                                        String name,
                                        String username,
                                        Collection<String> roles,
                                        Integer expiresIn,
                                        String accessToken,
                                        String refreshToken,
                                        String tokenType)
  {
    super(username, roles, expiresIn, accessToken, refreshToken, tokenType);
    this.userId = userId;
    this.name = name;
  }

  public Long getUserId() {
    return userId;
  }

  public CustomBearerAccessRefreshToken setUserId(final Long userId) {
    this.userId = userId;
    return this;
  }

  public String getName() {
    return name;
  }

  public CustomBearerAccessRefreshToken setName(final String name) {
    this.name = name;
    return this;
  }
}
