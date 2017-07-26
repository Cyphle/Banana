package com.banana.domain.adapters;

import com.banana.domain.models.Credit;
import com.banana.domain.models.User;

import java.util.List;

public interface ICreditFetcher {
  List<Credit> getCreditsOfUserAndAccount(User user, long accountId);
  Credit createCredit(long accountId, Credit credit);
  Credit updateCredit(long accountId, Credit credit);
  boolean deleteCredit(Credit credit);
}
