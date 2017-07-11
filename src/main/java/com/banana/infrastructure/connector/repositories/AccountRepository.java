package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountRepository implements IAccountRepository {
  private SAccountRepository accountRepository;

  @Autowired
  public AccountRepository(SAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public List<SAccount> getAccountsOfUser(SUser user) {
    return this.accountRepository.findByUserUsername(user.getUsername());
  }

  public SAccount getAccountByUserAndAccountName(SUser user, String accountName) {
    return this.accountRepository.findByUserUsernameAndName(user.getUsername(), accountName);
  }

  public SAccount getAccountByUserAndAccountSlug(SUser user, String accountSlug) {
    return this.accountRepository.findByUserUsernameAndSlug(user.getUsername(), accountSlug);
  }

  public SAccount createAccount(SAccount account) {
    // Set created date and update date

    return this.accountRepository.save(account);
  }
}
