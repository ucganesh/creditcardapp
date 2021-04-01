package com.mifinity.cctest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.Instant;

/**
 * Creditcard entity - represents the cards associated to a user
 */
@Data
@Entity
@Introspected
public class Creditcard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne
  private User user;

  @NotEmpty(message = "can not be empty")
  @Size(min = 13, max = 20)
  private String number;

  @NotEmpty(message = "can not be empty")
  @Size(min = 3, max = 60)
  private String name;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date expiryDate;

  @NotEmpty(message = "can not be empty")
  @Size(min = 3, max = 60)
  private String type;

  private String status = "ACTIVE";

  private String createdBy;

  @DateCreated
  private Instant createdDate;

  private String updatedBy;

  @DateUpdated
  private Instant updatedDate;
}
