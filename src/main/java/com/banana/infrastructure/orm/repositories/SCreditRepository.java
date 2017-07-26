package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SCredit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SCreditRepository extends CrudRepository<SCredit, Long> {
  @Query("Select c from SCredit c where c.account.user.username = ?1 and c.account.id = ?2")
  List<SCredit> findByUserUsernameAndAccountId(String username, long accountId);
}
