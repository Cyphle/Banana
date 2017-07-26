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
import com.banana.infrastructure.orm.models.*;
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
  private SBudget budgetOne;
  private SBudget budgetTwo;
  private SCharge chargeOne;
  private SCharge chargeTwo;
  private SExpense expenseBudgetOne;
  private SExpense expenseBudgetTwo;
  private SExpense expenseOne;
  private SExpense expenseTwo;

  @Before
  public void setUp() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");
    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(fakeUser);

    Moment today = new Moment();

    this.accountOne = new SAccount("Account one", 1000, new Moment("2016-01-01").getDate());
    this.accountOne.setSlug("account-one");
    this.accountOne.setUser(this.fakeUser);
    this.accountOne.setCreationDate(today.getDate());
    this.accountOne.setUpdateDate(today.getDate());
    this.entityManager.persist(this.accountOne);

    this.accountTwo = new SAccount("Account two", 2000, new Moment("2016-01-01").getDate());
    this.accountTwo.setUser(this.fakeUser);
    this.accountTwo.setCreationDate(today.getDate());
    this.accountTwo.setUpdateDate(today.getDate());
    this.entityManager.persist(this.accountTwo);

    this.budgetOne = new SBudget("Manger", 300, new Moment("2017-01-01").getDate());
    this.budgetOne.setAccount(this.accountOne);
    this.entityManager.persist(this.budgetOne);

    this.budgetTwo = new SBudget("Clopes", 200, new Moment("2017-02-01").getDate());
    this.budgetTwo.setAccount(this.accountOne);
    this.entityManager.persist(this.budgetTwo);

    this.chargeOne = new SCharge("Loyer", 1200, new Moment("2016-01-01").getDate());
    this.chargeOne.setAccount(this.accountOne);
    this.entityManager.persist(this.chargeOne);

    this.chargeTwo = new SCharge("Internet", 40, new Moment("2017-01-01").getDate());
    this.chargeTwo.setAccount(this.accountOne);
    this.entityManager.persist(this.chargeTwo);

    this.expenseBudgetOne = new SExpense("G20", 20, new Moment("2017-01-12").getDate());
    this.expenseBudgetOne.setBudget(this.budgetOne);
    this.entityManager.persist(this.expenseBudgetOne);

    this.expenseBudgetTwo = new SExpense("Monoprix", 30, new Moment("2017-02-13").getDate());
    this.expenseBudgetTwo.setBudget(this.budgetOne);
    this.entityManager.persist(this.expenseBudgetTwo);

    this.expenseOne = new SExpense("Bar", 50, new Moment("2017-07-20").getDate());
    this.expenseOne.setAccount(this.accountOne);
    this.entityManager.persist(this.expenseOne);

    this.expenseTwo = new SExpense("Retrait", 60, new Moment("2017-07-23").getDate());
    this.expenseTwo.setAccount(this.accountOne);
    this.entityManager.persist(this.expenseTwo);

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
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }

  @Test
  public void should_get_account_by_name_with_all_its_info() {
    Account account = this.accountPort.getAccountByUserAndAccountName(this.user, "Account one");
    Moment accountStartDate = new Moment(account.getStartDate());

    assertThat(account.getName()).isEqualTo("Account one");
    assertThat(account.getInitialAmount()).isEqualTo(1000);
    assertThat(account.getSlug()).isEqualTo("account-one");
    assertThat(account.getUser().getUsername()).isEqualTo("john@doe.fr");
    assertThat(accountStartDate.getDayOfMonth()).isEqualTo(1);
    assertThat(accountStartDate.getMonthNumber()).isEqualTo(1);
    assertThat(accountStartDate.getYear()).isEqualTo(2016);
    assertThat(account.getBudgets().size()).isEqualTo(2);
    // TODO to finish to check everything of account
  }

  @Test
  public void should_get_account_by_slug_with_all_its_info() {
    Account account = this.accountPort.getAccountByUserAndAccountSlug(this.user, "account-one");
  }

  @Test
  public void should_create_account() {
    Account accountToCreate = new Account(this.user, "Account create", 1000.0, new Moment("2016-01-20").getDate());

    Account createdAccount = this.accountPort.createAccount(accountToCreate);
    SAccount sCreatedAccount = this.sAccountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "account-create");
    Moment startDate = new Moment(createdAccount.getStartDate());

    assertThat(createdAccount).isNotNull();
    assertThat(createdAccount.getId()).isGreaterThan(0);
    assertThat(createdAccount.getName()).isEqualTo("Account create");
    assertThat(createdAccount.getSlug()).isEqualTo("account-create");
    assertThat(createdAccount.getInitialAmount()).isEqualTo(1000.0);
    assertThat(createdAccount.getUser().getUsername()).isEqualTo("john@doe.fr");
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(1);
    assertThat(startDate.getYear()).isEqualTo(2016);
    assertThat(sCreatedAccount.getId()).isEqualTo(createdAccount.getId());
    assertThat(sCreatedAccount.getInitialAmount()).isEqualTo(createdAccount.getInitialAmount());
    assertThat(sCreatedAccount.getSlug()).isEqualTo(createdAccount.getSlug());
  }

  @Test
  public void should_update_account() {
    SAccount accountToUpdate = this.sAccountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "account-one");

    Account accountWithNewData = new Account(this.user, "New account name", 2000.0, new Moment("2016-01-01").getDate());
    accountWithNewData.setId(accountToUpdate.getId());

    Account updatedAccount = this.accountPort.updateAccount(accountWithNewData);

    assertThat(updatedAccount.getId()).isEqualTo(accountToUpdate.getId());
    assertThat(updatedAccount.getName()).isEqualTo("New account name");
    assertThat(updatedAccount.getSlug()).isEqualTo("new-account-name");
    assertThat(updatedAccount.getInitialAmount()).isEqualTo(2000.0);
  }

  // TODO delete account
  /*
    - delete account
      -> set is_deleted to true for account and its budgets, its expenses, ...
   */
}