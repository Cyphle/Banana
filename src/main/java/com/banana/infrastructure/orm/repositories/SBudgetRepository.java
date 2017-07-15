package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SBudget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SBudgetRepository extends CrudRepository<SBudget, Long> {
  @Query("Select b from SBudget b where b.account.user.username = ?1 and b.account.id = ?2")
  List<SBudget> findByUserUsernameAndAccountId(String username, long accountId);
}
