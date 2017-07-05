package com.banana.infrastructure.adapters;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.repositories.IAccountRepository;

import java.util.List;

public class AccountFetcher implements IAccountFetcher {
  private IAccountRepository accountRepository;

  public AccountFetcher(IAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public List<Account> getAccountsOfUser(User user) {
    return this.accountRepository.getAccountsOfUser(user);
  }

  public Account getAccountByUserAndAccountName(User user, String accountName) { return this.accountRepository.getAccountByUserAndAccountName(user, accountName); }

  public Account getAccountByUserAndAccountSlug(User user, String accountSlug) { return this.accountRepository.getAccountByUserAndAccountSlug(user, accountSlug); }
}
