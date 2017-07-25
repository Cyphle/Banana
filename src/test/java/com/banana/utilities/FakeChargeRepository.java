package com.banana.utilities;

import com.banana.infrastructure.connector.repositories.IChargeRepository;
import com.banana.infrastructure.orm.models.SCharge;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utils.Moment;

import java.util.ArrayList;
import java.util.List;

public class FakeChargeRepository implements IChargeRepository {
  public List<SCharge> getChargesOfUserAndAccount(SUser user, long accountId) {
    List<SCharge> charges = new ArrayList<>();
    charges.add(new SCharge("Loyer", 1200, new Moment("2013-01-01").getDate()));
    charges.add(new SCharge("Internet", 40, new Moment("2017-01-01").getDate()));
    return charges;
  }

  public SCharge createCharge(SCharge charge) {
    return charge;
  }
}
