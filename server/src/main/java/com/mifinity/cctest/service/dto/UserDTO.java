package com.mifinity.cctest.service.dto;

import com.mifinity.cctest.model.User;
import com.mifinity.cctest.model.auth.Identity;
import com.mifinity.cctest.model.auth.Role;
import com.mifinity.cctest.model.config.Constants;
import com.nimbusds.jose.shaded.json.JSONArray;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.security.authentication.Authentication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UserDTO representing a User, with their authorities
 */
@Data
@Builder
@AllArgsConstructor
@Introspected
public class UserDTO implements Serializable {

  private Long id;

  @NotNull
  @Pattern(regexp = Constants.LOGIN_REGEX)
  @Size(min = 3, max = 50)
  private String login;

  @Size(min = 8, max = 100)
  private String password;

  private String status;

  private String name;

  private String createdBy;

  private Instant createdDate;

  private String updatedBy;

  private Instant updatedDate;

  private Set<String> roles;

  public UserDTO(){}

  public UserDTO(Identity identity) {
    User user = identity.getUser();;
    this.id = user.getId();
    this.name = user.getName();
    this.login = identity.getLogin();
    this.createdBy = user.getCreatedBy();
    this.createdDate = user.getCreatedDate();
    this.updatedBy = user.getUpdatedBy();
    this.updatedDate = user.getUpdatedDate();
    this.roles = identity.getRoles().stream()
      .map(Role::getName)
      .collect(Collectors.toSet());
  }

  public UserDTO(Authentication authentication) {
    Map<String, Object> attributes = authentication.getAttributes();
    JSONArray rolesArray = (JSONArray) attributes.get("roles");
    this.id = (Long) attributes.get("userId");
    this.login = authentication.getName();
    this.roles = rolesArray.stream().map(obj -> obj.toString()).collect(Collectors.toSet());
  }

}
