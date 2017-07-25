package com.banana.domain.ports;

import com.banana.domain.models.Charge;
import com.banana.domain.models.User;

public interface ChargePort {
  Charge createCharge(User user, long accountId, Charge charge);
  Charge updateCharge(User user, long accountId, Charge charge);
  boolean deleteCharge(User user, long accountId, Charge charge);
}
