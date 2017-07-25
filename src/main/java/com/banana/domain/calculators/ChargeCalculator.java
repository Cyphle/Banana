package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.domain.ports.ChargePort;
import com.banana.domain.validators.AccountVerifier;
import com.banana.utils.Moment;

public class ChargeCalculator implements ChargePort {
  private IAccountFetcher accountFetcher;
  private IChargeFetcher chargeFetcher;
  private AccountVerifier accountVerifier;

  public ChargeCalculator(IAccountFetcher accountFetcher, IChargeFetcher chargeFetcher) {
    this.accountFetcher = accountFetcher;
    this.chargeFetcher = chargeFetcher;

    this.accountVerifier = new AccountVerifier(this.accountFetcher);
  }

  public Charge createCharge(User user, long accountId, Charge charge) {
    this.accountVerifier.verifyAccount(user, accountId);
    charge.setAmount(Math.abs(charge.getAmount()));
    charge.setStartDate(new Moment(charge.getStartDate()).getFirstDateOfMonth().getDate());
    if (charge.getEndDate() != null)
      charge.setEndDate(new Moment(charge.getEndDate()).getLastDateOfMonth().getDate());
    return this.chargeFetcher.createCharge(accountId, charge);
  }

  public Charge updateCharge(User user, long accountId, Charge charge) {
    return null;
  }
}
