package com.mifinity.cctest.controller;

import com.mifinity.cctest.exceptions.BadRequestException;
import com.mifinity.cctest.model.User;
import com.mifinity.cctest.security.AuthoritiesConstants;
import com.mifinity.cctest.service.UserService;
import com.mifinity.cctest.service.dto.UserDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.authentication.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.security.Principal;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@Controller("/api")
public class UserController {

  private Logger log = LoggerFactory.getLogger(UserController.class);

  @Inject
  private UserService userService;

  /**
   * {@code POST  /register} : register the user.
   *
   * @param userDTO the user model.
   */
  @Post("/register")
  @Status(HttpStatus.CREATED)
  @ExecuteOn(TaskExecutors.IO)
  @Consumes(MediaType.APPLICATION_JSON)
  public void registerAccount(@Body UserDTO userDTO) {
    log.debug("Rest: request to register user received");
    User user = userService.registerUser(userDTO, userDTO.getPassword());
    log.info("Rest: completed user registration id: {}", user.getId());
  }

  /**
   * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
   *
   * @param request the HTTP request.
   * @return the login if the user is authenticated.
   */
  @Get("/authenticate")
  @ExecuteOn(TaskExecutors.IO)
  public Optional<String> isAuthenticated(HttpRequest<?> request) {
    log.debug("REST request to check if the current user is authenticated");
    return request.getUserPrincipal().map(Principal::getName);
  }

  /**
   * {@code GET  /users/:id} : get the user by "id".
   *
   * @param id the id of the user to retrieve.
   * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the user, or with status {@code 404 (Not Found)}.
   */
  @Get("/users/{id}")
  @ExecuteOn(TaskExecutors.IO)
  public Optional<User> getUser(Authentication authentication, @PathVariable Long id) {
    log.info("REST request to get user : {} by {}", id, authentication.getName());
    UserDTO userDTO =  new UserDTO(authentication);
    if (userDTO.getRoles().contains(AuthoritiesConstants.USER) &&
      userDTO.getId().compareTo(id) != 0) {
      throw new BadRequestException("Unauthorised");
    }
    return userService.getUserById(id);
  }
}
