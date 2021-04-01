package com.mifinity.cctest.security;

import com.nimbusds.jose.JOSEException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.text.ParseException;

/**
 * Created by Chaitanya on 28/03/21.
 */
@MicronautTest
public class PasswordEncoderTest {

  @Inject
  PasswordEncoder passwordEncoder;

  @Test
  public void testPasswordMatch() throws JOSEException, ParseException {
    String passcode  = "admin";
    String encodedString = passwordEncoder.encode(passcode);
    Assertions.assertNotNull(encodedString);

    boolean matched = passwordEncoder.matches(passcode, encodedString);
    Assertions.assertTrue(matched);
  }

  @Test
  public void testPasswordMismatch() throws JOSEException, ParseException {
    String passcode  = "user";
    String passcode1  = "admin";
    String encodedString = passwordEncoder.encode(passcode);
    Assertions.assertNotNull(encodedString);

    boolean matched = passwordEncoder.matches(passcode1, encodedString);
    Assertions.assertFalse(matched);
  }
}
