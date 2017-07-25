package com.banana.utilities;

import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.models.Charge;

public class FakeChargeFetcher implements IChargeFetcher {
  public Charge createCharge(long accountId, Charge charge) {
    charge.setId(1);
    return charge;
  }
}
