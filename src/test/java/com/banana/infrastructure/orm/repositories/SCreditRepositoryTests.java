package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SCredit;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SCreditRepositoryTests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SCreditRepository creditRepository;

  @Autowired
  private SAccountRepository accountRepository;

  private SUser user;
  private SAccount account;
  private SCredit creditOne;
  private SCredit creditTwo;

  @Before
  public void setup() {
    this.user = new SUser("Doe", "John", "john@doe.fr");
    this.entityManager.persist(this.user);

    this.account = new SAccount(this.user, "My Account", "my-account", 2000, new Moment("2016-01-01").getDate());
    this.entityManager.persist(this.account);

    this.creditOne = new SCredit("Salaire", 2400, new Moment("2017-06-30").getDate());
    this.creditOne.setAccount(this.account);
    this.entityManager.persist(this.creditOne);
    this.creditTwo = new SCredit("Maman", 500, new Moment("2017-06-25").getDate());
    this.creditTwo.setAccount(this.account);
    this.entityManager.persist(this.creditTwo);
  }

  @Test
  public void should_get_credits_of_account() {
    SAccount myAccount = this.accountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "my-account");

    List<SCredit> credits = this.creditRepository.findByUserUsernameAndAccountId(this.user.getUsername(), myAccount.getId());

    assertThat(credits.size()).isEqualTo(2);
    assertThat(credits.get(0).getId()).isGreaterThan(0);
    assertThat(credits.get(0).getDescription()).isEqualTo("Salaire");
    assertThat(credits.get(0).getAmount()).isEqualTo(2400);
    assertThat(credits.get(1).getId()).isGreaterThan(0);
    assertThat(credits.get(1).getDescription()).isEqualTo("Maman");
    assertThat(credits.get(1).getAmount()).isEqualTo(500);
  }

  @Test
  public void should_save_new_credit() {
    SCredit newCredit = new SCredit("Livret A", 500, new Moment("2017-07-20").getDate());
    newCredit.setAccount(this.account);

    SCredit createdCredit = this.creditRepository.save(newCredit);

    assertThat(createdCredit.getId()).isGreaterThan(0);
  }
}
