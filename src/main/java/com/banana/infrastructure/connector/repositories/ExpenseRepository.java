package com.banana.infrastructure.connector.repositories;

import com.banana.infrastructure.orm.models.SExpense;
import com.banana.infrastructure.orm.repositories.SExpenseRepository;
import com.banana.utils.Moment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ExpenseRepository implements IExpenseRepository {
  private SExpenseRepository expenseRepository;

  @Autowired
  public ExpenseRepository(SExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  public List<SExpense> getExpensesByBudgetid(long budgetId) {
    return this.expenseRepository.findByBudgetId(budgetId);
  }

  public SExpense createExpense(SExpense expense) {
    Moment today = new Moment();
    expense.setCreationDate(today.getDate());
    expense.setUpdateDate(today.getDate());
    return this.expenseRepository.save(expense);
  }
}
