package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.calculators.BudgetCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.infrastructure.connector.adapters.BudgetFetcher;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SBudgetRepository;
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
public class BudgetPivotITests {
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private SAccountRepository sAccountRepository;
  @Autowired
  private SUserRepository sUserRepository;
  @Autowired
  private SBudgetRepository sBudgetRepository;

  private IAccountRepository accountRepository;
  private IUserRepository userRepository;
  private IAccountFetcher accountFetcher;
  private IBudgetRepository budgetRepository;
  private IBudgetFetcher budgetFetcher;
  private AccountPort accountPort;
  private BudgetPort budgetPort;

  private User user;
  private SUser fakeUser;
  private SAccount accountOne;
  private SBudget budget;

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
    this.budget = new SBudget("Budget one", 300, (new Moment()).getFirstDateOfMonth().getDate());
    this.budget.setAccount(this.accountOne);
    this.entityManager.persist(this.budget);

    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.accountPort = new AccountCalculator(this.accountFetcher);

    this.budgetRepository = new BudgetRepository(this.sBudgetRepository);
    this.budgetFetcher = new BudgetFetcher(this.userRepository, this.accountRepository, this.budgetRepository);
    this.budgetPort = new BudgetCalculator(this.accountFetcher, this.budgetFetcher);
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
  public void should_update_a_budget() {
    Account myAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "my-account");
    List<Budget> existingBudgets = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId());
    Budget budgetToUpdate = null;
    for (Budget tempBudget : existingBudgets) {
      if (tempBudget.getName() == "Budget one")
        budgetToUpdate = tempBudget;
    }

    if (budgetToUpdate != null) {
      budgetToUpdate.setName("Budget updated");
      budgetToUpdate.setInitialAmount(500);
      budgetToUpdate.setStartDate((new Moment("2017-06-01")).getDate());
      budgetToUpdate.setEndDate((new Moment("2017-10-31")).getDate());
      Budget updatedBudget = this.budgetPort.updateBudget(this.user, myAccount.getId(), budgetToUpdate);

      Moment startDate = new Moment(updatedBudget.getStartDate());
      Moment endDate = new Moment(updatedBudget.getEndDate());

      assertThat(updatedBudget.getId()).isEqualTo(budgetToUpdate.getId());
      assertThat(updatedBudget.getName()).isEqualTo("Budget updated");
      assertThat(updatedBudget.getInitialAmount()).isEqualTo(500);
      assertThat(startDate.getDayOfMonth()).isEqualTo(1);
      assertThat(startDate.getMonthNumber()).isEqualTo(6);
      assertThat(startDate.getYear()).isEqualTo(2017);
      assertThat(endDate.getDayOfMonth()).isEqualTo(31);
      assertThat(endDate.getMonthNumber()).isEqualTo(10);
      assertThat(endDate.getYear()).isEqualTo(2017);
    } else {
      fail("No budget has been found in this test");
    }
  }

  @Test
  public void should_add_expense_to_budget() {
    Moment today = new Moment();
    Expense newExpense = new Expense("Courses", 20, today.getDate());

    Account myAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "my-account");
    List<Budget> existingBudgets = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId());
    Budget budget = null;
    for (Budget tempBudget : existingBudgets) {
      if (tempBudget.getName() == "Budget one")
        budget = tempBudget;
    }

    Budget myBudget = this.budgetFetcher.getBudgetOfUserAndAccountById(this.user, myAccount.getId(), budget.getId());

    Expense createdExpense = this.budgetPort.addExpense(this.user, myAccount.getId(), myBudget.getId(), newExpense);
    Moment expenseDate = new Moment(createdExpense.getExpenseDate());

    assertThat(createdExpense.getId()).isGreaterThan(0);
    assertThat(createdExpense.getDescription()).isEqualTo(newExpense.getDescription());
    assertThat(createdExpense.getAmount()).isEqualTo(newExpense.getAmount());
    assertThat(expenseDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(expenseDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(expenseDate.getYear()).isEqualTo(today.getYear());
  }
  /*
    Should add expense
    -> check that total budget expenses + expense is not higher that budget initial amount (for the month)
    -> add expense
   */

  /*
    Should delete budget
    -> modify endDate to end at given date
   */
}
