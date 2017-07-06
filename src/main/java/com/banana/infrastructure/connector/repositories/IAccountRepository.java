package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public interface IAccountRepository {
  List<SAccount> getAccountsOfUser(SUser user);
  SAccount getAccountByUserAndAccountName(SUser user, String accountName);
  SAccount getAccountByUserAndAccountSlug(SUser user, String accountSlug);
}
