package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.view.forms.AccountForm;
import com.banana.view.services.AccountService;
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
@RequestMapping("/accounts")
public class AccountController {
  private AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public String getAccounts(Model model) {
    model.addAttribute("accounts", this.accountService.getAccountsOfUser());
    return "account/accounts";
  }

  @RequestMapping(value = "/create", method = RequestMethod.GET)
  public String createAccount(Model model) {
    return "account/create-account";
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createAccountPost(@Valid AccountForm accountForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors()) {
      return "account/create-account";
    }

    Account createdAccount = this.accountService.createAccount(accountForm);
    if (createdAccount != null)
      return "redirect:/accounts/" + createdAccount.getSlug();
    return "account/create-account";
  }

  @RequestMapping(value = "/update/{accountSlug}", method = RequestMethod.GET)
  public String updateAccount(@PathVariable String accountSlug, Model model) {
    if (!model.containsAttribute("account"))
      model.addAttribute("account", this.accountService.getAccountBySlug(accountSlug));
    return "account/update-account";
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String updateAccountPost(@Valid AccountForm accountForm, Errors errors) throws IllegalStateException, IOException {
    if (errors.hasErrors()) {
      return "account/update-account";
    }

    Account updatedAccount = this.accountService.updateAccount(accountForm);
    if (updatedAccount != null)
      return "redirect:/accounts/" + updatedAccount.getSlug();
    return "account/update-account";
  }
}
