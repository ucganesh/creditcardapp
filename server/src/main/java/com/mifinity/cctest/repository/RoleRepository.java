package com.mifinity.cctest.repository;

import com.mifinity.cctest.model.auth.Role;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

/**
 * Micronaut Data CRUD repository for the {@link Role} entity.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
}
