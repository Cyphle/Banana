package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public interface IAccountRepository {
  List<SAccount> getAccountsOfUser(SUser user);
  SAccount getAccountById(long acocuntId);
  SAccount getAccountByUserAndId(SUser user, long accountId);
  SAccount getAccountByUserAndAccountName(SUser user, String accountName);
  SAccount getAccountByUserAndAccountSlug(SUser user, String accountSlug);
  SAccount createAccount(SAccount account);
  SAccount updateAccount(SAccount account);
}
