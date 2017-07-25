package com.banana.domain.adapters;

import com.banana.domain.models.Charge;
import com.banana.domain.models.User;

import java.util.List;

public interface IChargeFetcher {
  List<Charge> getChargesOfUserAndAccount(User user, long accountId);
  Charge createCharge(long accountId, Charge charge);
  Charge updateCharge(long accountId, Charge charge);
  boolean deleteCharge(Charge charge);
}
