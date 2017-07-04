package com.banana.infrastructure.adapters;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.repositories.IAccountRepository;
import com.banana.spring.models.SAccount;
import com.banana.spring.models.SUser;

import java.util.ArrayList;
import java.util.List;

public class AccountFetcher implements IAccountFetcher {
  private IAccountRepository accountRepository;

  public AccountFetcher(IAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public List<Account> getAccountsOfUser(User user) {
    return this.accountRepository.getAccountsOfUser(user);
  }
}
