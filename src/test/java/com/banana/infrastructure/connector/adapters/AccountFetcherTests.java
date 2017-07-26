package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utilities.FakeAccountRepository;
import com.banana.utilities.FakeUserRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountFetcherTests {
  private IAccountFetcher accountFetcher;
  private IAccountRepository accountRepository;
  private IUserRepository userRepository;
  private User user;

  @Before
  public void setup() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");

    this.accountRepository = new FakeAccountRepository();
    this.userRepository = new FakeUserRepository();
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
  }

  @Test
  public void should_get_account_by_user_and_account_id() {
    Account account = this.accountFetcher.getAccountByUserAndId(this.user, 1);

    assertThat(account.getName()).isEqualTo("Account test");
    assertThat(account.getSlug()).isEqualTo("account-test");
    assertThat(account.getInitialAmount()).isEqualTo(3000.0);
  }

  @Test
  public void should_return_account_after_its_creation() {
    Account account = new Account(this.user, "Account test", 3000.0, new Moment("2016-01-01").getDate());
    account.setSlug("account-test");
    Account createdAccount = this.accountFetcher.createAccount(account);

    assertThat(createdAccount.getName()).isEqualTo("Account test");
    assertThat(createdAccount.getInitialAmount()).isEqualTo(3000.0);
    assertThat(createdAccount.getSlug()).isEqualTo("account-test");
  }

  @Test
  public void should_return_account_after_its_update() {
    Account accountToUpdate = new Account(1, this.user, "Account update", "account-update", 3000.0, new Moment("2016-01-01").getDate());
    Account updatedAccount = this.accountFetcher.updateAccount(accountToUpdate);

    assertThat(updatedAccount.getName()).isEqualTo("Account update");
    assertThat(updatedAccount.getInitialAmount()).isEqualTo(3000.0);
    assertThat(updatedAccount.getSlug()).isEqualTo("account-update");
  }
}
