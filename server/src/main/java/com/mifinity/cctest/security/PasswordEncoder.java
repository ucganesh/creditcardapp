package com.mifinity.cctest.security;

import com.nimbusds.jose.JOSEException;

import javax.validation.constraints.NotBlank;

/**
 * Encodes password and matches the password provided during login
 */
public interface PasswordEncoder {

  String encode(@NotBlank String rawPassword) throws JOSEException;

  boolean matches(@NotBlank String rawPassword, @NotBlank String encodedPassword);

}
