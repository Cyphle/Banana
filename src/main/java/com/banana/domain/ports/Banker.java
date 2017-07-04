package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;

import java.util.ArrayList;
import java.util.List;

public class Banker implements IBanker {
  private IAccountFetcher accountFetcher;

  public Banker(IAccountFetcher accountFetcher) {
    if (accountFetcher != null)
      this.accountFetcher = accountFetcher;
    else
      this.accountFetcher = new FakeAccountFetcher();
  }

  public List<Account> getAccountsOfUser(User user) {
    if (this.accountFetcher != null) {
      return this.accountFetcher.getAccountsOfUser(user);
    } else
      return null;
  }

  private class FakeAccountFetcher implements IAccountFetcher {
    public List<Account> getAccountsOfUser(User user) {
      List<Account> accounts = new ArrayList<>();
      accounts.add(new Account(1, user, "Account one", 1000.0));
      accounts.add(new Account(2, user, "Account two", 2000.0));

      return accounts;
    }
  }
}
