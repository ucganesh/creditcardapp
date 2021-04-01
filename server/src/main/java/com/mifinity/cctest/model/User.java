package com.mifinity.cctest.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;

/**
 * User entity - represents the properties associated to a user
 */
@Data
@Entity
@Introspected
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "can not be empty")
  @Size(min = 3, max = 20)
  private String name;

  private String createdBy;

  @DateCreated
  private Instant createdDate;

  private String updatedBy;

  @DateUpdated
  private Instant updatedDate;
}
