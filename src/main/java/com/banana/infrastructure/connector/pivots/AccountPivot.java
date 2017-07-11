package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;

import java.util.ArrayList;
import java.util.List;

public class AccountPivot {
  public static Account fromInfrastructureToDomain(SAccount sAccount) {
    User user = UserPivot.fromInfrastructureToDomain(sAccount.getUser());
    return new Account(sAccount.getId(), user, sAccount.getName(), sAccount.getSlug(), sAccount.getInitialAmount());
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
    SUser sUser = new SUser(account.getUser().getLastname(), account.getUser().getFirstname(), account.getUser().getUsername());
    SAccount sAccount = new SAccount(account.getName(), account.getInitialAmount());
    sAccount.setSlug(account.getSlug());
    sAccount.setUser(sUser);
    sAccount.setId(account.getId());
    return sAccount;
  }
}
