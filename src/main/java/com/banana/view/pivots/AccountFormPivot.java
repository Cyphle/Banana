package com.banana.view.pivots;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.view.forms.AccountForm;

public class AccountFormPivot {
  public static AccountForm fromDomainToView(Account account) {
    AccountForm accountForm = new AccountForm();
    accountForm.setName(account.getName());
    accountForm.setInitialAmount(account.getInitialAmount());
    accountForm.setStartDate(account.getStartDate());
    if (account.getId() > 0) accountForm.setId(account.getId());
    return accountForm;
  }

  public static Account fromViewToDomain(User user, AccountForm accountForm) {
    Account account = new Account(user, accountForm.getName(), accountForm.getInitialAmount(), accountForm.getStartDate());
    if (accountForm.getId() > 0) account.setId(accountForm.getId());
    return account;
  }
}
