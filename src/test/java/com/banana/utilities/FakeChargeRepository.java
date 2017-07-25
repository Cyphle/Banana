package com.banana.utilities;

import com.banana.infrastructure.connector.repositories.IChargeRepository;
import com.banana.infrastructure.orm.models.SCharge;

public class FakeChargeRepository implements IChargeRepository {
  public SCharge createCharge(SCharge charge) {
    return charge;
  }
}
