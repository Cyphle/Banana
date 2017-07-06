package com.banana.domain.ports;

import com.banana.BananaApplication;
import com.banana.domain.models.Account;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.AccountRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountPortITests {
  @MockBean
  private SAccountRepository sAccountRepository;

  private List<SAccount> accounts;
  private SAccount account;
  private SUser suser;
  private User user;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.suser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.accounts = new ArrayList<>();
    SAccount accountOne = new SAccount("Account one", 1000.0);
    accountOne.setId(1);
    accountOne.setUser(this.suser);
    this.accounts.add(accountOne);
    SAccount accountTwo = new SAccount("Account two", 2000.0);
    accountTwo.setId(2);
    accountTwo.setUser(this.suser);
    this.accounts.add(accountTwo);
    this.account = new SAccount("Account three", 3000.0);
    this.account.setSlug("account-three");
    this.account.setUser(this.suser);
  }

  @Test
  public void should_get_accounts_of_user_from_fake_repository() {
    AccountRepository accountRepository = new AccountRepository(sAccountRepository);
    given(this.sAccountRepository.findByUserUsername(any(String.class))).willReturn(this.accounts);
    IAccountFetcher accountFetcher = new AccountFetcher(accountRepository);

    IAccountPort banker = new AccountPort(accountFetcher);

    List<Account> fetchedAccounts = banker.getAccountsOfUser(this.user);
    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }

  @Test
  public void should_get_an_account_of_user_by_slug_from_fake_repository() {
    AccountRepository accountRepository = new AccountRepository(sAccountRepository);
    given(this.sAccountRepository.findByUserUsernameAndSlug(any(String.class), any(String.class))).willReturn(this.account);
    IAccountFetcher accountFetcher = new AccountFetcher(accountRepository);

    IAccountPort aPort = new AccountPort(accountFetcher);

    Account fetchedAccount = aPort.getAccountByUserAndAccountSlug(this.user, "account-three");
    assertThat(fetchedAccount.getName()).isEqualTo("Account three");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(3000.0);
  }

  @Test
  public void should_not_get_account_if_user_and_slug_do_not_match() {
    AccountRepository accountRepository = new AccountRepository(sAccountRepository);
    given(this.sAccountRepository.findByUserUsernameAndSlug("john@doe.fr", "account-three")).willReturn(this.account);
    IAccountFetcher accountFetcher = new AccountFetcher(accountRepository);

    IAccountPort aPort = new AccountPort(accountFetcher);
    User badUser = new User("Hello", "World", "hello@world.fr");

    try {
      Account fetchedAccount = aPort.getAccountByUserAndAccountSlug(badUser, "account-three");
      fail("Should throw exception when charge has negative amount");
    } catch (NullPointerException e) {
      assertThat(e).isNotNull();
    }
  }
}