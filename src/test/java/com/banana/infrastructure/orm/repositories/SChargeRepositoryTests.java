package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SCharge;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SChargeRepositoryTests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SChargeRepository chargeRepository;

  @Autowired
  private SAccountRepository accountRepository;

  private SUser user;
  private SAccount account;

  @Before
  public void setup() {
    this.user = new SUser("Doe", "John", "john@doe.fr");
    this.entityManager.persist(this.user);

    this.account = new SAccount(this.user, "My Account", "my-account", 2000);
    this.entityManager.persist(this.account);
  }

  @Test
  public void should_save_new_charge() {
    SCharge newCharge = new SCharge("Loyer", 1200, new Moment("2013-01-01").getDate());
    newCharge.setAccount(this.account);

    SCharge createdCharge = this.chargeRepository.save(newCharge);

    assertThat(createdCharge.getId()).isGreaterThan(0);
  }
}
