package com.banana.utilities;

import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public class FakeAccountRepository implements IAccountRepository {
  public List<SAccount> getAccountsOfUser(SUser user) { return null; }
  public SAccount getAccountByUserAndId(SUser user, long accountId) {
    SAccount account = new SAccount("Account test", 3000.0);
    account.setSlug("account-test");
    account.setId(1);
    return account;
  }
  public SAccount getAccountByUserAndAccountName(SUser user, String accountName) { return null; }
  public SAccount getAccountByUserAndAccountSlug(SUser user, String accountSlug) { return null; }
  public SAccount createAccount(SAccount account) {
    return account;
  }

  public SAccount updateAccount(SAccount account) { return account; }
}
