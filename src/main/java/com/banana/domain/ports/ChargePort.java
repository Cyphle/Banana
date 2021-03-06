package com.banana.domain.ports;

import com.banana.domain.models.Charge;
import com.banana.domain.models.User;

public interface ChargePort {
  Charge getChargeById(User user, long accountId, long chargeId);
  Charge createCharge(User user, long accountId, Charge charge);
  Charge updateCharge(User user, long accountId, Charge charge);
  Charge deleteCharge(User user, long accountId, Charge charge);
}
