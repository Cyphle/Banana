package com.banana.spring.services;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.ports.AccountPort;
import com.banana.domain.ports.IAccountPort;
import com.banana.infrastructure.adapters.AccountFetcher;
import com.banana.infrastructure.repositories.AccountRepository;
import com.banana.spring.repositories.SAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
  private SAccountRepository sAccountRepository;
  private AccountRepository accountRepository;
  private IAccountFetcher accountFetcher;
  private IAccountPort banker;

  @Autowired
  public AccountService(SAccountRepository sAccountRepository) {
    this.sAccountRepository = sAccountRepository;
    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.accountFetcher = new AccountFetcher(accountRepository);
    this.banker = new AccountPort(this.accountFetcher);
  }

  public List<Account> getAccountsOfUser() {
    User user = new User("John", "Doe", "john@doe.fr");

    return this.banker.getAccountsOfUser(user);
  }
}
