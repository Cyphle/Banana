package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SCharge;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public interface IChargeRepository {
  List<SCharge> getChargesOfUserAndAccount(SUser user, long accountId);
  SCharge createCharge(SCharge charge);
}
