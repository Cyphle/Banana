package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.view.forms.ChargeForm;
import com.banana.view.pivots.ChargeFormPivot;
import com.banana.view.services.ChargeService;
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
@RequestMapping("/charges")
public class ChargeController {
  private ChargeService chargeService;

  @Autowired
  public ChargeController(ChargeService chargeService) {
    this.chargeService = chargeService;
  }

  @RequestMapping(value = "/create/{accountId}", method = RequestMethod.GET)
  public String createCharge(@PathVariable long accountId, Model model) {
    ChargeForm chargeForm = new ChargeForm();
    chargeForm.setAccountId(accountId);
    model.addAttribute("chargeForm", chargeForm);
    return "charge/create-charge";
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createChargePost(@Valid ChargeForm chargeForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "charge/create-charge";

    Account account = this.chargeService.createCharge(chargeForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "charge/create-charge";
  }

  @RequestMapping(value = "/update/{accountId}/{chargeId}", method = RequestMethod.GET)
  public String updateCharge(@PathVariable long accountId, @PathVariable long chargeId, Model model) {
    if (!model.containsAttribute("budget")) {
      ChargeForm chargeForm = ChargeFormPivot.fromDomainToView(this.chargeService.getCharge(accountId, chargeId));
      chargeForm.setAccountId(accountId);
      model.addAttribute("chargeForm", chargeForm);
    }
    return "charge/update-charge";
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String updateChargePost(@Valid ChargeForm chargeForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors())
      return "charge/update-charge";

    Account account = this.chargeService.updateCharge(chargeForm);
    if (account != null)
      return "redirect:/accounts/" + account.getSlug();
    return "charge/update-charge";
  }
}
