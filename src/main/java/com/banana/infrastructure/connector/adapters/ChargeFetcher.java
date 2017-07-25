package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.pivots.ChargePivot;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IChargeRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SCharge;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public class ChargeFetcher implements IChargeFetcher {
  private IAccountRepository accountRepository;
  private IChargeRepository chargeRepository;

  public ChargeFetcher(IAccountRepository accountRepository, IChargeRepository chargeRepository) {
    this.accountRepository = accountRepository;
    this.chargeRepository = chargeRepository;
  }

  public List<Charge> getChargesOfUserAndAccount(User user, long accountId) {
    SUser sUser = UserPivot.fromDomainToInfrastructure(user);
    List<SCharge> charges = this.chargeRepository.getChargesOfUserAndAccount(sUser, accountId);
    return ChargePivot.fromInfrastructureToDomain(charges);
  }

  public Charge createCharge(long accountId, Charge charge) {
    SCharge sCharge = this.fromDomainToInfrastructure(accountId, charge);
    SCharge createdCharge = this.chargeRepository.createCharge(sCharge);
    return ChargePivot.fromInfrastructureToDomain(createdCharge);
  }

  public Charge updateCharge(long accountId, Charge charge) {
    SCharge sCharge = this.fromDomainToInfrastructure(accountId, charge);
    SCharge updatedCharge = this.chargeRepository.updateCharge(sCharge);
    return ChargePivot.fromInfrastructureToDomain(updatedCharge);
  }

  private SCharge fromDomainToInfrastructure(long accountId, Charge charge) {
    SAccount sAccount = this.accountRepository.getAccountById(accountId);
    SCharge sCharge = ChargePivot.fromDomainToInfrastructure(charge);
    sCharge.setAccount(sAccount);
    return sCharge;
  }

  public boolean deleteCharge(Charge charge) {
    SCharge sCharge = ChargePivot.fromDomainToInfrastructure(charge);
    sCharge.setDeleted(true);
    SCharge deletedCharge = this.chargeRepository.updateCharge(sCharge);
    if (deletedCharge != null && deletedCharge.isDeleted())
      return true;
    return false;
  }
}
