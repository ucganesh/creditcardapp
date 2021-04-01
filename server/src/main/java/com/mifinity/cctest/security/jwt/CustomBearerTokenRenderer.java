package com.mifinity.cctest.security.jwt;

import com.mifinity.cctest.security.IdentityUserDetails;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerTokenRenderer;

import javax.inject.Singleton;

/**
 * Custom Token Renderer to populate the details needed of authenticated user
 */
@Singleton
@Replaces(bean = BearerTokenRenderer.class)
public class CustomBearerTokenRenderer extends BearerTokenRenderer {

  private final String BEARER_TOKEN_TYPE = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER;

  @Override
  public AccessRefreshToken render(UserDetails userDetails, Integer expiresIn, String accessToken, @Nullable String refreshToken) {
    if (userDetails instanceof IdentityUserDetails) {
      IdentityUserDetails identityUserDetails = (IdentityUserDetails) userDetails;
      return new CustomBearerAccessRefreshToken(identityUserDetails.getUserId(), identityUserDetails.getName(),
        identityUserDetails.getUsername(), identityUserDetails.getRoles(), expiresIn, accessToken,
        refreshToken, BEARER_TOKEN_TYPE);
    }
    return new BearerAccessRefreshToken(userDetails.getUsername(), userDetails.getRoles(), expiresIn, accessToken,
      refreshToken, BEARER_TOKEN_TYPE);
  }
}
