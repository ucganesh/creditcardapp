package com.mifinity.cctest.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mifinity.cctest.model.User;
import com.mifinity.cctest.model.config.Constants;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * User Identity to login to the application
 */
@Data
@Entity
@Introspected
public class Identity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Pattern(regexp = Constants.LOGIN_REGEX)
  @Size(min = 3, max = 50)
  @Column(length = 50, unique = true, nullable = false)
  private String login;

  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 100)
  @Column(length = 100, nullable = false)
  private String password;

  @NotNull
  private String status;

  @NotNull
  @ManyToOne
  private User user;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
    name = "identity_role",
    joinColumns = {@JoinColumn(name = "identity_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "role", referencedColumnName = "name")})
  @BatchSize(size=10)
  private Set<Role> roles = new HashSet<>();

  private String createdBy;

  @DateCreated
  private Instant createdDate;

  private String updatedBy;

  @DateUpdated
  private Instant updatedDate;

}
