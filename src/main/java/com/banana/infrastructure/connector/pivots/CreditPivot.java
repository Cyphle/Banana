package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Credit;
import com.banana.infrastructure.orm.models.SCredit;

import java.util.ArrayList;
import java.util.List;

public class CreditPivot {
  public static Credit fromInfrastructureToDomain(SCredit sCredit) {
    if (sCredit != null) {
      Credit credit = new Credit(sCredit.getDescription(), sCredit.getAmount(), sCredit.getCreditDate());
      if (sCredit.getId() > 0)
        credit.setId(sCredit.getId());
      return credit;
    } else
      return null;
  }

  public static List<Credit> fromInfrastructureToDomain(List<SCredit> sCredits) {
    List<Credit> credits = new ArrayList<>();
    for (SCredit sCredit : sCredits) {
      credits.add(CreditPivot.fromInfrastructureToDomain(sCredit));
    }
    return credits;
  }

  public static SCredit fromDomainToInfrastructure(Credit credit) {
    SCredit sCredit = new SCredit(credit.getDescription(), credit.getAmount(), credit.getCreditDate());
    if (credit.getId() > 0)
      sCredit.setId(credit.getId());
    return sCredit;
  }
}
