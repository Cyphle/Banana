package com.banana.domain.adapters;

import com.banana.domain.models.Charge;

public interface IChargeFetcher {
  Charge createCharge(long accountId, Charge charge);
}
