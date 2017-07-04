package com.banana.domain.ports;

import com.banana.infrastructure.adapters.AccountFetcher;
import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BankerTests {
  private User user;
  private List<Account> accounts;

  @Before
  public void setup() {
    this.user = new User("John", "Doe", "john@doe.fr");
    this.accounts = new ArrayList<>();
    this.accounts.add(new Account(1, this.user, "Account one", 1000.0));
    this.accounts.add(new Account(2, this.user, "Account two", 2000.0));
  }

  @Test
  public void should_get_accounts_of_user() {
    IBanker banker = new Banker(null);
    List<Account> fetchedAccounts = banker.getAccountsOfUser(this.user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }

  @Test
  public void should_get_accounts_of_user_from_fake_account_library() {
    IAccountFetcher accountFetcher = new AccountFetcher(null);
    accountFetcher = Mockito.spy(accountFetcher);
    Mockito.doReturn(this.accounts).when(accountFetcher).getAccountsOfUser(this.user);

    IBanker banker = new Banker(accountFetcher);

    List<Account> fetchedAccounts = banker.getAccountsOfUser(this.user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }
}
