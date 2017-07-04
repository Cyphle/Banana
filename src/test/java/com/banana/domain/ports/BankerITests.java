package com.banana.domain.ports;

import com.banana.BananaApplication;
import com.banana.domain.models.Account;
import com.banana.infrastructure.adapters.AccountFetcher;
import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.User;
import com.banana.infrastructure.repositories.AccountRepository;
import com.banana.spring.models.SAccount;
import com.banana.spring.models.SUser;
import com.banana.spring.repositories.SAccountRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class BankerITests {
  @MockBean
  private SAccountRepository sAccountRepository;

  private List<SAccount> accounts;
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
  }

  @Test
  public void should_get_accounts_of_user_from_fake_repository() {
    AccountRepository accountRepository = new AccountRepository(sAccountRepository);
    given(this.sAccountRepository.findByUserUsername(any(String.class))).willReturn(this.accounts);
    IAccountFetcher accountFetcher = new AccountFetcher(accountRepository);

    IBanker banker = new Banker(accountFetcher);

    List<Account> fetchedAccounts = banker.getAccountsOfUser(this.user);
    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }
}