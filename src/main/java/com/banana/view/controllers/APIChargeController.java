package com.banana.view.controllers;


import com.banana.utils.Status;
import com.banana.view.services.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/charges")
public class APIChargeController {
  private ChargeService chargeService;

  @Autowired
  public APIChargeController(ChargeService chargeService) {
    this.chargeService = chargeService;
  }

  /**
   *
   * @param accountId
   * @param chargeId
   * @param date in format yyyy-MM-dd
   * @return Status
   */
  @RequestMapping(value = "/{accountId}/{chargeId}", method = RequestMethod.DELETE)
  public Status deleteCharge(@PathVariable long accountId, @PathVariable long chargeId, @RequestParam(required = false) String date) {
    return new Status(this.chargeService.deleteCharge(accountId, chargeId, date), "Delete charge of id : " + chargeId);
  }
}
