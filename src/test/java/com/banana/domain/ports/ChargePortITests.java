package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.calculators.ChargeCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.infrastructure.connector.repositories.AccountRepository;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.infrastructure.connector.repositories.UserRepository;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SUserRepository;
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
public class ChargePortITests {
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private SAccountRepository sAccountRepository;
  @Autowired
  private SUserRepository sUserRepository;

  private IUserRepository userRepository;
  private IAccountRepository accountRepository;
  private IAccountFetcher accountFetcher;
  private ChargePort chargePort;

  private User user;
  private SUser sUser;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(this.sUser);

    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);

    this.chargePort = new ChargeCalculator(this.accountFetcher);
  }

  @Test
  public void should_create_a_charge() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Charge newCharge = new Charge("Loyer", 1200, (new Moment("2013-02-13")).getDate());

    Charge createdCharge = this.chargePort.createCharge(this.user, myAccount.getId(), newCharge);
    Moment startDate = new Moment(createdCharge.getStartDate());

    assertThat(createdCharge.getId()).isGreaterThan(0);
    assertThat(createdCharge.getDescription()).isEqualTo("Loyer");
    assertThat(createdCharge.getAmount()).isEqualTo(1200);
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(2);
    assertThat(startDate.getYear()).isEqualTo(2013);
  }

  @Test
  public void should_duplicate_charge_if_amount_is_modified() {
    // TODO stop the charge previous month, duplicate it, start it from current month
  }

  @Test
  public void should_update_start_date_or_end_date_or_descriptionof_charge() {
    // TODO nothing special to do for this update
  }
}
