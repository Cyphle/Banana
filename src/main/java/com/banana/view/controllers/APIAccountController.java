package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.utils.Status;
import com.banana.view.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class APIAccountController {
  private AccountService accountService;

  @Autowired
  public APIAccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @RequestMapping(value = "/{accountSlug}", method = RequestMethod.GET)
  public Account getAccount(@PathVariable String accountSlug) {
    Account account = this.accountService.getAccountBySlug(accountSlug);
    return account;
  }

  @RequestMapping(value = "/{accountId}", method = RequestMethod.DELETE)
  public Status deleteAccount(@PathVariable long accountId) {
    return new Status(this.accountService.deleteAccount(accountId), "Delete account of id : " + accountId);
  }
}
