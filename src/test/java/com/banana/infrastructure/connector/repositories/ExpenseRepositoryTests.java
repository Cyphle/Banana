package com.banana.infrastructure.connector.repositories;

import com.banana.BananaApplication;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SExpense;
import com.banana.infrastructure.orm.repositories.SExpenseRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ExpenseRepositoryTests {
  @MockBean
  private SExpenseRepository sExpenseRepository;

  private IExpenseRepository expenseRepository;

  private List<SExpense> expenses;
  private SExpense expenseOne;
  private SExpense expenseTwo;
  private SBudget budget;

  @Before
  public void setup() {
    this.budget = new SBudget("My budget", 200, (new Moment()).getFirstDateOfMonth().getDate());
    this.expenseOne = new SExpense("Courses", 24, (new Moment("2017-07-12")).getDate());
    this.expenseTwo = new SExpense("Bar", 40, (new Moment("2017-07-13")).getDate());
    this.expenses = new ArrayList<>();
    this.expenses.add(this.expenseOne);
    this.expenses.add(this.expenseTwo);

    this.expenseRepository = new ExpenseRepository(this.sExpenseRepository);
  }

  @Test
  public void should_get_expenses_by_budget_id() {
    given(this.expenseRepository.getExpensesByBudgetid(any(long.class))).willReturn(this.expenses);

    List<SExpense> sExpenses = this.expenseRepository.getExpensesByBudgetid(1);

    assertThat(sExpenses.size()).isEqualTo(2);
    assertThat(sExpenses.get(0).getDescription()).isEqualTo("Courses");
    assertThat(sExpenses.get(0).getAmount()).isEqualTo(24);
    assertThat(sExpenses.get(1).getDescription()).isEqualTo("Bar");
    assertThat(sExpenses.get(1).getAmount()).isEqualTo(40);
  }

  @Test
  public void should_create_new_expense() {
    SExpense newExpense = new SExpense("Courses", 24, (new Moment("2017-07-18")).getDate());
    newExpense.setBudget(this.budget);
    Moment today = new Moment();
    given(this.sExpenseRepository.save(any(SExpense.class))).willReturn(newExpense);

    SExpense createdExpense = this.expenseRepository.createExpense(newExpense);
    Moment creationDate = new Moment(createdExpense.getCreationDate());
    Moment updateDate = new Moment(createdExpense.getUpdateDate());

    assertThat(createdExpense.getDescription()).isEqualTo("Courses");
    assertThat(createdExpense.getAmount()).isEqualTo(24);
    assertThat(creationDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(creationDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(creationDate.getYear()).isEqualTo(today.getYear());
    assertThat(updateDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(updateDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(updateDate.getYear()).isEqualTo(today.getYear());
  }
}
