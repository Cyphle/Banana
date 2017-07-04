package com.banana.spring.repositories;

import com.banana.spring.models.SUserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SUserRoleRepository  extends CrudRepository<SUserRole, Long> {
  @Query("select a.role from SUserRole a, SUser b where b.username=?1 and a.userId=b.id")
  public List<String> findRoleByUserName(String username);
}
