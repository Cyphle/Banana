package com.banana.infrastructure.repositories;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.repositories.IAccountRepository;
import com.banana.infrastructure.pivots.AccountPivot;
import com.banana.spring.models.SAccount;
import com.banana.spring.repositories.SAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AccountRepository implements IAccountRepository {
  private SAccountRepository accountRepository;

  @Autowired
  public AccountRepository(SAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public List<Account> getAccountsOfUser(User user) {
    // get accounts
    List<SAccount> sAccounts = this.accountRepository.findByUserUsername(user.getUsername());
    // from infra to domain
    return AccountPivot.fromInfrastructureToDomain(sAccounts);
  }
}
