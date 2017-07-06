package com.banana.domain.ports;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;

import java.util.List;

public interface IAccountPort {
  List<Account> getAccountsOfUser(User user);
  Account getAccountByUserAndAccountName(User user, String accountName);
  Account getAccountByUserAndAccountSlug(User user, String accountSlug);
  Account createAccount(User user, Account account);
}
