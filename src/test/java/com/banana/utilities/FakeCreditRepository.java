package com.banana.utilities;

import com.banana.infrastructure.connector.repositories.ICreditRepository;
import com.banana.infrastructure.orm.models.SCredit;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utils.Moment;

import java.util.ArrayList;
import java.util.List;

public class FakeCreditRepository implements ICreditRepository {
  public List<SCredit> getCreditsOfUserAndAccount(SUser user, long accountId) {
    List<SCredit> credits = new ArrayList<>();
    credits.add(new SCredit("Salaire", 2400, new Moment("2017-06-30").getDate()));
    credits.add(new SCredit("Maman", 500, new Moment("2017-06-25").getDate()));
    return credits;
  }

  public SCredit createCredit(SCredit credit) {
    return credit;
  }

  public SCredit updateCredit(SCredit credit) {
    return credit;
  }
}
