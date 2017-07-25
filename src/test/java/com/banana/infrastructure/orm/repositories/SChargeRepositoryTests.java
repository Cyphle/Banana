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

import java.util.List;

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
  private SCharge chargeOne;
  private SCharge chargeTwo;

  @Before
  public void setup() {
    this.user = new SUser("Doe", "John", "john@doe.fr");
    this.entityManager.persist(this.user);

    this.account = new SAccount(this.user, "My Account", "my-account", 2000);
    this.entityManager.persist(this.account);

    this.chargeOne = new SCharge("Loyer", 1200, new Moment("2013-01-01").getDate());
    this.chargeOne.setAccount(this.account);
    this.entityManager.persist(this.chargeOne);
    this.chargeTwo = new SCharge("Internet", 40, new Moment("2017-01-01").getDate());
    this.chargeTwo.setAccount(this.account);
    this.entityManager.persist(this.chargeTwo);
  }

  @Test
  public void should_get_charges_of_account() {
    SAccount myAccount = this.accountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "my-account");

    List<SCharge> charges = this.chargeRepository.findByUserUsernameAndAccountId(this.user.getUsername(), myAccount.getId());

    assertThat(charges.size()).isEqualTo(2);
    assertThat(charges.get(0).getId()).isGreaterThan(0);
    assertThat(charges.get(0).getDescription()).isEqualTo("Loyer");
    assertThat(charges.get(0).getAmount()).isEqualTo(1200);
    assertThat(charges.get(1).getId()).isGreaterThan(0);
    assertThat(charges.get(1).getDescription()).isEqualTo("Internet");
    assertThat(charges.get(1).getAmount()).isEqualTo(40);
  }

  @Test
  public void should_save_new_charge() {
    SCharge newCharge = new SCharge("Loyer", 1200, new Moment("2013-01-01").getDate());
    newCharge.setAccount(this.account);

    SCharge createdCharge = this.chargeRepository.save(newCharge);

    assertThat(createdCharge.getId()).isGreaterThan(0);
  }
}
