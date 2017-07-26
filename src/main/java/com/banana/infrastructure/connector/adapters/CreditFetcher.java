package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.ICreditFetcher;
import com.banana.domain.models.Credit;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.pivots.CreditPivot;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.ICreditRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SCredit;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public class CreditFetcher implements ICreditFetcher {
  private IAccountRepository accountRepository;
  private ICreditRepository creditRepository;

  public CreditFetcher(IAccountRepository accountRepository, ICreditRepository creditRepository) {
    this.accountRepository = accountRepository;
    this.creditRepository = creditRepository;
  }

  public List<Credit> getCreditsOfUserAndAccount(User user, long accountId) {
    SUser sUser = UserPivot.fromDomainToInfrastructure(user);
    List<SCredit> credits = this.creditRepository.getCreditsOfUserAndAccount(sUser, accountId);
    return CreditPivot.fromInfrastructureToDomain(credits);
  }

  public Credit createCredit(long accountId, Credit credit) {
    SCredit sCredit = this.fromDomainToInfrastructure(accountId, credit);
    SCredit createdCredit = this.creditRepository.createCredit(sCredit);
    return CreditPivot.fromInfrastructureToDomain(createdCredit);
  }

  public Credit updateCredit(long accountId, Credit credit) {
    SCredit sCredit = this.fromDomainToInfrastructure(accountId, credit);
    SCredit updatedCredit = this.creditRepository.updateCredit(sCredit);
    return CreditPivot.fromInfrastructureToDomain(updatedCredit);
  }

  public boolean deleteCredit(Credit credit) {
    SCredit sCredit = CreditPivot.fromDomainToInfrastructure(credit);
    sCredit.setDeleted(true);
    SCredit deletedCredit = this.creditRepository.updateCredit(sCredit);
    if (sCredit != null && deletedCredit.isDeleted())
      return true;
    return false;
  }

  private SCredit fromDomainToInfrastructure(long accountId, Credit credit) {
    SAccount sAccount = this.accountRepository.getAccountById(accountId);
    SCredit sCredit = CreditPivot.fromDomainToInfrastructure(credit);
    sCredit.setAccount(sAccount);
    return sCredit;
  }
}
