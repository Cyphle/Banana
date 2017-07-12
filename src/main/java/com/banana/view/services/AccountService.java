package com.banana.view.services;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.ports.IAccountPort;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.infrastructure.connector.repositories.AccountRepository;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.infrastructure.connector.repositories.UserRepository;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
  private SUserRepository sUserRepository;
  private IUserRepository userRepository;
  private SAccountRepository sAccountRepository;
  private IAccountRepository accountRepository;
  private IAccountFetcher accountFetcher;
  private IAccountPort banker;

  @Autowired
  public AccountService(SUserRepository sUserRepository, SAccountRepository sAccountRepository) {
    this.sAccountRepository = sAccountRepository;
    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.sUserRepository = sUserRepository;
    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.banker = new AccountCalculator(this.accountFetcher);
  }

  public List<Account> getAccountsOfUser() {
    User user = new User(1, "John", "Doe", "john@doe.fr");

    return this.banker.getAccountsOfUser(user);
  }
}
