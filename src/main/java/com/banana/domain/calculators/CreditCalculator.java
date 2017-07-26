package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.ICreditFetcher;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.exceptions.UpdateException;
import com.banana.domain.models.Credit;
import com.banana.domain.models.User;
import com.banana.domain.ports.CreditPort;
import com.banana.domain.validators.AccountVerifier;

import java.util.List;
import java.util.stream.Collectors;

public class CreditCalculator implements CreditPort {
  private IAccountFetcher accountFetcher;
  private ICreditFetcher creditFetcher;
  private AccountVerifier accountVerifier;

  public CreditCalculator(IAccountFetcher accountFetcher, ICreditFetcher creditFetcher) {
    this.accountFetcher = accountFetcher;
    this.creditFetcher = creditFetcher;

    this.accountVerifier = new AccountVerifier(this.accountFetcher);
  }

  public Credit createCredit(User user, long accountId, Credit credit) {
    this.accountVerifier.verifyAccount(user, accountId);
    credit.setAmount(Math.abs(credit.getAmount()));
    return this.creditFetcher.createCredit(accountId, credit);
  }

  public Credit updateCredit(User user, long accountId, Credit credit) throws NoElementFoundException {
    this.accountVerifier.verifyAccount(user, accountId);
    List<Credit> credits = this.creditFetcher
            .getCreditsOfUserAndAccount(user, accountId)
            .stream()
            .filter(fetchedCredit -> fetchedCredit.getId() == credit.getId())
            .collect(Collectors.toList());
    if (credits.size() == 0)
      throw new UpdateException("No credit found with id " + credit.getId());
    else
      return this.creditFetcher.updateCredit(accountId, credit);
  }

  public boolean deleteCredit(User user, long accountId, Credit credit) throws NoElementFoundException {
    if (credit.getId() > 0) {
      this.accountVerifier.verifyAccount(user, accountId);
      return this.creditFetcher.deleteCredit(credit);
    } else
      throw new NoElementFoundException("No such credit");
  }
}
