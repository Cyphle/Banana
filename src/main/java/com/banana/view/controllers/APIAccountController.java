package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.view.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
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
}
