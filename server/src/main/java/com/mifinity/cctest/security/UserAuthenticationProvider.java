package com.mifinity.cctest.security;

import com.mifinity.cctest.model.auth.Identity;
import com.mifinity.cctest.model.auth.Role;
import com.mifinity.cctest.repository.IdentityRepository;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * User credential authenticator
 */
@Singleton
public class UserAuthenticationProvider implements AuthenticationProvider {
  private final Logger log = LoggerFactory.getLogger(UserAuthenticationProvider.class);

  @Inject
  private IdentityRepository identityRepository;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Override
  public Publisher<AuthenticationResponse> authenticate(@Nullable final HttpRequest<?> httpRequest, final AuthenticationRequest<?, ?> authenticationRequest) {
    String username = authenticationRequest.getIdentity().toString();

    log.info("Authenticating {}", username);

    String lowercaseLogin = username.toLowerCase(Locale.ENGLISH);
    return Flowable.just(identityRepository.findOneByLogin(lowercaseLogin)
      .filter(identity -> passwordEncoder.matches(authenticationRequest.getSecret().toString(), identity.getPassword()))
      .map(identity -> createUserDetails(lowercaseLogin, identity))
      .orElse(new NotAuthenticatedResponse("Invalid username or password")))
      ;
  }

  private AuthenticationResponse createUserDetails(String lowercaseLogin, Identity identity) {
    List<String> grantedAuthorities = identity.getRoles().stream()
      .map(Role::getName)
      .collect(Collectors.toList());
    return new IdentityUserDetails(identity.getLogin(), grantedAuthorities, identity.getUser().getId(), identity.getUser().getName());
  }
}
