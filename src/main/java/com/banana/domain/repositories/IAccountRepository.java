package com.banana.domain.repositories;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;

import java.util.List;

public interface IAccountRepository {
  List<Account> getAccountsOfUser(User user);
  Account getAccountByUserAndAccountName(User user, String accountName);
  Account getAccountByUserAndAccountSlug(User user, String accountSlug);
}
