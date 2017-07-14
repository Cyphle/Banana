package com.banana.domain.adapters;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;

import java.util.List;

public interface IAccountFetcher {
  List<Account> getAccountsOfUser(User user);
  Account getAccountByUserAndId(User user, long accountId);
  Account getAccountByUserAndAccountName(User user, String accountName);
  Account getAccountByUserAndAccountSlug(User user, String accountSlug);
  Account createAccount(Account account);
  Account updateAccount(Account account);
}
