package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SExpense;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SExpenseRepositoryTests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SExpenseRepository expenseRepository;

  @Autowired
  private SBudgetRepository budgetRepository;

  @Autowired
  private SAccountRepository accountRepository;

  private SUser user;
  private SAccount account;
  private SExpense expenseOne;
  private SExpense expenseTwo;
  private SExpense expenseThree;
  private SExpense expenseFour;
  private SBudget budget;

  @Before
  public void setup() {
    this.user = new SUser("Doe", "John", "john@doe.fr");
    this.entityManager.persist(this.user);

    this.account = new SAccount(this.user, "My Account", "my-account", 2000);
    this.entityManager.persist(this.account);

    this.budget = new SBudget("My budget", 200, (new Moment()).getFirstDateOfMonth().getDate());
    this.budget.setAccount(this.account);
    this.entityManager.persist(this.budget);

    this.expenseOne = new SExpense("Courses", 24, (new Moment("2017-07-12")).getDate());
    this.expenseOne.setBudget(this.budget);
    this.entityManager.persist(this.expenseOne);

    this.expenseTwo = new SExpense("Bar", 40, (new Moment("2017-07-13")).getDate());
    this.expenseTwo.setBudget(this.budget);
    this.entityManager.persist(this.expenseTwo);

    this.expenseThree = new SExpense("Retrait", 50, (new Moment("2017-07-14")).getDate());
    this.expenseThree.setAccount(this.account);
    this.entityManager.persist(this.expenseThree);

    this.expenseFour = new SExpense("Sortie", 100, (new Moment("2017-08-15")).getDate());
    this.expenseFour.setAccount(this.account);
    this.entityManager.persist(this.expenseFour);
  }

  @Test
  public void should_get_expenses_by_account_id_but_not_budget_expenses() {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "my-account");

    List<SExpense> sExpenses = this.expenseRepository.findByAccountId(sAccount.getId());

    assertThat(sExpenses.size()).isEqualTo(2);
    assertThat(sExpenses.get(0).getDescription()).isEqualTo("Retrait");
    assertThat(sExpenses.get(0).getAmount()).isEqualTo(50);
    assertThat(sExpenses.get(1).getDescription()).isEqualTo("Sortie");
    assertThat(sExpenses.get(1).getAmount()).isEqualTo(100);
  }

  @Test
  public void should_get_expenses_by_budget_id() {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "my-account");
    List<SBudget> sBudget = this.budgetRepository.findByUserUsernameAndAccountId(this.user.getUsername(), sAccount.getId());

    List<SExpense> sExpenses = this.expenseRepository.findByBudgetId(sBudget.get(0).getId());

    assertThat(sExpenses.size()).isEqualTo(2);
    assertThat(sExpenses.get(0).getDescription()).isEqualTo("Courses");
    assertThat(sExpenses.get(0).getAmount()).isEqualTo(24);
    assertThat(sExpenses.get(1).getDescription()).isEqualTo("Bar");
    assertThat(sExpenses.get(1).getAmount()).isEqualTo(40);
  }

  @Test
  public void should_save_new_expense() {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "my-account");
    List<SBudget> sBudgets = this.budgetRepository.findByUserUsernameAndAccountId(this.user.getUsername(), sAccount.getId());
    SExpense sExpense = new SExpense("Courses", 24, (new Moment("2017-07-18")).getDate());
    sExpense.setBudget(sBudgets.get(0));

    SExpense savedExpense = this.expenseRepository.save(sExpense);

    assertThat(savedExpense.getId()).isGreaterThan(0);
    assertThat(savedExpense.getDescription()).isEqualTo("Courses");
    assertThat(savedExpense.getAmount()).isEqualTo(24);
  }

  @Test
  public void should_update_budget_expense() {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "my-account");
    List<SBudget> sBudgets = this.budgetRepository.findByUserUsernameAndAccountId(this.user.getUsername(), sAccount.getId());
    SExpense sExpense = new SExpense("Bar", 40, (new Moment("2017-07-18")).getDate());
    sExpense.setBudget(sBudgets.get(0));
    SExpense expenseToUpdate = this.expenseRepository.save(sExpense);
    expenseToUpdate.setAmount(100);
    expenseToUpdate.setDebitDate((new Moment("2017-07-20")).getDate());

    SExpense updatedExpense = this.expenseRepository.save(expenseToUpdate);
    Moment debitDate = new Moment(updatedExpense.getDebitDate());

    assertThat(updatedExpense.getId()).isEqualTo(expenseToUpdate.getId());
    assertThat(updatedExpense.getAmount()).isEqualTo(100);
    assertThat(updatedExpense.getBudget().getName()).isEqualTo("My budget");
    assertThat(updatedExpense.getAccount()).isNull();
    assertThat(debitDate.getDayOfMonth()).isEqualTo(20);
    assertThat(debitDate.getMonthNumber()).isEqualTo(7);
    assertThat(debitDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_update_account_expense() {
    SAccount sAccount = this.accountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "my-account");
    SExpense sExpense = new SExpense("Retrait", 50, (new Moment("2017-07-18")).getDate());
    sExpense.setAccount(sAccount);
    SExpense expenseToUpdate = this.expenseRepository.save(sExpense);
    expenseToUpdate.setAmount(200);
    expenseToUpdate.setDebitDate((new Moment("2017-07-22")).getDate());

    SExpense updatedExpense = this.expenseRepository.save(expenseToUpdate);
    Moment debitDate = new Moment(updatedExpense.getDebitDate());

    assertThat(updatedExpense.getId()).isEqualTo(expenseToUpdate.getId());
    assertThat(updatedExpense.getAmount()).isEqualTo(200);
    assertThat(updatedExpense.getBudget()).isNull();
    assertThat(updatedExpense.getAccount().getName()).isEqualTo("My Account");
    assertThat(debitDate.getDayOfMonth()).isEqualTo(22);
    assertThat(debitDate.getMonthNumber()).isEqualTo(7);
    assertThat(debitDate.getYear()).isEqualTo(2017);
  }
}
