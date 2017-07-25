package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Charge;
import com.banana.infrastructure.orm.models.SCharge;

import java.util.ArrayList;
import java.util.List;

public class ChargePivot {
  public static Charge fromInfrastructureToDomain(SCharge sCharge) {
    Charge charge = new Charge(sCharge.getDescription(), sCharge.getAmount(), sCharge.getStartDate());
    if (sCharge.getId() > 0)
      charge.setId(sCharge.getId());
    if (sCharge.getEndDate() != null)
      charge.setEndDate(sCharge.getEndDate());
    return charge;
  }

  public static List<Charge> fromInfrastructureToDomain(List<SCharge> sCharges) {
    List<Charge> charges = new ArrayList<>();
    for (SCharge sCharge : sCharges) {
      Charge charge = ChargePivot.fromInfrastructureToDomain(sCharge);
      charges.add(charge);
    }
    return charges;
  }

  public static SCharge fromDomainToInfrastructure(Charge charge) {
    SCharge sCharge = new SCharge(charge.getDescription(), charge.getAmount(), charge.getStartDate());
    if (charge.getId() > 0)
      sCharge.setId(charge.getId());
    if (charge.getEndDate() != null)
      sCharge.setEndDate(charge.getEndDate());
    return sCharge;
  }
}
