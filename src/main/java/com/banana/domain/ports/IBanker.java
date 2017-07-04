package com.banana.domain.ports;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;

import java.util.List;

public interface IBanker {
  List<Account> getAccountsOfUser(User user);
}
