package com.banana.view.pivots;

import com.banana.domain.models.Charge;
import com.banana.view.forms.ChargeForm;

public class ChargeFormPivot {
  public static ChargeForm fromDomainToView(Charge charge) {
    ChargeForm chargeForm = new ChargeForm();
    if (charge.getId() > 0) chargeForm.setId(charge.getId());
    chargeForm.setDescription(charge.getDescription());
    chargeForm.setAmount(charge.getAmount());
    chargeForm.setStartDate(charge.getStartDate());
    if (charge.getEndDate() != null) charge.setEndDate(charge.getEndDate());
    return chargeForm;
  }

  public static Charge fromViewToDomain(ChargeForm chargeForm) {
    Charge charge = new Charge(chargeForm.getDescription(), chargeForm.getAmount(), chargeForm.getStartDate());
    if (chargeForm.getId() > 0) charge.setId(chargeForm.getId());
    if (chargeForm.getEndDate() != null) charge.setEndDate(chargeForm.getEndDate());
    return charge;
  }
}
