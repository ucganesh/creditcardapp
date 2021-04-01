package com.mifinity.cctest.model.auth;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User role
 */
@Data
@Entity
@Introspected
public class Role {

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;
}
