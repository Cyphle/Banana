package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;

import java.util.ArrayList;
import java.util.List;

public class AccountPivot {
  public static Account fromInfrastructureToDomain(SAccount sAccount) {
    if (sAccount != null) {
      User user = UserPivot.fromInfrastructureToDomain(sAccount.getUser());
      return new Account(sAccount.getId(), user, sAccount.getName(), sAccount.getSlug(), sAccount.getInitialAmount(), sAccount.getStartDate());
    } else
      return null;
  }

  public static List<Account> fromInfrastructureToDomain(List<SAccount> sAccounts) {
    List<Account> accounts = new ArrayList<>();
    for (SAccount sAccount : sAccounts) {
      Account account = AccountPivot.fromInfrastructureToDomain(sAccount);
      accounts.add(account);
    }
    return accounts;
  }

  public static SAccount fromDomainToInfrastructure(Account account) {
    if (account != null) {
      SUser sUser = UserPivot.fromDomainToInfrastructure(account.getUser());
      SAccount sAccount = new SAccount(account.getName(), account.getInitialAmount(), account.getStartDate());
      sAccount.setSlug(account.getSlug());
      sAccount.setUser(sUser);
      if (account.getId() > 0)
        sAccount.setId(account.getId());
      return sAccount;
    } else
      return null;
  }
}
