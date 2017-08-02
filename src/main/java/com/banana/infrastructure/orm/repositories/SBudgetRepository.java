package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SBudget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SBudgetRepository extends CrudRepository<SBudget, Long> {
  @Query("Select b from SBudget b where b.account.user.username = ?1 and b.account.id = ?2 and b.isDeleted = false")
  List<SBudget> findByUserUsernameAndAccountId(String username, long accountId);
  @Query("Select b from SBudget  b where b.account.user.username = ?1 and b.account.id = ?2 and b.id = ?3 and b.isDeleted = false")
  SBudget findByUserUsernameAndAccountIdAndBudgetId(String username, long accountId, long budgetId);
}
