package com.banana.domain.ports;

import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;

public interface BudgetPort {
  Budget createBudget(Account account, Budget budget);
}
