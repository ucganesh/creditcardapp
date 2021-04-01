package com.mifinity.cctest.security.jwt;

import com.mifinity.cctest.security.IdentityUserDetails;
import com.nimbusds.jwt.JWTClaimsSet;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.config.TokenConfiguration;
import io.micronaut.security.token.jwt.generator.claims.ClaimsAudienceProvider;
import io.micronaut.security.token.jwt.generator.claims.JWTClaimsSetGenerator;
import io.micronaut.security.token.jwt.generator.claims.JwtIdGenerator;

import javax.inject.Singleton;

/**
 * Custom JWT claim set generator to populate the details needed of authenticated user
 */
@Singleton
@Replaces(bean = JWTClaimsSetGenerator.class)
public class CustomJwtClaimSetGenerator extends JWTClaimsSetGenerator {

  public CustomJwtClaimSetGenerator(final TokenConfiguration tokenConfiguration, @Nullable final JwtIdGenerator jwtIdGenerator, @Nullable final ClaimsAudienceProvider claimsAudienceProvider, @Nullable final ApplicationConfiguration applicationConfiguration) {
    super(tokenConfiguration, jwtIdGenerator, claimsAudienceProvider, applicationConfiguration);
  }

  @Override
  protected void populateWithUserDetails(JWTClaimsSet.Builder builder, UserDetails userDetails) {
    super.populateWithUserDetails(builder, userDetails);
    if (userDetails instanceof IdentityUserDetails) {
      builder.claim("userId", ((IdentityUserDetails)userDetails).getUserId());
      builder.claim("name", ((IdentityUserDetails)userDetails).getName());
    }
  }
}

