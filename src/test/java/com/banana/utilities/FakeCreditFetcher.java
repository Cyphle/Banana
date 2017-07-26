package com.banana.utilities;

import com.banana.domain.adapters.ICreditFetcher;
import com.banana.domain.models.Credit;
import com.banana.domain.models.User;

import java.util.List;

public class FakeCreditFetcher implements ICreditFetcher {
  public List<Credit> getCreditsOfUserAndAccount(User user, long accountId) {
    return null;
  }

  public Credit createCredit(long accountId, Credit credit) {
    credit.setId(10);
    return credit;
  }

  public Credit updateCredit(long accountId, Credit credit) {
    return credit;
  }

  public boolean deleteCredit(Credit credit) {
    return true;
  }
}
