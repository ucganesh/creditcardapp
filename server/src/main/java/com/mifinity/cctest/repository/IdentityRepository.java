package com.mifinity.cctest.repository;

import com.mifinity.cctest.model.User;
import com.mifinity.cctest.model.auth.Identity;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

/**
 * Micronaut Data CRUD repository for the {@link Identity} entity.
 */
@Repository
public interface IdentityRepository extends CrudRepository<Identity, Long> {

  @EntityGraph(attributePaths = {"roles", "user"})
  public Optional<Identity> findOneById(Long id);

  @EntityGraph(attributePaths = {"roles", "user"})
  public Optional<Identity> findOneByLogin(String login);

  @EntityGraph(attributePaths = {"user"})
  public Optional<User> findUserByLogin(String login);

  @Query("SELECT r.name FROM Identity idy JOIN idy.roles r where idy.login = :login")
  public Set<String> findRoleNameByLogin(String login);

}
