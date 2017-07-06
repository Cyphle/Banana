package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;

import java.util.ArrayList;
import java.util.List;

public class AccountPort implements IAccountPort {
  private IAccountFetcher accountFetcher;

  public AccountPort(IAccountFetcher accountFetcher) {
    if (accountFetcher != null)
      this.accountFetcher = accountFetcher;
    else
      this.accountFetcher = new FakeAccountFetcher();
  }

  public List<Account> getAccountsOfUser(User user) {
    return this.accountFetcher.getAccountsOfUser(user);
  }

  public Account getAccountByUserAndAccountName(User user, String accountName) {
    return this.accountFetcher.getAccountByUserAndAccountName(user, accountName);
  }

  public Account getAccountByUserAndAccountSlug(User user, String accountSlug) {
    return this.accountFetcher.getAccountByUserAndAccountSlug(user, accountSlug);
  }

  public Account createAccount(User user, Account account) {
    // LOGIC SHOULD GO ELSEWHERE !!! (like validation and in fact all the steps below)

    // get account by slug

    // if not exists and valid (positive amount, etc...), create

    // if exists, throw CreationException, message = "Account already exists, cannot create"
    return null;
  }

  private class FakeAccountFetcher implements IAccountFetcher {
    public List<Account> getAccountsOfUser(User user) {
      List<Account> accounts = new ArrayList<>();
      accounts.add(new Account(1, user, "Account one", "account-one", 1000.0));
      accounts.add(new Account(2, user, "Account two", "account-two", 2000.0));

      return accounts;
    }

    public Account getAccountByUserAndAccountName(User user, String accountName) {
      return new Account(3, user, "Account test", "account-test", 3000.0);
    }

    public Account getAccountByUserAndAccountSlug(User user, String accountSlug) {
      return new Account(3, user, "Account test", "account-test", 3000.0);
    }
  }
}
