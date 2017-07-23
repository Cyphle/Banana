package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.ports.AccountPort;
import com.github.slugify.Slugify;

import java.util.List;

public class AccountCalculator implements AccountPort {
  private IAccountFetcher accountFetcher;

  public AccountCalculator(IAccountFetcher accountFetcher) {
    this.accountFetcher = accountFetcher;
  }

  public List<Account> getAccountsOfUser(User user) {
    return this.accountFetcher.getAccountsOfUser(user);
  }

  public Account getAccountByUserAndAccountName(User user, String accountName) {
    // TODO add budgets, expenses, and all
    return this.accountFetcher.getAccountByUserAndAccountName(user, accountName);
  }

  public Account getAccountByUserAndAccountSlug(User user, String accountSlug) {
    // TODO add budgets, expenses, and all
    return this.accountFetcher.getAccountByUserAndAccountSlug(user, accountSlug);
  }

  public Account createAccount(Account account) throws CreationException {
    Account checkAccount = this.accountFetcher.getAccountByUserAndAccountSlug(account.getUser(), account.getSlug());
    if (checkAccount != null)
      throw new CreationException("Account already exists with this name");
    else {
      Slugify slg = new Slugify();
      String accountSlug = slg.slugify(account.getName());
      account.setSlug(accountSlug);
      return this.accountFetcher.createAccount(account);
    }
  }

  public Account updateAccount(Account account) {
    Account accountToUpdate = this.accountFetcher.getAccountByUserAndId(account.getUser(), account.getId());
    if (accountToUpdate == null) {
      throw new NoElementFoundException("No account found to update at id : " + account.getId());
    } else {
      accountToUpdate.setInitialAmount(account.getInitialAmount());
      accountToUpdate.setName(account.getName());
      Slugify slg = new Slugify();
      String accountSlug = slg.slugify(account.getName());
      accountToUpdate.setSlug(accountSlug);
      return this.accountFetcher.updateAccount(accountToUpdate);
    }
  }
}
