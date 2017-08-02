package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.exceptions.UpdateException;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.domain.ports.ChargePort;
import com.banana.domain.validators.AccountVerifier;
import com.banana.utils.Moment;

import java.util.List;
import java.util.stream.Collectors;

public class ChargeCalculator implements ChargePort {
  private IAccountFetcher accountFetcher;
  private IChargeFetcher chargeFetcher;
  private AccountVerifier accountVerifier;

  public ChargeCalculator(IAccountFetcher accountFetcher, IChargeFetcher chargeFetcher) {
    this.accountFetcher = accountFetcher;
    this.chargeFetcher = chargeFetcher;

    this.accountVerifier = new AccountVerifier(this.accountFetcher);
  }

  public Charge getChargeById(User user, long accountId, long chargeId) {
    this.accountVerifier.verifyAccount(user, accountId);
    return this.chargeFetcher.getChargesOfUserAndAccount(user, accountId).stream().filter(charge -> charge.getId() == chargeId).collect(Collectors.toList()).get(0);
  }

  public Charge createCharge(User user, long accountId, Charge charge) {
    this.accountVerifier.verifyAccount(user, accountId);
    charge.setAmount(Math.abs(charge.getAmount()));
    charge.setStartDate(new Moment(charge.getStartDate()).getFirstDateOfMonth().getDate());
    if (charge.getEndDate() != null)
      charge.setEndDate(new Moment(charge.getEndDate()).getLastDateOfMonth().getDate());
    return this.chargeFetcher.createCharge(accountId, charge);
  }

  public Charge updateCharge(User user, long accountId, Charge charge) {
    this.accountVerifier.verifyAccount(user, accountId);
    List<Charge> charges = this.chargeFetcher
            .getChargesOfUserAndAccount(user, accountId)
            .stream()
            .filter(fetchedCharge -> fetchedCharge.getId() == charge.getId())
            .collect(Collectors.toList());
    if (charges.size() == 0)
      throw new UpdateException("No charge found with id " + charge.getId());
    else {
      Charge oldCharge = charges.get(0);
      if (oldCharge.getAmount() != charge.getAmount())
        return this.updateChargeAmount(accountId, charge, oldCharge);
      else
        return this.updateChargeProperties(accountId, charge);
    }
  }

  public Charge deleteCharge(User user, long accountId, Charge charge) throws NoElementFoundException {
    if (charge.getId() > 0) {
      this.accountVerifier.verifyAccount(user, accountId);
      charge.setEndDate(new Moment(charge.getEndDate()).getLastDateOfMonth().getDate());
      return this.chargeFetcher.updateCharge(accountId, charge);
    } else
      throw new NoElementFoundException("No such charge");
  }

  private Charge updateChargeAmount(long accountId, Charge charge, Charge oldCharge) {
    Moment oldChargeEndDate = new Moment(charge.getStartDate()).getLastDayOfPrecedingMonth();
    oldCharge.setEndDate(oldChargeEndDate.getDate());
    this.chargeFetcher.updateCharge(accountId, oldCharge);
    Charge newCharge = new Charge(charge.getDescription(), Math.abs(charge.getAmount()), new Moment(charge.getStartDate()).getFirstDateOfMonth().getDate());
    if (charge.getEndDate() != null) newCharge.setEndDate(charge.getEndDate());
    return this.chargeFetcher.createCharge(accountId, newCharge);
  }

  private Charge updateChargeProperties(long accountId, Charge charge) {
    charge.setStartDate(new Moment(charge.getStartDate()).getFirstDateOfMonth().getDate());
    if (charge.getEndDate() != null) charge.setEndDate(new Moment(charge.getEndDate()).getLastDateOfMonth().getDate());
    return this.chargeFetcher.updateCharge(accountId, charge);
  }
}
