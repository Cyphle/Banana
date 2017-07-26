package com.banana.utilities;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.utils.Moment;

import java.util.ArrayList;
import java.util.List;

public class FakeAccountFetcher implements IAccountFetcher {
  public List<Account> getAccountsOfUser(User user) {
    List<Account> accounts = new ArrayList<>();
    accounts.add(new Account(1, user, "Account one", "account-one", 1000.0, new Moment("2016-01-01").getDate()));
    accounts.add(new Account(2, user, "Account two", "account-two", 2000.0, new Moment("2016-01-01").getDate()));
    return accounts;
  }

  public Account getAccountByUserAndId(User user, long accountId) {
    return null;
  }

  public Account getAccountByUserAndAccountName(User user, String accountName) {
    return new Account(3, user, "Account test", "account-test", 3000.0, new Moment("2016-01-01").getDate());
  }

  public Account getAccountByUserAndAccountSlug(User user, String accountSlug) {
    return new Account(3, user, "Account test", "account-test", 3000.0, new Moment("2016-01-01").getDate());
  }

  public Account createAccount(Account account) { return account; }

  public Account updateAccount(Account account) { return null; }
}
