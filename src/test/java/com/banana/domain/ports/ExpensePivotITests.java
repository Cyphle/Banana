package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.adapters.IExpenseFetcher;
import com.banana.domain.calculators.ExpenseCalculator;
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

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExpensePivotITests {
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
  private ExpensePort expensePort;

  private User user;
  private SUser sUser;
  private SAccount sAccount;
  private SBudget sBudget;
  private SExpense sExpenseOne;
  private SExpense sExpenseTwo;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(this.sUser);

    this.sAccount = new SAccount(this.sUser, "My account", "my-account", 2000);
    this.entityManager.persist(this.sAccount);

    this.sBudget = new SBudget("My budget", 200, (new Moment("2017-07-01")).getDate());
    this.sBudget.setAccount(this.sAccount);
    this.entityManager.persist(this.sBudget);

    this.sExpenseOne = new SExpense("Courses", 40, (new Moment("2017-07-18")).getDate());
    this.sExpenseOne.setBudget(this.sBudget);
    this.entityManager.persist(this.sExpenseOne);

    this.sExpenseTwo = new SExpense("Bar", 30, (new Moment("2017-07-06")).getDate());
    this.sExpenseTwo.setAccount(this.sAccount);
    this.entityManager.persist(this.sExpenseTwo);

    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.budgetRepository = new BudgetRepository(this.sBudgetRepository);
    this.budgetFetcher = new BudgetFetcher(this.userRepository, this.accountRepository, this.budgetRepository);
    this.expenseRepository = new ExpenseRepository(this.sExpenseRepository);
    this.expenseFetcher = new ExpenseFetcher(this.accountRepository, this.budgetRepository, this.expenseRepository);

    this.expensePort = new ExpenseCalculator(this.accountFetcher, this.budgetFetcher, this.expenseFetcher);
  }

  @Test
  public void should_add_expense_to_budget() {
    Moment today = new Moment();
    Expense newExpense = new Expense("Courses", 20, today.getDate());

    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Budget myBudget = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId()).get(0);

    Expense createdExpense = this.expensePort.createExpense(this.user, myAccount.getId(), myBudget.getId(), newExpense);
    Moment expenseDate = new Moment(createdExpense.getExpenseDate());

    assertThat(createdExpense.getId()).isGreaterThan(0);
    assertThat(createdExpense.getDescription()).isEqualTo(newExpense.getDescription());
    assertThat(createdExpense.getAmount()).isEqualTo(newExpense.getAmount());
    assertThat(expenseDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(expenseDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(expenseDate.getYear()).isEqualTo(today.getYear());
  }

  @Test
  public void should_add_expense_to_account() {
    Moment today = new Moment();
    Expense newExpense = new Expense("Courses", 20, today.getDate());

    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");

    Expense createdExpense = this.expensePort.createExpense(this.user, myAccount.getId(), -1, newExpense);
    Moment expenseDate = new Moment(createdExpense.getExpenseDate());

    assertThat(createdExpense.getId()).isGreaterThan(0);
    assertThat(createdExpense.getDescription()).isEqualTo(newExpense.getDescription());
    assertThat(createdExpense.getAmount()).isEqualTo(newExpense.getAmount());
    assertThat(expenseDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(expenseDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(expenseDate.getYear()).isEqualTo(today.getYear());
  }

  @Test
  public void should_update_a_budget_expense() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Budget myBudget = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, myAccount.getId()).get(0);
    Expense myExpense = this.expenseFetcher.getExpensesOfBudget(myBudget).get(0);

    myExpense.setDescription(myExpense.getDescription() + " updated");
    myExpense.setAmount(50);
    myExpense.setDebitDate(new Moment("2017-08-02").getDate());

    Expense updatedExpense = this.expensePort.updateExpense(this.user, myAccount.getId(), myBudget.getId(), myExpense);
    Moment debitDate = new Moment(updatedExpense.getDebitDate());

    assertThat(updatedExpense.getId()).isEqualTo(myExpense.getId());
    assertThat(updatedExpense.getDescription()).isEqualTo("Courses updated");
    assertThat(updatedExpense.getAmount()).isEqualTo(50);
    assertThat(debitDate.getDayOfMonth()).isEqualTo(2);
    assertThat(debitDate.getMonthNumber()).isEqualTo(8);
    assertThat(debitDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_update_an_account_expense() {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(this.user, "my-account");
    Expense expenseToUpdate = this.expenseFetcher.getExpensesOfAccount(myAccount).get(0);

    expenseToUpdate.setDescription(expenseToUpdate.getDescription() + " updated");
    expenseToUpdate.setAmount(100);
    expenseToUpdate.setDebitDate((new Moment("2017-08-10")).getDate());

    Expense updatedExpense = this.expensePort.updateExpense(this.user, myAccount.getId(), -1, expenseToUpdate);
    Moment debitDate = new Moment(updatedExpense.getDebitDate());

    assertThat(updatedExpense.getId()).isEqualTo(expenseToUpdate.getId());
    assertThat(updatedExpense.getDescription()).isEqualTo("Bar updated");
    assertThat(updatedExpense.getAmount()).isEqualTo(100);
    assertThat(debitDate.getDayOfMonth()).isEqualTo(10);
    assertThat(debitDate.getMonthNumber()).isEqualTo(8);
    assertThat(debitDate.getYear()).isEqualTo(2017);
  }
}
