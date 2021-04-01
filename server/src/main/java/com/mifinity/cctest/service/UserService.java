package com.mifinity.cctest.service;

import com.mifinity.cctest.exceptions.AccountException;
import com.mifinity.cctest.model.User;
import com.mifinity.cctest.model.auth.Identity;
import com.mifinity.cctest.model.auth.Role;
import com.mifinity.cctest.repository.IdentityRepository;
import com.mifinity.cctest.repository.RoleRepository;
import com.mifinity.cctest.repository.UserRepository;
import com.mifinity.cctest.security.AuthoritiesConstants;
import com.mifinity.cctest.security.PasswordEncoder;
import com.mifinity.cctest.service.dto.UserDTO;
import com.nimbusds.jose.JOSEException;
import io.micronaut.transaction.annotation.ReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service for the {@link User} entity
 */

@Singleton
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Inject
  private UserRepository userRepository;

  @Inject
  private IdentityRepository identityRepository;

  @Inject
  private RoleRepository roleRepository;

  @Inject
  private PasswordEncoder passwordEncoder;

  public User registerUser(@Valid @NotNull UserDTO userDTO, @NotBlank String password){
    identityRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingIdentity -> {
      throw new ConstraintViolationException("Login already exists", null);
    });

    User newUser = new User();
    String encryptedPassword = null;
    try {
      encryptedPassword = passwordEncoder.encode(password);
    } catch (JOSEException joseException) {
      throw new AccountException("Password encoding failed", joseException);
    }
    newUser.setName(Optional.ofNullable(userDTO.getName()).orElse(userDTO.getLogin()));
    newUser.setCreatedBy("user");
    newUser = userRepository.save(newUser);

    Identity identity = new Identity();
    identity.setUser(newUser);

    identity.setLogin(userDTO.getLogin().toLowerCase());
    identity.setPassword(encryptedPassword);
    identity.setStatus("ACTIVE");

    Set<Role> roles = new HashSet<>();
    roleRepository.findById(AuthoritiesConstants.USER).ifPresent(roles::add);
    identity.setRoles(roles);

    identity.setCreatedBy("user");
    identityRepository.save(identity);

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  @ReadOnly
  public Optional<UserDTO> getUserByLogin(String login) {
    Optional<Identity> identity = identityRepository.findOneByLogin(login);
    return identity.map(identity1 -> new UserDTO());
  }

  @ReadOnly
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }
}

