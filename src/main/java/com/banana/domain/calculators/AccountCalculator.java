package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.ports.IAccountPort;
import com.github.slugify.Slugify;

import java.util.List;

public class AccountCalculator implements IAccountPort {
  private IAccountFetcher accountFetcher;

  public AccountCalculator(IAccountFetcher accountFetcher) {
    this.accountFetcher = accountFetcher;
  }

  public List<Account> getAccountsOfUser(User user) {
    return this.accountFetcher.getAccountsOfUser(user);
  }

  public Account getAccountByUserAndAccountName(User user, String accountName) {
    return this.accountFetcher.getAccountByUserAndAccountName(user, accountName);
  }

  public Account getAccountByUserAndAccountSlug(User user, String accountSlug) {
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
}
