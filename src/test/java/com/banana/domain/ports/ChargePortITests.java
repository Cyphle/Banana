package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.calculators.ChargeCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.infrastructure.connector.adapters.ChargeFetcher;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SCharge;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SChargeRepository;
import com.banana.infrastructure.orm.repositories.SUserRepository;
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
public class ChargePortITests {
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private SAccountRepository sAccountRepository;
  @Autowired
  private SUserRepository sUserRepository;
  @Autowired
  private SChargeRepository sChargeRepository;

  private IUserRepository userRepository;
  private IAccountRepository accountRepository;
  private IAccountFetcher accountFetcher;
  private IChargeRepository chargeRepository;
  private IChargeFetcher chargeFetcher;
  private ChargePort chargePort;

  private User user;
  private SUser sUser;
  private SAccount accountOne;
  private SCharge chargeOne;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(this.sUser);

    Moment today = new Moment();
    this.accountOne = new SAccount("My Account", 100, new Moment("2016-01-01").getDate());
    this.accountOne.setSlug("my-account");
    this.accountOne.setUser(this.sUser);
    this.accountOne.setCreationDate(today.getDate());
    this.accountOne.setUpdateDate(today.getDate());
    this.entityManager.persist(this.accountOne);

    this.chargeOne = new SCharge("Internet", 40, new Moment("2017-01-01").getDate());
    this.chargeOne.setAccount(this.accountOne);
    this.entityManager.persist(this.chargeOne);

    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.chargeRepository = new ChargeRepository(this.sChargeRepository);
    this.chargeFetcher = new ChargeFetcher(this.accountRepository, this.chargeRepository);

    this.chargePort = new ChargeCalculator(this.accountFetcher, this.chargeFetcher);
  }

  @Test
  public void should_create_a_charge() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Charge newCharge = new Charge("Loyer", 1200, (new Moment("2013-02-13")).getDate());

    Charge createdCharge = this.chargePort.createCharge(this.user, myAccount.getId(), newCharge);
    Moment startDate = new Moment(createdCharge.getStartDate());
    List<Charge> charges = this.chargeFetcher.getChargesOfUserAndAccount(this.user, myAccount.getId());

    assertThat(charges.size()).isEqualTo(2);
    assertThat(createdCharge.getId()).isGreaterThan(0);
    assertThat(createdCharge.getDescription()).isEqualTo("Loyer");
    assertThat(createdCharge.getAmount()).isEqualTo(1200);
    assertThat(startDate.getDayOfMonth()).isEqualTo(13);
    assertThat(startDate.getMonthNumber()).isEqualTo(2);
    assertThat(startDate.getYear()).isEqualTo(2013);
  }

  @Test
  public void should_duplicate_charge_if_amount_is_modified() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Charge chargeToUpdate = this.chargeFetcher.getChargesOfUserAndAccount(this.user, myAccount.getId()).get(0);

    chargeToUpdate.setAmount(50);
    chargeToUpdate.setStartDate(new Moment("2017-08-10").getDate());
    Charge updatedCharge = this.chargePort.updateCharge(this.user, myAccount.getId(), chargeToUpdate);
    List<Charge> charges = this.chargeFetcher.getChargesOfUserAndAccount(this.user, myAccount.getId());
    Moment oldStartDate = new Moment(charges.get(0).getStartDate());
    Moment oldEndDate = new Moment(charges.get(0).getEndDate());
    Moment newStartDate = new Moment(updatedCharge.getStartDate());

    assertThat(charges.size()).isEqualTo(2);
    assertThat(charges.get(0).getDescription()).isEqualTo("Internet");
    assertThat(charges.get(0).getAmount()).isEqualTo(40);
    assertThat(oldStartDate.getDayOfMonth()).isEqualTo(1);
    assertThat(oldStartDate.getMonthNumber()).isEqualTo(1);
    assertThat(oldStartDate.getYear()).isEqualTo(2017);
    assertThat(oldEndDate.getDayOfMonth()).isEqualTo(31);
    assertThat(oldEndDate.getMonthNumber()).isEqualTo(7);
    assertThat(oldEndDate.getYear()).isEqualTo(2017);
    assertThat(updatedCharge.getDescription()).isEqualTo("Internet");
    assertThat(updatedCharge.getAmount()).isEqualTo(50);
    assertThat(newStartDate.getDayOfMonth()).isEqualTo(1);
    assertThat(newStartDate.getMonthNumber()).isEqualTo(8);
    assertThat(newStartDate.getYear()).isEqualTo(2017);
    assertThat(updatedCharge.getEndDate()).isNull();
  }

  @Test
  public void should_update_start_date_or_end_date_or_description_of_charge() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Charge chargeToUpdate = this.chargeFetcher.getChargesOfUserAndAccount(this.user, myAccount.getId()).get(0);

    chargeToUpdate.setStartDate(new Moment("2017-03-03").getDate());
    chargeToUpdate.setEndDate(new Moment("2018-04-20").getDate());
    Charge updatedCharge = this.chargePort.updateCharge(this.user, myAccount.getId(), chargeToUpdate);
    Moment startDate = new Moment(updatedCharge.getStartDate());
    Moment endDate = new Moment(updatedCharge.getEndDate());
    List<Charge> charges = this.chargeFetcher.getChargesOfUserAndAccount(this.user, myAccount.getId());

    assertThat(charges.size()).isEqualTo(1);
    assertThat(startDate.getDayOfMonth()).isEqualTo(3);
    assertThat(startDate.getMonthNumber()).isEqualTo(3);
    assertThat(startDate.getYear()).isEqualTo(2017);
    assertThat(endDate.getDayOfMonth()).isEqualTo(30);
    assertThat(endDate.getMonthNumber()).isEqualTo(4);
    assertThat(endDate.getYear()).isEqualTo(2018);
  }

  @Test
  public void should_update_charge_description() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Charge chargeToUpdate = this.chargeFetcher.getChargesOfUserAndAccount(this.user, myAccount.getId()).get(0);

    chargeToUpdate.setDescription("Bouygues");
    Charge updatedCharge = this.chargePort.updateCharge(this.user, myAccount.getId(), chargeToUpdate);
    List<Charge> charges = this.chargeFetcher.getChargesOfUserAndAccount(this.user, myAccount.getId());

    assertThat(charges.size()).isEqualTo(1);
    assertThat(updatedCharge.getDescription()).isEqualTo("Bouygues");
    assertThat(updatedCharge.getAmount()).isEqualTo(40);
  }

  @Test
  public void should_delete_charge() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Charge chargeToDelete = this.chargeFetcher.getChargesOfUserAndAccount(this.user, myAccount.getId()).get(0);
    chargeToDelete.setEndDate(new Moment("2017-07-20").getDate());

    Charge charge = this.chargePort.deleteCharge(this.user, myAccount.getId(), chargeToDelete);
    Moment endDate = new Moment(charge.getEndDate());

    assertThat(endDate.getDayOfMonth()).isEqualTo(31);
    assertThat(endDate.getMonthNumber()).isEqualTo(7);
    assertThat(endDate.getYear()).isEqualTo(2017);
  }
}
