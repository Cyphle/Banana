package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SCharge;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SChargeRepository;
import com.banana.utils.Moment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChargeRepository implements IChargeRepository {
  private SChargeRepository chargeRepository;

  @Autowired
  public ChargeRepository(SChargeRepository chargeRepository) {
    this.chargeRepository = chargeRepository;
  }

  public List<SCharge> getChargesOfUserAndAccount(SUser user, long accountId) {
    return this.chargeRepository.findByUserUsernameAndAccountId(user.getUsername(), accountId);
  }

  public SCharge createCharge(SCharge charge) {
    Moment today = new Moment();
    charge.setCreationDate(today.getDate());
    charge.setUpdateDate(today.getDate());
    return this.chargeRepository.save(charge);
  }
}
