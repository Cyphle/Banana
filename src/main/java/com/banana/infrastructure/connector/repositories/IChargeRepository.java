package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SCharge;

public interface IChargeRepository {
  SCharge createCharge(SCharge charge);
}
