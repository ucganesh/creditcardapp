package com.mifinity.cctest.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * CreditcardDTO representing a creditcard
 */
@Data
@Builder
@AllArgsConstructor
@Introspected
public class CreditcardDTO implements Serializable {

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date expiryDate;

  public CreditcardDTO(){}


}
