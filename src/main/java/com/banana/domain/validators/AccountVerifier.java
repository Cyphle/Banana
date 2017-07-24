package com.banana.domain.validators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;

public class AccountVerifier {
  private IAccountFetcher accountFetcher;

  public AccountVerifier(IAccountFetcher accountFetcher) {
    this.accountFetcher = accountFetcher;
  }

  public void verifyAccount(User user, long accountId) throws CreationException {
    Account account = this.accountFetcher.getAccountByUserAndId(user, accountId);
    if (account == null)
      throw new CreationException("No account for user and id : " + accountId);
  }
}
