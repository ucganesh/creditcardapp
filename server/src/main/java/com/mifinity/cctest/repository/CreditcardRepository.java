package com.mifinity.cctest.repository;

import com.mifinity.cctest.model.Creditcard;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Micronaut Data CRUD repository for the {@link Creditcard} entity.
 */
@Repository
public interface CreditcardRepository extends PageableRepository<Creditcard, Long> {


  Page<Creditcard> findByNumberLike(String number, Pageable pageable);

  @Query(value = "SELECT * FROM Creditcard WHERE user_id = :userId AND number LIKE :number",
    countQuery = "SELECT count(*) FROM Creditcard WHERE user_id = :userId AND number LIKE :number",
    nativeQuery = true)
  Page<Creditcard> findByUserAndNumberLike(Long userId, String number, Pageable pageable);

  Page<Creditcard> findByUserId(Long userId, Pageable pageable);

  Optional<Creditcard> findByIdAndUserId(Long id, Long userId);

  @Query("DELETE FROM creditcard where id = :id and user_id = :userId")
  void deleteById(@NonNull @NotNull Long id, Long userId);
}
