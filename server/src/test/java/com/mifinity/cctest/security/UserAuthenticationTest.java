package com.mifinity.cctest.security;

import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

/**
 * Created by Chaitanya on 28/03/21.
 */
@MicronautTest
public class UserAuthenticationTest {

  @Inject
  private UserAuthenticationProvider authenticationProvider;

  @Test
  public void shouldFindASuccessfulLogin() {
    AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest<Object, Object>() {
      public Object getIdentity() {
        return "admin";
      }
      public Object getSecret() {
        return "admin";
      }
    })).blockingFirst();
    Assertions.assertNotNull(response);
    Assertions.assertTrue(((UserDetails) response).getUsername().equals("admin"));
  }

  @Test
  public void shouldFailLoginAttempt() {
    AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest<Object, Object>() {
      public Object getIdentity() {
        return "user";
      }
      public Object getSecret() {
        return "admin";
      }
    })).blockingFirst();
    Assertions.assertNotNull(response);
    Assertions.assertFalse(response.isAuthenticated());
  }
}
