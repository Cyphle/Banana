package com.banana.domain.ports;

import com.banana.domain.models.Credit;
import com.banana.domain.models.User;

public interface CreditPort {
  Credit getCreditById(User user, long accountId, long creditId);
  Credit createCredit(User user, long accountId, Credit credit);
  Credit updateCredit(User user, long accountId, Credit credit);
  boolean deleteCredit(User user, long accountId, Credit credit);
}
