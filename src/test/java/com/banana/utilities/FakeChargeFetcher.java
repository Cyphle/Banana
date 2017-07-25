package com.banana.utilities;

import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;

import java.util.List;

public class FakeChargeFetcher implements IChargeFetcher {
  public List<Charge> getChargesOfUserAndAccount(User user, long accountId) {
    return null;
  }

  public Charge createCharge(long accountId, Charge charge) {
    charge.setId(10);
    return charge;
  }

  public Charge updateCharge(long accountId, Charge charge) {
    return charge;
  }
}
