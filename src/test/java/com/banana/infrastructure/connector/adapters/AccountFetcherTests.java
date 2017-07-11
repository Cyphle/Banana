package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

public class AccountFetcherTests {
  private IAccountFetcher accountFetcher;
  private IAccountRepository accountRepository;
  private User user;

  @Before
  public void setup() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");

    this.accountRepository = new FakeAccountRepository();
    this.accountFetcher = new AccountFetcher(this.accountRepository);
  }

  @Test
  public void should_return_account_after_its_creation() {
    Account account = new Account(1, this.user, "Account test", "account-test", 3000.0);
    Account createdAccount = this.accountFetcher.createAccount(account);

    assertThat(createdAccount.getName()).isEqualTo("Account test");
    assertThat(createdAccount.getInitialAmount()).isEqualTo(3000.0);
    assertThat(createdAccount.getSlug()).isEqualTo("account-test");
  }

  @Test
  public void should_return_null_if_amount_is_negative() {
    Account account = new Account(1, this.user, "Account test", "account-test", -3000.0);

    try {
      Account createdAccount = this.accountFetcher.createAccount(account);
      fail("Should throw exception when initial amount of created account is negative");
    } catch (NullPointerException e) {
      assertThat(e).isNotNull();
    }
  }

  private class FakeAccountRepository implements IAccountRepository {
    public List<SAccount> getAccountsOfUser(SUser user) { return null; }
    public SAccount getAccountByUserAndAccountName(SUser user, String accountName) { return null; }
    public SAccount getAccountByUserAndAccountSlug(SUser user, String accountSlug) { return null; }
    public SAccount createAccount(SAccount account) {
      if (account.getInitialAmount() < 0)
        return null;
      else
        return account;
    }
  }
}
