package com.banana.domain.adapters;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;

import java.util.List;

public interface IAccountFetcher {
  List<Account> getAccountsOfUser(User user);
}
