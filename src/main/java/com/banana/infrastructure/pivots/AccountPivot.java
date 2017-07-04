package com.banana.infrastructure.pivots;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.spring.models.SAccount;

import java.util.ArrayList;
import java.util.List;

public class AccountPivot {
  public static Account fromInfrastructureToDomain(SAccount sAccount) {
    User user = new User(sAccount.getUser().getLastname(), sAccount.getUser().getFirstname(), sAccount.getUser().getUsername());
    return new Account(sAccount.getId(), user, sAccount.getName(), sAccount.getInitialAmount());
  }

  public static List<Account> fromInfrastructureToDomain(List<SAccount> sAccounts) {
    List<Account> accounts = new ArrayList<>();
    for (SAccount sAccount : sAccounts) {
      Account account = AccountPivot.fromInfrastructureToDomain(sAccount);
      accounts.add(account);
    }
    return accounts;
  }
}
