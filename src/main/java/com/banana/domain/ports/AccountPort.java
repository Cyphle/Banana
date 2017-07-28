package com.banana.domain.ports;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;

import java.util.List;

public interface AccountPort {
  List<Account> getAccountsOfUser(User user);
  Account getAccountByUserAndAccountName(User user, String accountName);
  Account getAccountByUserAndAccountSlug(User user, String accountSlug);
  Account getAccountByUserAndAccountId(User user, long accountId);
  Account createAccount(Account account);
  Account updateAccount(Account account);
  boolean deleteAccount(User user, long accountId);
}
