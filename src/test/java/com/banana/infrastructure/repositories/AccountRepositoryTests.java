package com.banana.infrastructure.repositories;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.spring.models.SAccount;
import com.banana.spring.models.SUser;
import com.banana.spring.repositories.SAccountRepository;
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
    this.sAccounts.add(new SAccount("Account one", 100));
    this.sAccounts.add(new SAccount("Account two", 200));
    this.sAccounts.get(0).setUser(this.sUser);
    this.sAccounts.get(1).setUser(this.sUser);
    this.sAccount = new SAccount("Account three", 300);
    this.sAccount.setUser(this.sUser);

    this.accountRepository = new AccountRepository(sAccountRepository);
  }

  @Test
  public void should_get_accounts_of_user_in_domain_format() {
    given(this.sAccountRepository.findByUserUsername(any(String.class))).willReturn(this.sAccounts);

    User user = new User("Doe", "John", "johndoe");
    List<Account> fetchedAccounts = this.accountRepository.getAccountsOfUser(user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(100);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(200);
  }

  @Test
  public void should_get_account_of_user_from_account_name_in_domain_format() {
    given(this.sAccountRepository.findByUserUsernameAndName(any(String.class), any(String.class))).willReturn(this.sAccount);

    User user = new User("Doe", "John", "johndoe");
    Account fetchedAccount = this.accountRepository.getAccountByUserAndAccountName(user, "Account three");

    assertThat(fetchedAccount.getName()).isEqualTo("Account three");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(300);
  }

  @Test
  public void should_get_account_of_user_from_account_slug_in_domain_format() {
    given(this.sAccountRepository.findByUserUsernameAndSlug(any(String.class), any(String.class))).willReturn(this.sAccount);

    User user = new User("Doe", "John", "johndoe");
    Account fetchedAccount = this.accountRepository.getAccountByUserAndAccountSlug(user, "Account three");

    assertThat(fetchedAccount.getName()).isEqualTo("Account three");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(300);
  }
}
