package com.banana.spring.repositories;

import com.banana.spring.models.SAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SAccountRepository extends CrudRepository<SAccount, Long> {
  List<SAccount> findByUserId(long userId);
  List<SAccount> findByUserUsername(String username);
}
