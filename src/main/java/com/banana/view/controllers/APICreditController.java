package com.banana.view.controllers;

import com.banana.utils.Status;
import com.banana.view.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credits")
public class APICreditController {
  private CreditService creditService;

  @Autowired
  public APICreditController(CreditService creditService) {
    this.creditService = creditService;
  }

  @RequestMapping(value = "/delete/{accountId}/{creditId}", method = RequestMethod.GET)
  public Status deleteCredit(@PathVariable long accountId, @PathVariable long creditId) {
    return new Status(this.creditService.deleteCredit(accountId, creditId), "Delete credit of id : " + creditId);
  }
}
