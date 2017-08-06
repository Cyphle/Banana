package com.banana.domain.calculators;

import com.banana.domain.adapters.*;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.models.*;
import com.banana.domain.ports.AccountPort;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.utilities.*;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;

public class AccountCalculatorTests {
  private User user;
  private List<Account> accounts;
  private Account account;
  private AccountPort accountPort;

  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private IChargeFetcher chargeFetcher;
  private ICreditFetcher creditFetcher;
  private IExpenseFetcher expenseFetcher;

  @Before
  public void setup() {
    this.user = new User(1, "John", "Doe", "john@doe.fr");
    this.accounts = new ArrayList<>();
    this.accounts.add(new Account(1, this.user, "Account one", "account-one", 1000.0, new Moment("2016-01-01").getDate()));
    this.accounts.add(new Account(2, this.user, "Account two", "account-two", 2000.0, new Moment("2016-01-01").getDate()));

    this.accountFetcher = new FakeAccountFetcher();
    this.budgetFetcher = new FakeBudgetFetcher();
    this.chargeFetcher = new FakeChargeFetcher();
    this.creditFetcher = new FakeCreditFetcher();
    this.expenseFetcher = new FakeExpenseFetcher();
    this.accountPort = new AccountCalculator(this.accountFetcher, this.budgetFetcher, this.chargeFetcher, this.creditFetcher, this.expenseFetcher);

    this.account = new Account(3, this.user, "Account test", "account-three", 3000.0, new Moment("2016-01-01").getDate());

    List<Budget> budgets = new ArrayList<>();
    Budget budgetOne = new Budget(1, "Manger", 400, new Moment("2016-01-01").getDate());
    List<Expense> budgetOneExpenses = new ArrayList<>();
    budgetOneExpenses.add(new Expense(1, "G20", 30, new Moment("2016-02-20").getDate()));
    budgetOneExpenses.add(new Expense(2, "Monoprix", 40, new Moment("2017-07-03").getDate()));
    budgetOneExpenses.add(new Expense(3, "G20", 50, new Moment("2017-08-02").getDate()));
    budgetOne.setExpenses(budgetOneExpenses);
    budgets.add(budgetOne);

    Budget budgetTwo = new Budget(2, "Resto", 300, new Moment("2016-01-01").getDate());
    List<Expense> budgetTwoExpenses = new ArrayList<>();
    budgetTwoExpenses.add(new Expense(4, "Nove", 70, new Moment("2017-07-20").getDate()));
    budgetTwoExpenses.add(new Expense(5, "Ange20", 100, new Moment("2016-06-28").getDate()));
    budgetTwo.setExpenses(budgetTwoExpenses);
    budgets.add(budgetTwo);

    Budget budgetThree = new Budget(3, "Clopes", 200, new Moment("2016-01-01").getDate());
    budgetThree.setEndDate(new Moment("2016-12-31").getDate());
    List<Expense> budgetThreeExpenses = new ArrayList<>();
    budgetThreeExpenses.add(new Expense(6, "Clopes", 35, new Moment("2017-04-12").getDate()));
    budgetThreeExpenses.add(new Expense(7, "Clopes", 35, new Moment("2016-10-28").getDate()));
    budgetThree.setExpenses(budgetThreeExpenses);
    budgets.add(budgetThree);
    this.account.setBudgets(budgets);

    List<Charge> charges = new ArrayList<>();
    Charge chargeOne = new Charge(1, "Loyer", 1200, new Moment("2017-01-01").getDate());
    charges.add(chargeOne);
    Charge chargeTwo = new Charge(2, "Internet", 30, new Moment("2016-02-01").getDate());
    chargeTwo.setEndDate(new Moment("2017-03-03").getDate());
    charges.add(chargeTwo);
    this.account.setCharges(charges);

    List<Credit> credits = new ArrayList<>();
    credits.add(new Credit(1, "Salaire", 2400, new Moment("2017-06-30").getDate()));
    credits.add(new Credit(2, "Maman", 500, new Moment("2017-03-18").getDate()));
    credits.add(new Credit(3, "Salaire", 2400, new Moment("2017-08-31").getDate()));
    this.account.setCredits(credits);

    List<Expense> expenses = new ArrayList<>();
    expenses.add(new Expense(1, "Bar 1", 100, new Moment("2017-04-13").getDate()));
    expenses.add(new Expense(2, "Bar 2", 50, new Moment("2017-05-19").getDate()));
    expenses.add(new Expense(3, "Uber", 60, new Moment("2017-08-05").getDate()));
    this.account.setExpenses(expenses);
  }

  @Test
  public void should_get_accounts_of_user() {
    List<Account> fetchedAccounts = this.accountPort.getAccountsOfUser(this.user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
  }

  @Test
  public void should_get_an_account_of_user_by_name() {
    Account fetchedAccount = this.accountPort.getAccountByUserAndAccountName(this.user, "Account test");

    assertThat(fetchedAccount.getName()).isEqualTo("Account test");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(3000.0);
  }

  @Test
  public void should_get_an_account_of_user_by_account_slug() {
    Account fetchedAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "account-test");

    assertThat(fetchedAccount.getName()).isEqualTo("Account test");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(3000.0);
  }

  @Test
  public void should_get_an_account_of_user_by_account_id() {
    Account fetchedAccount = this.accountPort.getAccountByUserAndAccountId(this.user, 3);

    assertThat(fetchedAccount.getName()).isEqualTo("Account test");
    assertThat(fetchedAccount.getInitialAmount()).isEqualTo(3000.0);
  }

  @Test
  public void should_create_and_account_for_user() {
    IAccountFetcher accountFetcher = new AccountFetcher(null, null);
    accountFetcher = Mockito.spy(accountFetcher);

    Account accountToCreate = new Account(this.user, "Account create", 1500.0, new Moment("2016-01-10").getDate());
    Mockito.doReturn(accountToCreate).when(accountFetcher).createAccount(any(Account.class));
    Mockito.doReturn(null).when(accountFetcher).getAccountByUserAndAccountSlug(accountToCreate.getUser(), accountToCreate.getSlug());

    AccountPort aPort = new AccountCalculator(accountFetcher, this.budgetFetcher, this.chargeFetcher, this.creditFetcher, this.expenseFetcher);

    Account createdAccount = aPort.createAccount(accountToCreate);
    Moment startDate = new Moment(createdAccount.getStartDate());

    assertThat(createdAccount).isNotNull();
    assertThat(createdAccount.getName()).isEqualTo("Account create");
    assertThat(createdAccount.getInitialAmount()).isEqualTo(1500.0);
    assertThat(createdAccount.getSlug()).isEqualTo("account-create");
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(1);
    assertThat(startDate.getYear()).isEqualTo(2016);
  }

  @Test
  public void should_throw_creation_exception_if_account_already_exists() {
    IAccountFetcher accountFetcher = new AccountFetcher(null, null);
    accountFetcher = Mockito.spy(accountFetcher);

    Account accountToCreate = new Account(1, this.user, "Account create", "account-create", 1500.0, new Moment("2016-01-01").getDate());
    Mockito.doReturn(accountToCreate).when(accountFetcher).createAccount(any(Account.class));
    Mockito.doReturn(accountToCreate).when(accountFetcher).getAccountByUserAndAccountSlug(accountToCreate.getUser(), accountToCreate.getSlug());

    AccountPort aPort = new AccountCalculator(accountFetcher, this.budgetFetcher, this.chargeFetcher, this.creditFetcher, this.expenseFetcher);

    try {
      aPort.createAccount(accountToCreate);
      fail("Should throw exception when account already exists");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("Account already exists with this name");
    }
  }

  @Test
  public void should_update_account_of_user() {
    IAccountFetcher accountFetcher = new AccountFetcher(null, null);
    accountFetcher = Mockito.spy(accountFetcher);

    Account accountToUpdate = new Account(this.user, "Account to update", 1500.0, new Moment("2016-01-01").getDate());
    accountToUpdate.setId(1);

    Mockito.doReturn(accountToUpdate).when(accountFetcher).updateAccount(any(Account.class));
    Mockito.doReturn(accountToUpdate).when(accountFetcher).getAccountByUserAndId(accountToUpdate.getUser(), accountToUpdate.getId());

    AccountPort aPort = new AccountCalculator(accountFetcher, this.budgetFetcher, this.chargeFetcher, this.creditFetcher, this.expenseFetcher);

    Account updatedAccount = aPort.updateAccount(accountToUpdate);

    assertThat(updatedAccount).isNotNull();
    assertThat(updatedAccount.getName()).isEqualTo("Account to update");
    assertThat(updatedAccount.getInitialAmount()).isEqualTo(1500.0);
    assertThat(updatedAccount.getSlug()).isEqualTo("account-to-update");
  }

  @Test
  public void should_delete_account() {
    boolean isDeleted = this.accountPort.deleteAccount(this.user, 1);
    assertThat(isDeleted).isTrue();
  }

  @Test
  public void should_calculate_start_amount_of_today_month() {
    Moment today = new Moment("2017-08-06");
    double startAmount = this.accountPort.calculateGivenMonthStartAmount(this.account, today.getDate());
    assertThat(startAmount).isEqualTo(-3380);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_after_today() {
    Moment nextMonth = new Moment("2017-09-06");
    double startAmount = this.accountPort.calculateGivenMonthStartAmount(this.account, nextMonth.getDate());
    assertThat(startAmount).isEqualTo(-2290);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_before_today() {
    Moment previousMonth = new Moment("2017-07-06");
    double startAmount = this.accountPort.calculateGivenMonthStartAmount(this.account, previousMonth.getDate());
    assertThat(startAmount).isEqualTo(-2070);
  }

  @Test
  public void should_calculate_current_amount_of_today_month() {
    Moment today = new Moment("2017-08-06");
    double startAmount = this.accountPort.calculateGivenMonthCurrentAmount(this.account, today.getDate());
    assertThat(startAmount).isEqualTo(-2290);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_after_today() {
    Moment nextMonth = new Moment("2017-09-06");
    double startAmount = this.accountPort.calculateGivenMonthCurrentAmount(this.account, nextMonth.getDate());
    assertThat(startAmount).isEqualTo(-3490);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_before_today() {
    Moment previousMonth = new Moment("2017-07-06");
    double startAmount = this.accountPort.calculateGivenMonthCurrentAmount(this.account, previousMonth.getDate());
    assertThat(startAmount).isEqualTo(-3380);
  }

  @Test
  public void should_calculate_free_amount_of_today_month() {
    Moment today = new Moment("2017-08-06");
    double startAmount = this.accountPort.calculateGivenMonthFreeAmount(this.account, today.getDate());
    assertThat(startAmount).isEqualTo(-2940);
  }

  @Test
  public void should_calculate_free_amount_of_given_month_after_today() {
    Moment nextMonth = new Moment("2017-09-06");
    double startAmount = this.accountPort.calculateGivenMonthFreeAmount(this.account, nextMonth.getDate());
    assertThat(startAmount).isEqualTo(-4190);
  }

  @Test
  public void should_calculate_free_amount_of_given_month_before_today() {
    Moment previousMonth = new Moment("2017-07-06");
    double startAmount = this.accountPort.calculateGivenMonthFreeAmount(this.account, previousMonth.getDate());
    assertThat(startAmount).isEqualTo(-3970);
  }
}
