package com.banana.spring.controllers;

import com.banana.domain.models.User;
import com.banana.spring.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
