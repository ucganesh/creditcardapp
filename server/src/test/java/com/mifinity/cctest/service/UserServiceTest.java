package com.mifinity.cctest.service;

import com.mifinity.cctest.model.User;
import com.mifinity.cctest.security.UserAuthenticationProvider;
import com.mifinity.cctest.service.dto.UserDTO;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
public class UserServiceTest {

  @Inject
  private UserService userService;

  @Inject
  private UserAuthenticationProvider authenticationProvider;

  @Test
  public void shouldCreateNewUser(){
    String username = "ucganesh";
    String password = "Und1ll@";

    UserDTO userDTO = UserDTO.builder()
      .name("Chaitanya")
      .login(username)
      .build();

    User user = userService.registerUser(userDTO, password);
    Assertions.assertNotNull(user);

    shouldFindASuccessfulLogin(username, password);
  }

  private void shouldFindASuccessfulLogin(String username, String password) {
    AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest<Object, Object>() {
      public Object getIdentity() {
        return username;
      }
      public Object getSecret() {
        return password;
      }
    })).blockingFirst();
    Assertions.assertNotNull(response);
    Assertions.assertTrue(((UserDetails) response).getUsername().equals(username));
  }
}
