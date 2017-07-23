package com.banana.infrastructure.orm.repositories;


import com.banana.infrastructure.orm.models.SExpense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SExpenseRepository  extends CrudRepository<SExpense, Long> {
  @Query("Select e from SExpense e where e.account.id = ?1 and e.budget is null")
  List<SExpense> findByAccountId(long accountId);
  @Query("Select e from SExpense e where e.budget.id = ?1")
  List<SExpense> findByBudgetId(long budgetId);
}
