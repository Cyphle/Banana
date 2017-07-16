package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.IExpenseRepository;
import com.banana.utilities.FakeExpenseRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpenseFetcherTests {
  private IExpenseRepository expenseRepository;
  private IExpenseFetcher expenseFetcher;

  private User user;

  @Before
  public void setup() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");

    this.expenseRepository = new FakeExpenseRepository();

    this.expenseFetcher = new ExpenseFetcher(this.expenseRepository);
  }

  @Test
  public void should_get_expenses_for_budget_of_user_and_account() {
    List<Expense> expenses = this.expenseFetcher.getExpensesByBudgetid(1);

    assertThat(expenses.size()).isEqualTo(2);
    assertThat(expenses.get(0).getDescription()).isEqualTo("Courses");
    assertThat(expenses.get(0).getAmount()).isEqualTo(24);
    assertThat(expenses.get(1).getDescription()).isEqualTo("Bar");
    assertThat(expenses.get(1).getAmount()).isEqualTo(40);
  }
}
