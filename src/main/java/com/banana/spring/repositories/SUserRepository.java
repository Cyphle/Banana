package com.banana.spring.repositories;

import com.banana.spring.models.SUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SUserRepository extends CrudRepository<SUser, Long> {
  public SUser findByUsername(String username);
  @Query("select a from SUser a where a.slug like ?1%")
  public List<SUser> findAllBySlugLike(String slug);
}