package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SCharge;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SChargeRepository extends CrudRepository<SCharge, Long> {
  @Query("Select c from SCharge c where c.account.user.username = ?1 and c.account.id = ?2 and c.isDeleted = false")
  List<SCharge> findByUserUsernameAndAccountId(String username, long accountId);
}
