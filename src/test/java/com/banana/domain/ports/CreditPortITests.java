package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.ICreditFetcher;
import com.banana.domain.calculators.CreditCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Credit;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.infrastructure.connector.adapters.CreditFetcher;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SCredit;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SCreditRepository;
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
public class CreditPortITests {
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private SAccountRepository sAccountRepository;
  @Autowired
  private SUserRepository sUserRepository;
  @Autowired
  private SCreditRepository sCreditRepository;

  private IUserRepository userRepository;
  private IAccountRepository accountRepository;
  private IAccountFetcher accountFetcher;
  private ICreditRepository creditRepository;
  private ICreditFetcher creditFetcher;
  private CreditPort creditPort;

  private User user;
  private SUser sUser;
  private SAccount accountOne;
  private SCredit creditOne;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(this.sUser);

    this.accountOne = new SAccount("My Account", 100, new Moment("2016-01-01").getDate());
    this.accountOne.setSlug("my-account");
    this.accountOne.setUser(this.sUser);
    this.entityManager.persist(this.accountOne);

    this.creditOne = new SCredit("Maman", 1300, new Moment("2017-06-27").getDate());
    this.creditOne.setAccount(this.accountOne);
    this.entityManager.persist(this.creditOne);

    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.creditRepository = new CreditRepository(this.sCreditRepository);
    this.creditFetcher = new CreditFetcher(this.accountRepository, this.creditRepository);

    this.creditPort = new CreditCalculator(this.accountFetcher, this.creditFetcher);
  }

  @Test
  public void should_create_a_credit() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Credit newCredit = new Credit("Salaire", 2400, (new Moment("2013-06-30")).getDate());

    Credit createdCredit = this.creditPort.createCredit(this.user, myAccount.getId(), newCredit);
    Moment creditDate = new Moment(createdCredit.getCreditDate());
    List<Credit> credits = this.creditFetcher.getCreditsOfUserAndAccount(this.user, myAccount.getId());

    assertThat(credits.size()).isEqualTo(2);
    assertThat(createdCredit.getId()).isGreaterThan(0);
    assertThat(createdCredit.getDescription()).isEqualTo("Salaire");
    assertThat(createdCredit.getAmount()).isEqualTo(2400);
    assertThat(creditDate.getDayOfMonth()).isEqualTo(30);
    assertThat(creditDate.getMonthNumber()).isEqualTo(6);
    assertThat(creditDate.getYear()).isEqualTo(2013);
  }

  @Test
  public void should_update_credit() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Credit creditToUpdate = this.creditFetcher.getCreditsOfUserAndAccount(this.user, myAccount.getId()).get(0);

    creditToUpdate.setDescription("Maman update");
    creditToUpdate.setAmount(1400);
    creditToUpdate.setCreditDate(new Moment("2017-06-25").getDate());
    Credit updatedCredit = this.creditPort.updateCredit(this.user, myAccount.getId(), creditToUpdate);
    Moment newCreditDate = new Moment(updatedCredit.getCreditDate());

    assertThat(updatedCredit.getDescription()).isEqualTo("Maman update");
    assertThat(updatedCredit.getAmount()).isEqualTo(1400);
    assertThat(newCreditDate.getDayOfMonth()).isEqualTo(25);
    assertThat(newCreditDate.getMonthNumber()).isEqualTo(6);
    assertThat(newCreditDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_delete_credit() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Credit creditToDelete = this.creditFetcher.getCreditsOfUserAndAccount(this.user, myAccount.getId()).get(0);

    boolean isDeleted = this.creditPort.deleteCredit(this.user, myAccount.getId(), creditToDelete);

    assertThat(isDeleted).isTrue();
  }
}
