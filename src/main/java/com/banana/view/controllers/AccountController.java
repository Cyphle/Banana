package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.view.forms.AccountForm;
import com.banana.view.pivots.AccountFormPivot;
import com.banana.view.services.AccountService;
import com.banana.view.services.UserService;
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
  private UserService userService;

  @Autowired
  public AccountController(AccountService accountService, UserService userService) {
    this.accountService = accountService;
    this.userService = userService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public String getAccounts(Model model) {
    model.addAttribute("accounts", this.accountService.getAccountsOfUser());
    model.addAttribute("user", this.userService.getAuthenticatedUser());
    return "account/accounts";
  }

  @RequestMapping(value = "{accountSlug}", method = RequestMethod.GET)
  public String getAccount(@PathVariable String accountSlug, Model model) {
    model.addAttribute("accountSlug", accountSlug);
    model.addAttribute("user", this.userService.getAuthenticatedUser());
    return "account/account";
  }

  @RequestMapping(value = "/create", method = RequestMethod.GET)
  public String createAccount(Model model) {
    model.addAttribute("user", this.userService.getAuthenticatedUser());
    return "account/create-account";
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createAccountPost(@Valid AccountForm accountForm, Errors errors, Model model) throws IllegalStateException, IOException {
    if (errors.hasErrors()) {
      return "account/create-account";
    }

    model.addAttribute("user", this.userService.getAuthenticatedUser());
    Account createdAccount = this.accountService.createAccount(accountForm);
    if (createdAccount != null)
      return "redirect:/accounts/" + createdAccount.getSlug();
    return "account/create-account";
  }

  @RequestMapping(value = "/update/{accountSlug}", method = RequestMethod.GET)
  public String updateAccount(@PathVariable String accountSlug, Model model) {
    if (!model.containsAttribute("account")) {
      AccountForm accountForm = AccountFormPivot.fromDomainToView(this.accountService.getAccountBySlug(accountSlug));
      model.addAttribute("accountForm", accountForm);
    }
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
