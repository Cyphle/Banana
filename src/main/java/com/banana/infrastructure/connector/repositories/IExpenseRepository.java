package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SExpense;

import java.util.List;

public interface IExpenseRepository {
  List<SExpense> getExpenseByAccountId(long accountId);
  List<SExpense> getExpensesByBudgetid(long budgetId);
  SExpense createExpense(SExpense expense);
  SExpense updateExpense(SExpense expense);
}
