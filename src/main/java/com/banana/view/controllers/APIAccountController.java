package com.banana.view.controllers;

import com.banana.domain.models.Account;
import com.banana.utils.Status;
import com.banana.view.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/accounts")
public class APIAccountController {
  private AccountService accountService;

  @Autowired
  public APIAccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public Account getAccount(@RequestParam(required = false) String slug, @RequestParam(required = false) String id) {
    Account account = null;
    if (slug != null) account = this.accountService.getAccountBySlug(slug);
    if (id != null) account = this.accountService.getAccountById(Integer.parseInt(id));
    return account;
  }

  @RequestMapping(value = "/delete/{accountId}", method = RequestMethod.GET)
  public Status deleteAccount(@PathVariable long accountId) {
    return new Status(this.accountService.deleteAccount(accountId), "Delete account of id : " + accountId);
  }
}
