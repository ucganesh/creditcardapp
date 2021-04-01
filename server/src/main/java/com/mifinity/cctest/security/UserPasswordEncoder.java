package com.mifinity.cctest.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import io.micronaut.context.annotation.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;

/**
 * Encode and match user credentials
 */
@Singleton
public class UserPasswordEncoder implements  PasswordEncoder{
  private final Logger log = LoggerFactory.getLogger(UserPasswordEncoder.class);

  @Property(name = "micronaut.security.token.jwt.signatures.secret.generator.secret")
  private String sharedSecret;

  @Override
  public String encode(@NotBlank final String rawPassword) throws JOSEException {
    JWSSigner signer = new MACSigner(sharedSecret);
    JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(rawPassword));
    jwsObject.sign(signer);
    return jwsObject.serialize();
  }

  @Override
  public boolean matches(@NotBlank final String rawPassword, @NotBlank final String encodedPassword)  {
    try {
      String hashedPassword = encode(rawPassword);
      return hashedPassword.equals(encodedPassword);
    } catch (JOSEException joseException) {
      log.error("Could not encode", joseException);
    }
    return false;
  }
}
