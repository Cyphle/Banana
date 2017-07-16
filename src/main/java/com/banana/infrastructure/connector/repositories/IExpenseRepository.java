package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SExpense;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public interface IExpenseRepository {
  List<SExpense> getExpensesOfUserAccountAndBudgetById(SUser user, long accountId, long budgetId);
}
