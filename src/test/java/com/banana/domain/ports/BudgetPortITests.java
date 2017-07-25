package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.calculators.BudgetCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.infrastructure.connector.adapters.BudgetFetcher;
import com.banana.infrastructure.connector.adapters.ExpenseFetcher;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SExpense;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SBudgetRepository;
import com.banana.infrastructure.orm.repositories.SExpenseRepository;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BudgetPortITests {
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private SAccountRepository sAccountRepository;
  @Autowired
  private SUserRepository sUserRepository;
  @Autowired
  private SBudgetRepository sBudgetRepository;
  @Autowired
  private SExpenseRepository sExpenseRepository;

  private IAccountRepository accountRepository;
  private IUserRepository userRepository;
  private IAccountFetcher accountFetcher;
  private IBudgetRepository budgetRepository;
  private IBudgetFetcher budgetFetcher;
  private IExpenseRepository expenseRepository;
  private IExpenseFetcher expenseFetcher;
  private AccountPort accountPort;
  private BudgetPort budgetPort;

  private User user;
  private SUser fakeUser;
  private SAccount accountOne;
  private SBudget budget;
  private SExpense expenseOne;
  private SExpense expenseTwo;
  private SExpense expenseThree;

  @Before
  public void setup() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");
    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(fakeUser);

    Moment today = new Moment();
    this.accountOne = new SAccount("My Account", 100);
    this.accountOne.setSlug("my-account");
    this.accountOne.setUser(this.fakeUser);
    this.accountOne.setCreationDate(today.getDate());
    this.accountOne.setUpdateDate(today.getDate());
    this.entityManager.persist(this.accountOne);

    this.budget = new SBudget("Budget one", 300, (new Moment("2017-07-01")).getFirstDateOfMonth().getDate());
    this.budget.setAccount(this.accountOne);
    this.entityManager.persist(this.budget);

    this.expenseOne = new SExpense("Bar", 20, (new Moment("2017-07-10")).getDate());
    this.expenseOne.setBudget(this.budget);
    this.entityManager.persist(this.expenseOne);
    this.expenseTwo = new SExpense("Retrait", 40, (new Moment("2017-08-01")).getDate());
    this.expenseTwo.setBudget(this.budget);
    this.entityManager.persist(this.expenseTwo);
    this.expenseThree = new SExpense("Courses", 10, (new Moment("2017-09-01")).getDate());
    this.expenseThree.setBudget(this.budget);
    this.entityManager.persist(this.expenseThree);

    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.accountPort = new AccountCalculator(this.accountFetcher);

    this.expenseRepository = new ExpenseRepository(this.sExpenseRepository);
    this.budgetRepository = new BudgetRepository(this.sBudgetRepository);
    this.expenseFetcher = new ExpenseFetcher(this.accountRepository, this.budgetRepository, this.expenseRepository);
    this.budgetFetcher = new BudgetFetcher(this.accountRepository, this.budgetRepository);
    this.budgetPort = new BudgetCalculator(this.accountFetcher, this.budgetFetcher, this.expenseFetcher);
  }

  @Test
  public void should_create_new_budget() {
    Moment today = new Moment();
    Account myAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "my-account");
    Budget newBudget = new Budget("My budget", 200, today.getFirstDateOfMonth().getDate());

    Budget createdBudget = this.budgetPort.createBudget(this.user, myAccount.getId(), newBudget);
    Moment startDate = new Moment(createdBudget.getStartDate());

    assertThat(createdBudget.getId()).isGreaterThan(0);
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(startDate.getYear()).isEqualTo(today.getYear());
  }

  @Test
  public void should_update_a_budget_name() {
    Account myAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "my-account");
    List<Budget> existingBudgets = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId());
    Budget budgetToUpdate = null;
    for (Budget tempBudget : existingBudgets) {
      if (tempBudget.getName() == "Budget one")
        budgetToUpdate = tempBudget;
    }

    if (budgetToUpdate != null) {
      budgetToUpdate.setName("Budget updated");
      Budget updatedBudget = this.budgetPort.updateBudget(this.user, myAccount.getId(), budgetToUpdate);

      Moment startDate = new Moment(updatedBudget.getStartDate());

      assertThat(updatedBudget.getId()).isEqualTo(budgetToUpdate.getId());
      assertThat(updatedBudget.getName()).isEqualTo("Budget updated");
      assertThat(updatedBudget.getInitialAmount()).isEqualTo(300);
      assertThat(startDate.getDayOfMonth()).isEqualTo(1);
      assertThat(startDate.getMonthNumber()).isEqualTo(7);
      assertThat(startDate.getYear()).isEqualTo(2017);
    } else {
      fail("No budget has been found in this test");
    }
  }

  @Test
  public void should_create_a_new_budget_if_amount_is_modified_and_old_budget_is_stopped() {
    Account myAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "my-account");
    Budget budgetToUpdate = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId()).get(0);
    Moment initialStartDate = new Moment(budget.getStartDate());

    // Set new start date to indicate beginning date of change
    budgetToUpdate.setInitialAmount(200);
    budgetToUpdate.setStartDate((new Moment("2017-08-10")).getDate());
    Budget updatedBudget = this.budgetPort.updateBudget(this.user, myAccount.getId(), budgetToUpdate);
    Moment newStartDate = new Moment(updatedBudget.getStartDate());

    List<Budget> existingBudgets = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId());
    Budget oldBudget = existingBudgets.get(0);
    Moment oldStartDate = new Moment(oldBudget.getStartDate());
    Moment oldEndDate = new Moment(oldBudget.getEndDate());

    // Check that a new budget has been created
    assertThat(existingBudgets.size()).isEqualTo(2);
    assertThat(updatedBudget.getId()).isNotEqualTo(oldBudget.getId());
    // Check that old budget did not change except for end date
    assertThat(oldStartDate.getDayOfMonth()).isEqualTo(initialStartDate.getDayOfMonth());
    assertThat(oldStartDate.getMonthNumber()).isEqualTo(initialStartDate.getMonthNumber());
    assertThat(oldStartDate.getYear()).isEqualTo(initialStartDate.getYear());
    assertThat(oldEndDate.getDayOfMonth()).isEqualTo(31);
    assertThat(oldEndDate.getMonthNumber()).isEqualTo(7);
    assertThat(oldEndDate.getYear()).isEqualTo(2017);
    // Check property of new budget
    assertThat(updatedBudget.getId()).isNotEqualTo(budgetToUpdate.getId());
    assertThat(updatedBudget.getInitialAmount()).isEqualTo(200);
    assertThat(updatedBudget.getEndDate()).isNull();
    assertThat(newStartDate.getDayOfMonth()).isEqualTo(1);
    assertThat(newStartDate.getMonthNumber()).isEqualTo(8);
    assertThat(newStartDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_delete_expenses_before_new_budget_start_date() {
    Account myAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "my-account");
    Budget budgetToUpdate = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId()).get(0);

    budgetToUpdate.setStartDate(new Moment("2017-08-01").getDate());
    Budget updatedBudget = this.budgetPort.updateBudget(this.user, myAccount.getId(), budgetToUpdate);
    Moment newStartDate = new Moment(updatedBudget.getStartDate());

    List<Expense> expenses = this.expenseFetcher.getExpensesOfBudget(budgetToUpdate);

    assertThat(newStartDate.getDayOfMonth()).isEqualTo(1);
    assertThat(newStartDate.getMonthNumber()).isEqualTo(8);
    assertThat(newStartDate.getYear()).isEqualTo(2017);
    assertThat(expenses.size()).isEqualTo(2);
    assertThat(expenses.get(0).getDescription()).isEqualTo("Retrait");
    assertThat(expenses.get(1).getDescription()).isEqualTo("Courses");
  }

  @Test
  public void should_delete_expenses_after_new_budget_end_date() {
    Account myAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "my-account");
    Budget budgetToUpdate = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId()).get(0);

    budgetToUpdate.setEndDate(new Moment("2017-08-20").getDate());
    Budget updatedBudget = this.budgetPort.updateBudget(this.user, myAccount.getId(), budgetToUpdate);
    Moment newEndDate = new Moment(updatedBudget.getEndDate());

    List<Expense> expenses = this.expenseFetcher.getExpensesOfBudget(budgetToUpdate);

    assertThat(newEndDate.getDayOfMonth()).isEqualTo(31);
    assertThat(newEndDate.getMonthNumber()).isEqualTo(8);
    assertThat(newEndDate.getYear()).isEqualTo(2017);
    assertThat(expenses.size()).isEqualTo(2);
    assertThat(expenses.get(0).getDescription()).isEqualTo("Bar");
    assertThat(expenses.get(1).getDescription()).isEqualTo("Retrait");
  }

  /*
    Should delete budget
    -> modify endDate to end at given date
   */
}
