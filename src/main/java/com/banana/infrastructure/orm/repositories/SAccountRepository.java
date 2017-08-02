package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SAccountRepository extends CrudRepository<SAccount, Long> {
  List<SAccount> findByUserId(long userId);
  List<SAccount> findByUserUsername(String username);
  @Query("Select a from SAccount a where a.user.username = ?1 and a.id = ?2 and a.isDeleted = false")
  SAccount findByUserUsernameAndId(String username, long id);
  @Query("Select a from SAccount a where a.user.username = ?1 and a.name like %?2% and a.isDeleted = false")
  SAccount findByUserUsernameAndName(String username, String name);
  @Query("Select a from SAccount a where a.user.username = ?1 and a.slug like %?2% and a.isDeleted = false")
  SAccount findByUserUsernameAndSlug(String username, String slug);
}
