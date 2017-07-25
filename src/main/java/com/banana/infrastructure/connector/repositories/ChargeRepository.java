package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SCharge;
import com.banana.infrastructure.orm.repositories.SChargeRepository;
import com.banana.utils.Moment;
import org.springframework.beans.factory.annotation.Autowired;

public class ChargeRepository implements IChargeRepository {
  private SChargeRepository chargeRepository;

  @Autowired
  public ChargeRepository(SChargeRepository chargeRepository) {
    this.chargeRepository = chargeRepository;
  }

  public SCharge createCharge(SCharge charge) {
    Moment today = new Moment();
    charge.setCreationDate(today.getDate());
    charge.setUpdateDate(today.getDate());
    return this.chargeRepository.save(charge);
  }
}
