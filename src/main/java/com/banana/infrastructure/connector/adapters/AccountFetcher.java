package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.pivots.AccountPivot;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public class AccountFetcher implements IAccountFetcher {
  private IAccountRepository accountRepository;
  private IUserRepository userRepository;

  public AccountFetcher(IUserRepository userRepository, IAccountRepository accountRepository) {
    this.userRepository = userRepository;
    this.accountRepository = accountRepository;
  }

  public List<Account> getAccountsOfUser(User user) {
    // from domain to infra
    SUser sUser = UserPivot.fromDomainToInfrastructure(user);
    // get data
    List<SAccount> sAccounts = this.accountRepository.getAccountsOfUser(sUser);
    // from infra to domain
    return AccountPivot.fromInfrastructureToDomain(sAccounts);
  }

  public Account getAccountByUserAndId(User user, long accountId) {
    SUser sUser = UserPivot.fromDomainToInfrastructure(user);
    SAccount sAccount = this.accountRepository.getAccountByUserAndId(sUser, accountId);
    return AccountPivot.fromInfrastructureToDomain(sAccount);
  }

  public Account getAccountByUserAndAccountName(User user, String accountName) {
    SUser sUser = UserPivot.fromDomainToInfrastructure(user);
    SAccount sAccount = this.accountRepository.getAccountByUserAndAccountName(sUser, accountName);
    return AccountPivot.fromInfrastructureToDomain(sAccount);
  }

  public Account getAccountByUserAndAccountSlug(User user, String accountSlug) {
    SUser sUser = new SUser(user.getLastname(), user.getFirstname(), user.getUsername());
    SAccount sAccount = this.accountRepository.getAccountByUserAndAccountSlug(sUser, accountSlug);
    return AccountPivot.fromInfrastructureToDomain(sAccount);
  }

  public Account createAccount(Account account) {
    SAccount sAccount = this.fromDomainToInfrastructure(account);
    // create account with repository
    SAccount createdAccount = this.accountRepository.createAccount(sAccount);
    // from infra to domain
    return AccountPivot.fromInfrastructureToDomain(createdAccount);
  }

  public Account updateAccount(Account account) {
    SAccount sAccount = this.fromDomainToInfrastructure(account);
    SAccount updatedAccount = this.accountRepository.updateAccount(sAccount);
    return AccountPivot.fromInfrastructureToDomain(updatedAccount);
  }

  public boolean deleteAccount(Account account) {
    SAccount sAccount = AccountPivot.fromDomainToInfrastructure(account);
    sAccount.setDeleted(true);
    SAccount deleteAccount = this.accountRepository.updateAccount(sAccount);
    if (deleteAccount != null && deleteAccount.isDeleted())
      return true;
    return false;
  }

  private SAccount fromDomainToInfrastructure(Account account) {
    // from domain to infra
    SAccount sAccount = AccountPivot.fromDomainToInfrastructure(account);
    // Fetched user
    SUser user = this.userRepository.getUserByUsername(account.getUser().getUsername());
    sAccount.setUser(user);
    return sAccount;
  }
}
