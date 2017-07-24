package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.domain.ports.ChargePort;

public class ChargeCalculator implements ChargePort {
  private IAccountFetcher accountFetcher;

  public ChargeCalculator(IAccountFetcher accountFetcher) {
    this.accountFetcher = accountFetcher;
  }

  public Charge createCharge(User user, long accountId, Charge charge) {
    return null;
  }

  public Charge updateCharge(User user, long accountId, Charge charge) {
    return null;
  }
}
