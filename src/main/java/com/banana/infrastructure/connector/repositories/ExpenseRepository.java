package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SExpense;
import com.banana.infrastructure.orm.models.SUser;

import java.util.List;

public class ExpenseRepository implements IExpenseRepository {

  public List<SExpense> getExpensesOfUserAccountAndBudgetById(SUser user, long accountId, long budgetId) {
    return null;
  }
}
