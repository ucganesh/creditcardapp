package com.mifinity.cctest.repository;

import com.mifinity.cctest.model.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

/**
 * Micronaut Data CRUD repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
