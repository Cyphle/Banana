package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Expense;
import com.banana.infrastructure.orm.models.SExpense;
import com.banana.utils.Moment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpensePivotTests {
  @Test
  public void should_pivot_domain_expense_from_infrastructure() {
    SExpense sExpense = new SExpense("My expense", 20, (new Moment("2017-07-14")).getDate());
    sExpense.setId(1);
    sExpense.setDebitDate((new Moment("2017-07-21")).getDate());

    Expense expense = ExpensePivot.fromInfrastructureToDomain(sExpense);
    Moment expenseDate = new Moment(expense.getExpenseDate());
    Moment debitDate = new Moment(expense.getDebitDate());

    assertThat(expense.getId()).isEqualTo(sExpense.getId());
    assertThat(expense.getDescription()).isEqualTo(expense.getDescription());
    assertThat(expense.getAmount()).isEqualTo(sExpense.getAmount());
    assertThat(expenseDate.getDayOfMonth()).isEqualTo(14);
    assertThat(expenseDate.getMonthNumber()).isEqualTo(7);
    assertThat(expenseDate.getYear()).isEqualTo(2017);
    assertThat(debitDate.getDayOfMonth()).isEqualTo(21);
    assertThat(debitDate.getMonthNumber()).isEqualTo(7);
    assertThat(debitDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_pivot_domain_expenses_from_infrastructure() {
    SExpense sExpenseOne = new SExpense("My expense", 20, (new Moment("2017-07-14")).getDate());
    sExpenseOne.setId(1);
    sExpenseOne.setDebitDate((new Moment("2017-07-21")).getDate());
    SExpense sExpenseTwo = new SExpense("Bar", 40, (new Moment("2017-07-12")).getDate());
    sExpenseOne.setId(2);
    sExpenseOne.setDebitDate((new Moment("2017-07-16")).getDate());
    List<SExpense> sExpenses = new ArrayList<>();
    sExpenses.add(sExpenseOne);
    sExpenses.add(sExpenseTwo);

    List<Expense> expenses = ExpensePivot.fromInfrastructureToDomain(sExpenses);

    assertThat(expenses.size()).isEqualTo(2);
    assertThat(expenses.get(0).getId()).isEqualTo(sExpenseOne.getId());
    assertThat(expenses.get(0).getDescription()).isEqualTo(sExpenseOne.getDescription());
    assertThat(expenses.get(0).getAmount()).isEqualTo(sExpenseOne.getAmount());
    assertThat(expenses.get(1).getId()).isEqualTo(sExpenseTwo.getId());
    assertThat(expenses.get(1).getDescription()).isEqualTo(sExpenseTwo.getDescription());
    assertThat(expenses.get(1).getAmount()).isEqualTo(sExpenseTwo.getAmount());
  }

  @Test
  public void should_pivot_infrastructure_expense_from_domain() {
    Expense expense = new Expense(1, "Courses", 24, (new Moment("2017-07-18")).getDate());
    expense.setDebitDate((new Moment("2017-07-20")).getDate());

    SExpense sExpense = ExpensePivot.fromDomainToInfrastructure(expense);
    Moment expenseDate = new Moment(sExpense.getExpenseDate());
    Moment debitDate = new Moment(sExpense.getDebitDate());

    assertThat(sExpense.getId()).isEqualTo(1);
    assertThat(sExpense.getDescription()).isEqualTo("Courses");
    assertThat(sExpense.getAmount()).isEqualTo(24);
    assertThat(expenseDate.getDayOfMonth()).isEqualTo(18);
    assertThat(expenseDate.getMonthNumber()).isEqualTo(7);
    assertThat(expenseDate.getYear()).isEqualTo(2017);
    assertThat(debitDate.getDayOfMonth()).isEqualTo(20);
    assertThat(debitDate.getMonthNumber()).isEqualTo(7);
    assertThat(debitDate.getYear()).isEqualTo(2017);
  }
}
