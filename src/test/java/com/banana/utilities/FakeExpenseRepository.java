package com.banana.utilities;

import com.banana.infrastructure.connector.repositories.IExpenseRepository;
import com.banana.infrastructure.orm.models.SExpense;
import com.banana.utils.Moment;

import java.util.ArrayList;
import java.util.List;

public class FakeExpenseRepository implements IExpenseRepository {
  public List<SExpense> getExpensesByBudgetid(long budgetId) {
    SExpense sExpenseOne = new SExpense("Courses", 24, (new Moment("2017-07-14")).getDate());
    sExpenseOne.setId(1);
    sExpenseOne.setDebitDate((new Moment("2017-07-21")).getDate());
    SExpense sExpenseTwo = new SExpense("Bar", 40, (new Moment("2017-07-12")).getDate());
    sExpenseOne.setId(2);
    sExpenseOne.setDebitDate((new Moment("2017-07-16")).getDate());
    List<SExpense> sExpenses = new ArrayList<>();
    sExpenses.add(sExpenseOne);
    sExpenses.add(sExpenseTwo);
    return sExpenses;
  }
}
