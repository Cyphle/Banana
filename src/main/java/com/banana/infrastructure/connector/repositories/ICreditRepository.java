package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SCredit;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public interface ICreditRepository {
  List<SCredit> getCreditsOfUserAndAccount(SUser user, long accountId);
  SCredit createCredit(SCredit credit);
  SCredit updateCredit(SCredit credit);
}
