package com.banana.domain.ports;

import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.models.Account;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.AccountRepository;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.infrastructure.connector.repositories.UserRepository;
import com.banana.infrastructure.orm.models.SAccount;
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

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountPortITests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SAccountRepository sAccountRepository;

  @Autowired
  private SUserRepository sUserRepository;

  private IAccountRepository accountRepository;
  private IUserRepository userRepository;
  private IAccountFetcher accountFetcher;
  private AccountPort accountPort;
  private User user;
  private SUser fakeUser;
  private SAccount accountOne;
  private SAccount accountTwo;

  @Before
  public void setUp() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");
    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(fakeUser);

    Moment today = new Moment();

    this.accountOne = new SAccount("Account one", 100);
    this.accountOne.setSlug("account-one");
    this.accountOne.setUser(this.fakeUser);
    this.accountOne.setCreationDate(today.getDate());
    this.accountOne.setUpdateDate(today.getDate());
    this.accountTwo = new SAccount("Account two", 200);
    this.accountTwo.setUser(this.fakeUser);
    this.accountTwo.setCreationDate(today.getDate());
    this.accountTwo.setUpdateDate(today.getDate());
    this.entityManager.persist(this.accountOne);
    this.entityManager.persist(this.accountTwo);

    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.accountPort = new AccountCalculator(this.accountFetcher);
  }

  @Test
  public void should_get_accounts_of_user_from_fake_repository() {
    List<Account> fetchedAccounts = this.accountPort.getAccountsOfUser(this.user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(100.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(200.0);
  }

  @Test
  public void should_create_account() {
    Account accountToCreate = new Account(this.user, "Account create", 1000.0);

    Account createdAccount = this.accountPort.createAccount(accountToCreate);
    SAccount sCreatedAccount = this.sAccountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "account-create");

    assertThat(createdAccount).isNotNull();
    assertThat(createdAccount.getId()).isGreaterThan(0);
    assertThat(createdAccount.getName()).isEqualTo("Account create");
    assertThat(createdAccount.getSlug()).isEqualTo("account-create");
    assertThat(createdAccount.getInitialAmount()).isEqualTo(1000.0);
    assertThat(createdAccount.getUser().getUsername()).isEqualTo("john@doe.fr");
    assertThat(sCreatedAccount.getId()).isEqualTo(createdAccount.getId());
    assertThat(sCreatedAccount.getInitialAmount()).isEqualTo(createdAccount.getInitialAmount());
    assertThat(sCreatedAccount.getSlug()).isEqualTo(createdAccount.getSlug());
  }

  @Test
  public void should_update_account() {
    SAccount accountToUpdate = this.sAccountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "account-one");

    Account accountWithNewData = new Account(this.user, "New account name", 2000.0);
    accountWithNewData.setId(accountToUpdate.getId());

    Account updatedAccount = this.accountPort.updateAccount(accountWithNewData);

    assertThat(updatedAccount.getId()).isEqualTo(accountToUpdate.getId());
    assertThat(updatedAccount.getName()).isEqualTo("New account name");
    assertThat(updatedAccount.getSlug()).isEqualTo("new-account-name");
    assertThat(updatedAccount.getInitialAmount()).isEqualTo(2000.0);
  }

  /*
    - delete account
      -> set is_deleted to true for account and its budgets, its expenses, ...
   */
}