package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SCredit;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SCreditRepository;
import com.banana.utils.Moment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CreditRepository implements ICreditRepository {
  private SCreditRepository chargeRepository;

  @Autowired
  public CreditRepository(SCreditRepository chargeRepository) {
    this.chargeRepository = chargeRepository;
  }

  public List<SCredit> getCreditsOfUserAndAccount(SUser user, long accountId) {
    return this.chargeRepository.findByUserUsernameAndAccountId(user.getUsername(), accountId);
  }

  public SCredit createCredit(SCredit credit) {
    Moment today = new Moment();
    credit.setCreationDate(today.getDate());
    credit.setUpdateDate(today.getDate());
    return this.chargeRepository.save(credit);
  }

  public SCredit updateCredit(SCredit credit) {
    credit.setUpdateDate((new Moment()).getDate());
    return this.chargeRepository.save(credit);
  }
}
