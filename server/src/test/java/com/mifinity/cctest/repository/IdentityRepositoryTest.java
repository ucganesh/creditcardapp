package com.mifinity.cctest.repository;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Set;

/**
 * Test to check IdentityRepository queries
 */
@MicronautTest
public class IdentityRepositoryTest {

  @Inject
  private IdentityRepository identityRepository;

  @Test
  public void shouldFetchRolesGivenLogin(){
    Set<String> roles = identityRepository.findRoleNameByLogin("admin");
    Assertions.assertNotNull(roles);
  }
}
