package com.banana.domain.ports;

import com.banana.domain.models.Charge;

public interface ChargePort {
  Charge createCharge(Charge charge);
  Charge updateCharge(Charge charge);
}
