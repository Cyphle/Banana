package com.banana.view.pivots;

import com.banana.domain.models.Credit;
import com.banana.view.forms.CreditForm;

public class CreditFormPivot {
  public static CreditForm fromDomainToView(Credit credit) {
    CreditForm creditForm = new CreditForm();
    if (credit.getId() > 0) creditForm.setId(credit.getId());
    creditForm.setDescription(credit.getDescription());
    creditForm.setAmount(credit.getAmount());
    creditForm.setCreditDate(credit.getCreditDate());
    return creditForm;
  }

  public static Credit fromViewToDomain(CreditForm creditForm) {
    Credit credit = new Credit(creditForm.getDescription(), creditForm.getAmount(), creditForm.getCreditDate());
    if (creditForm.getId() > 0) credit.setId(creditForm.getId());
    return credit;
  }
}
