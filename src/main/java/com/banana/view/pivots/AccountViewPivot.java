package com.banana.view.pivots;

import com.banana.domain.models.Account;
import com.banana.view.models.AccountView;

import java.util.ArrayList;
import java.util.List;

public class AccountViewPivot {
  public static AccountView fromDomainToView(Account account) {
    return new AccountView(account.getId(), account.getName(), account.getSlug(), account.getInitialAmount(), account.getStartDate());
  }

  public static List<AccountView> fromDomainToView(List<Account> accounts) {
    List<AccountView> accountViews = new ArrayList<>();
    for (Account account : accounts) {
      accountViews.add(new AccountView(account.getId(), account.getName(), account.getSlug(), account.getInitialAmount(), account.getStartDate()));
    }
    return accountViews;
  }
}
