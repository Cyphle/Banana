package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.ports.AccountPort;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.utilities.FakeAccountFetcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;

public class AccountCalculatorTests {
  private User user;
  private List<Account> accounts;
  private Account account;

  @Before
  public void setup() {
    this.user = new User(1, "John", "Doe", "john@doe.fr");
    this.accounts = new ArrayList<>();
    this.accounts.add(new Account(1, this.user, "Account one", "account-one", 1000.0));
    this.accounts.add(new Account(2, this.user, "Account two", "account-two", 2000.0));
    this.account = new Account(3, this.user, "Account test", "account-three", 3000.0);
  }

  @Test
  public void should_get_accounts_of_user() {
    AccountPort banker = new AccountCalculator(new FakeAccountFetcher());
    List<Account> fetchedAccounts = banker.getAccountsOfUser(this.user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }

  @Test
  public void should_get_accounts_of_user_from_fake_account_library() {
    IAccountFetcher accountFetcher = new AccountFetcher(null, null);
    accountFetcher = Mockito.spy(accountFetcher);
    Mockito.doReturn(this.accounts).when(accountFetcher).getAccountsOfUser(this.user);

    AccountPort banker = new AccountCalculator(accountFetcher);

    List<Account> fetchedAccounts = banker.getAccountsOfUser(this.user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }

  @Test
  public void should_get_an_account_of_user_by_name() {
    IAccountFetcher accountFetcher = new AccountFetcher(null,null);
    accountFetcher = Mockito.spy(accountFetcher);
    Mockito.doReturn(this.account).when(accountFetcher).getAccountByUserAndAccountName(this.user, "Account test");

    AccountPort banker = new AccountCalculator(accountFetcher);

    Account fetchedAccount = banker.getAccountByUserAndAccountName(this.user, "Account test");

    assertThat(fetchedAccount.getName()).isEqualTo("Account test");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(3000.0);
  }

  @Test
  public void should_get_an_account_of_user_by_account_slug() {
    IAccountFetcher accountFetcher = new AccountFetcher(null,null);
    accountFetcher = Mockito.spy(accountFetcher);
    Mockito.doReturn(this.account).when(accountFetcher).getAccountByUserAndAccountSlug(this.user, "account-test");

    AccountPort banker = new AccountCalculator(accountFetcher);

    Account fetchedAccount = banker.getAccountByUserAndAccountSlug(this.user, "account-test");

    assertThat(fetchedAccount.getName()).isEqualTo("Account test");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(3000.0);
  }

  @Test
  public void should_create_and_account_for_user() {
    IAccountFetcher accountFetcher = new AccountFetcher(null, null);
    accountFetcher = Mockito.spy(accountFetcher);

    Account accountToCreate = new Account(this.user, "Account create", 1500.0);
    Mockito.doReturn(accountToCreate).when(accountFetcher).createAccount(any(Account.class));
    Mockito.doReturn(null).when(accountFetcher).getAccountByUserAndAccountSlug(accountToCreate.getUser(), accountToCreate.getSlug());

    AccountPort aPort = new AccountCalculator(accountFetcher);

    Account createdAccount = aPort.createAccount(accountToCreate);

    assertThat(createdAccount).isNotNull();
    assertThat(createdAccount.getName()).isEqualTo("Account create");
    assertThat(createdAccount.getInitialAmount()).isEqualTo(1500.0);
    assertThat(createdAccount.getSlug()).isEqualTo("account-create");
  }

  @Test
  public void should_throw_creation_exception_if_account_already_exists() {
    IAccountFetcher accountFetcher = new AccountFetcher(null, null);
    accountFetcher = Mockito.spy(accountFetcher);

    Account accountToCreate = new Account(1, this.user, "Account create", "account-create", 1500.0);
    Mockito.doReturn(accountToCreate).when(accountFetcher).createAccount(any(Account.class));
    Mockito.doReturn(accountToCreate).when(accountFetcher).getAccountByUserAndAccountSlug(accountToCreate.getUser(), accountToCreate.getSlug());

    AccountPort aPort = new AccountCalculator(accountFetcher);

    try {
      Account createdAccount = aPort.createAccount(accountToCreate);
      fail("Should throw exception when account already exists");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("Account already exists with this name");
    }
  }

  @Test
  public void should_update_account_of_user() {
    IAccountFetcher accountFetcher = new AccountFetcher(null, null);
    accountFetcher = Mockito.spy(accountFetcher);

    Account accountToUpdate = new Account(this.user, "Account to update", 1500.0);
    accountToUpdate.setId(1);

    Mockito.doReturn(accountToUpdate).when(accountFetcher).updateAccount(any(Account.class));
    Mockito.doReturn(accountToUpdate).when(accountFetcher).getAccountByUserAndId(accountToUpdate.getUser(), accountToUpdate.getId());

    AccountPort aPort = new AccountCalculator(accountFetcher);

    Account updatedAccount = aPort.updateAccount(accountToUpdate);

    assertThat(updatedAccount).isNotNull();
    assertThat(updatedAccount.getName()).isEqualTo("Account to update");
    assertThat(updatedAccount.getInitialAmount()).isEqualTo(1500.0);
    assertThat(updatedAccount.getSlug()).isEqualTo("account-to-update");
  }
}
