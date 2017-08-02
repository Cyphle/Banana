package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.domain.models.Credit;
import com.banana.view.forms.ChargeForm;
import com.banana.view.forms.CreditForm;
import com.banana.view.pivots.ChargeFormPivot;
import com.banana.view.pivots.CreditFormPivot;
import com.banana.view.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/credits")
public class CreditController {
  private CreditService creditService;

  @Autowired
  public CreditController(CreditService creditService) {
    this.creditService = creditService;
  }

  @RequestMapping(value = "/create/{accountId}", method = RequestMethod.GET)
  public String createCredit(@PathVariable long accountId, Model model) {
    CreditForm creditForm = new CreditForm();
    creditForm.setAccountId(accountId);
    model.addAttribute("creditForm", creditForm);
    return "credit/create-credit";
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createCreditPost(@Valid CreditForm creditForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "credit/create-credit";

    Account account = this.creditService.createCredit(creditForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "credit/create-credit";
  }

  @RequestMapping(value = "/update/{accountId}/{creditId}", method = RequestMethod.GET)
  public String updateCredit(@PathVariable long accountId, @PathVariable long creditId, Model model) {
    if (!model.containsAttribute("budget")) {
      CreditForm creditForm = CreditFormPivot.fromDomainToView(this.creditService.getCredit(accountId, creditId));
      creditForm.setAccountId(accountId);
      model.addAttribute("creditForm", creditForm);
    }
    return "credit/update-credit";
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String updateCreditPost(@Valid CreditForm creditForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "credit/update-credit";

    Account account = this.creditService.updateCredit(creditForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "credit/update-credit";
  }
}
