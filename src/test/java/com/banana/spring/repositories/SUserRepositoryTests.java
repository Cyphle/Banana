package com.banana.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.banana.spring.models.SUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SUserRepositoryTests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SUserRepository repository;

  private SUser fakeUser;
  private SUser fakeUserTwo;

  @Before
  public void setup() {
    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.fakeUser.setUsername("johndoe");
    this.fakeUser.setSlug("fake-user");
    this.entityManager.persist(this.fakeUser);

    this.fakeUserTwo = new SUser("Foo", "Bar", "foo@bar.fr", "foobar");
    this.fakeUserTwo.setUsername("foobar");
    this.fakeUserTwo.setSlug("fake-user-two");
    this.entityManager.persist(this.fakeUserTwo);
  }

  @Test
  public void should_find_user_by_username() {
    // WHEN
    SUser user = this.repository.findByUsername("johndoe");
    // THEN
    assertThat(user.getUsername()).isEqualTo("johndoe");
    assertThat(user.getLastname()).isEqualTo("Doe");
  }

  @Test
  public void should_find_all_users_by_slug_like() {
    // WHEN
    List<SUser> users = this.repository.findAllBySlugLike("fake-user");
    // THEN
    assertThat(users.size()).isEqualTo(2);
  }
}