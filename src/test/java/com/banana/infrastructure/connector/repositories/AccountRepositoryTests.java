package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Matchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.banana.BananaApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountRepositoryTests {
  @MockBean
  private SAccountRepository sAccountRepository;

  private AccountRepository accountRepository;
  private SUser sUser;
  private List<SAccount> sAccounts;
  private SAccount sAccount;

  @Before
  public void setup() {
    this.sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.sUser.setId(1);
    this.sAccounts = new ArrayList<>();
    this.sAccounts.add(new SAccount("Account one", 100, new Moment("2016-01-01").getDate()));
    this.sAccounts.add(new SAccount("Account two", 200, new Moment("2016-01-01").getDate()));
    this.sAccounts.get(0).setUser(this.sUser);
    this.sAccounts.get(1).setUser(this.sUser);
    this.sAccount = new SAccount("Account three", 300, new Moment("2016-01-01").getDate());
    this.sAccount.setUser(this.sUser);

    this.accountRepository = new AccountRepository(sAccountRepository);
  }

  @Test
  public void should_get_accounts_of_user() {
    given(this.sAccountRepository.findByUserUsername(any(String.class))).willReturn(this.sAccounts);

    SUser user = new SUser("Doe", "John", "johndoe");
    List<SAccount> fetchedAccounts = this.accountRepository.getAccountsOfUser(user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(100);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(200);
  }

  @Test
  public void should_get_account_of_user_from_account_id() {
    given(this.sAccountRepository.findByUserUsernameAndId(any(String.class), any(long.class))).willReturn(this.sAccount);

    SUser user = new SUser("Doe", "John", "johndoe");
    SAccount fetchedAccount = this.accountRepository.getAccountByUserAndId(user, 1);

    assertThat(fetchedAccount.getName()).isEqualTo("Account three");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(300);
  }

  @Test
  public void should_get_account_of_user_from_account_name() {
    given(this.sAccountRepository.findByUserUsernameAndName(any(String.class), any(String.class))).willReturn(this.sAccount);

    SUser user = new SUser("Doe", "John", "johndoe");
    SAccount fetchedAccount = this.accountRepository.getAccountByUserAndAccountName(user, "Account three");

    assertThat(fetchedAccount.getName()).isEqualTo("Account three");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(300);
  }

  @Test
  public void should_get_account_of_user_from_account_slug() {
    given(this.sAccountRepository.findByUserUsernameAndSlug(any(String.class), any(String.class))).willReturn(this.sAccount);

    SUser user = new SUser("Doe", "John", "johndoe");
    SAccount fetchedAccount = this.accountRepository.getAccountByUserAndAccountSlug(user, "Account three");

    assertThat(fetchedAccount.getName()).isEqualTo("Account three");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(300);
  }

  @Test
  public void should_create_account_of_user() {
    SUser user = new SUser("Doe", "John", "johndoe");
    SAccount sAccount = new SAccount("Account", 1000.0, new Moment("2016-01-01").getDate());
    sAccount.setUser(user);
    sAccount.setSlug("account");
    Moment today = new Moment();

    given(this.sAccountRepository.save(any(SAccount.class))).willReturn(sAccount);

    SAccount createdAccount = this.accountRepository.createAccount(sAccount);

    Calendar creationDateCalendar = Calendar.getInstance();
    creationDateCalendar.setTime(createdAccount.getCreationDate());
    Calendar updateDateCalendar = Calendar.getInstance();
    updateDateCalendar.setTime(createdAccount.getUpdateDate());

    assertThat(createdAccount.getName()).isEqualTo("Account");
    assertThat(createdAccount.getInitialAmount()).isEqualTo(1000.0);
    assertThat(creationDateCalendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(today.getDayOfMonth());
    assertThat(creationDateCalendar.get(Calendar.MONTH) + 1).isEqualTo(today.getMonthNumber());
    assertThat(creationDateCalendar.get(Calendar.YEAR)).isEqualTo(today.getYear());
    assertThat(updateDateCalendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(today.getDayOfMonth());
    assertThat(updateDateCalendar.get(Calendar.MONTH) + 1).isEqualTo(today.getMonthNumber());
    assertThat(updateDateCalendar.get(Calendar.YEAR)).isEqualTo(today.getYear());
  }

  @Test
  public void should_update_account_of_user() {
    SUser user = new SUser("Doe", "John", "johndoe");
    SAccount sAccount = new SAccount("Account", 1000.0, new Moment("2016-01-01").getDate());
    sAccount.setUser(user);
    sAccount.setSlug("account");
    Moment today = new Moment();

    given(this.sAccountRepository.save(any(SAccount.class))).willReturn(sAccount);

    SAccount updatedAccount = this.accountRepository.updateAccount(sAccount);

    Calendar updateDateCalendar = Calendar.getInstance();
    updateDateCalendar.setTime(updatedAccount.getUpdateDate());

    assertThat(updatedAccount.getName()).isEqualTo("Account");
    assertThat(updatedAccount.getInitialAmount()).isEqualTo(1000.0);
    assertThat(updateDateCalendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(today.getDayOfMonth());
    assertThat(updateDateCalendar.get(Calendar.MONTH) + 1).isEqualTo(today.getMonthNumber());
    assertThat(updateDateCalendar.get(Calendar.YEAR)).isEqualTo(today.getYear());
  }
}
