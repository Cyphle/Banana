package com.banana.domain.ports;

import com.banana.domain.adapters.*;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.models.*;
import com.banana.infrastructure.connector.adapters.*;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.models.*;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountPortITests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SAccountRepository sAccountRepository;
  @Autowired
  private SUserRepository sUserRepository;
  @Autowired
  private SBudgetRepository sBudgetRepository;
  @Autowired
  private SChargeRepository sChargeRepository;
  @Autowired
  private SCreditRepository sCreditRepository;
  @Autowired
  private SExpenseRepository sExpenseRepository;

  private IUserRepository userRepository;
  private IAccountRepository accountRepository;
  private IBudgetRepository budgetRepository;
  private IChargeRepository chargeRepository;
  private ICreditRepository creditRepository;
  private IExpenseRepository expenseRepository;

  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private IChargeFetcher chargeFetcher;
  private ICreditFetcher creditFetcher;
  private IExpenseFetcher expenseFetcher;

  private AccountPort accountPort;

  private User user;
  private SUser fakeUser;
  private SAccount accountOne;
  private SAccount accountTwo;
  private SBudget budgetOne;
  private SBudget budgetTwo;
  private SCharge chargeOne;
  private SCharge chargeTwo;
  private SExpense expenseBudgetOne;
  private SExpense expenseBudgetTwo;
  private SExpense expenseOne;
  private SExpense expenseTwo;
  private SCredit creditOne;

  @Before
  public void setUp() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");
    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(fakeUser);

    Moment today = new Moment();

    this.accountOne = new SAccount("Account one", 1000, new Moment("2016-01-01").getDate());
    this.accountOne.setSlug("account-one");
    this.accountOne.setUser(this.fakeUser);
    this.accountOne.setCreationDate(today.getDate());
    this.accountOne.setUpdateDate(today.getDate());
    this.entityManager.persist(this.accountOne);

    this.accountTwo = new SAccount("Account two", 2000, new Moment("2016-01-01").getDate());
    this.accountTwo.setUser(this.fakeUser);
    this.accountTwo.setCreationDate(today.getDate());
    this.accountTwo.setUpdateDate(today.getDate());
    this.entityManager.persist(this.accountTwo);

    this.budgetOne = new SBudget("Manger", 300, new Moment("2017-01-01").getDate());
    this.budgetOne.setAccount(this.accountOne);
    this.entityManager.persist(this.budgetOne);

    this.budgetTwo = new SBudget("Clopes", 200, new Moment("2017-02-01").getDate());
    this.budgetTwo.setAccount(this.accountOne);
    this.entityManager.persist(this.budgetTwo);

    this.chargeOne = new SCharge("Loyer", 1200, new Moment("2016-01-01").getDate());
    this.chargeOne.setAccount(this.accountOne);
    this.entityManager.persist(this.chargeOne);

    this.chargeTwo = new SCharge("Internet", 40, new Moment("2017-01-01").getDate());
    this.chargeTwo.setAccount(this.accountOne);
    this.entityManager.persist(this.chargeTwo);

    this.expenseBudgetOne = new SExpense("G20", 20, new Moment("2017-01-12").getDate());
    this.expenseBudgetOne.setBudget(this.budgetOne);
    this.entityManager.persist(this.expenseBudgetOne);

    this.expenseBudgetTwo = new SExpense("Monoprix", 30, new Moment("2017-02-13").getDate());
    this.expenseBudgetTwo.setBudget(this.budgetOne);
    this.entityManager.persist(this.expenseBudgetTwo);

    this.expenseOne = new SExpense("Bar", 50, new Moment("2017-07-20").getDate());
    this.expenseOne.setAccount(this.accountOne);
    this.entityManager.persist(this.expenseOne);

    this.expenseTwo = new SExpense("Retrait", 60, new Moment("2017-07-23").getDate());
    this.expenseTwo.setAccount(this.accountOne);
    this.entityManager.persist(this.expenseTwo);

    this.creditOne = new SCredit("Salaire", 2400, new Moment("2017-06-30").getDate());
    this.creditOne.setAccount(this.accountOne);
    this.entityManager.persist(this.creditOne);

    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.budgetRepository = new BudgetRepository(this.sBudgetRepository);
    this.chargeRepository = new ChargeRepository(this.sChargeRepository);
    this.creditRepository = new CreditRepository(this.sCreditRepository);
    this.expenseRepository = new ExpenseRepository(this.sExpenseRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.budgetFetcher = new BudgetFetcher(this.accountRepository, this.budgetRepository);
    this.chargeFetcher = new ChargeFetcher(this.accountRepository, this.chargeRepository);
    this.creditFetcher = new CreditFetcher(this.accountRepository, this.creditRepository);
    this.expenseFetcher = new ExpenseFetcher(this.accountRepository, this.budgetRepository, this.expenseRepository);
    this.accountPort = new AccountCalculator(this.accountFetcher, this.budgetFetcher, this.chargeFetcher, this.creditFetcher, this.expenseFetcher);
  }

  @Test
  public void should_get_accounts_of_user_from_fake_repository() {
    List<Account> fetchedAccounts = this.accountPort.getAccountsOfUser(this.user);

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }

  @Test
  public void should_get_account_by_name_with_all_its_info() {
    Account account = this.accountPort.getAccountByUserAndAccountName(this.user, "Account one");
    Moment accountStartDate = new Moment(account.getStartDate());

    assertThat(account.getName()).isEqualTo("Account one");
    assertThat(account.getInitialAmount()).isEqualTo(1000);
    assertThat(account.getSlug()).isEqualTo("account-one");
    assertThat(account.getUser().getUsername()).isEqualTo("john@doe.fr");
    assertThat(accountStartDate.getDayOfMonth()).isEqualTo(1);
    assertThat(accountStartDate.getMonthNumber()).isEqualTo(1);
    assertThat(accountStartDate.getYear()).isEqualTo(2016);
    assertThat(account.getBudgets().size()).isEqualTo(2);
    assertThat(account.getExpenses().size()).isEqualTo(2);
    assertThat(account.getCharges().size()).isEqualTo(2);
    assertThat(account.getCredits().size()).isEqualTo(1);

    // BUDGETS
    Budget budgetManger = account.getBudgets().stream().filter(budget -> budget.getName() == "Manger").collect(Collectors.toList()).get(0);
    Budget budgetClopes = account.getBudgets().stream().filter(budget -> budget.getName() == "Clopes").collect(Collectors.toList()).get(0);
    assertThat(budgetManger.getInitialAmount()).isEqualTo(300);
    assertThat(budgetManger.getExpenses().size()).isEqualTo(2);
    assertThat(budgetClopes.getInitialAmount()).isEqualTo(200);

    // BUDGET EXPENSES
    Expense expenseG20 = budgetManger.getExpenses().stream().filter(expense -> expense.getDescription() == "G20").collect(Collectors.toList()).get(0);
    Expense expenseMonoprix = budgetManger.getExpenses().stream().filter(expense -> expense.getDescription() == "Monoprix").collect(Collectors.toList()).get(0);
    assertThat(expenseG20.getAmount()).isEqualTo(20);
    assertThat(expenseMonoprix.getAmount()).isEqualTo(30);

    // EXPENSES
    Expense expenseBar = account.getExpenses().stream().filter(expense -> expense.getDescription() == "Bar").collect(Collectors.toList()).get(0);
    Expense expenseRetrait = account.getExpenses().stream().filter(expense -> expense.getDescription() == "Retrait").collect(Collectors.toList()).get(0);
    assertThat(expenseBar.getAmount()).isEqualTo(50);
    assertThat(expenseRetrait.getAmount()).isEqualTo(60);

    // CHARGES
    Charge chargeLoyer = account.getCharges().stream().filter(charge -> charge.getDescription() == "Loyer").collect(Collectors.toList()).get(0);
    Charge chargeInternet = account.getCharges().stream().filter(charge -> charge.getDescription() == "Internet").collect(Collectors.toList()).get(0);
    assertThat(chargeLoyer.getAmount()).isEqualTo(1200);
    assertThat(chargeInternet.getAmount()).isEqualTo(40);

    // CREDITS
    Credit creditSalaire = account.getCredits().get(0);
    assertThat(creditSalaire.getAmount()).isEqualTo(2400);
  }

  @Test
  public void should_get_account_by_slug_with_all_its_info() {
    Account account = this.accountPort.getAccountByUserAndAccountSlug(this.user, "account-one");
    Moment accountStartDate = new Moment(account.getStartDate());

    assertThat(account.getName()).isEqualTo("Account one");
    assertThat(account.getInitialAmount()).isEqualTo(1000);
    assertThat(account.getSlug()).isEqualTo("account-one");
    assertThat(account.getUser().getUsername()).isEqualTo("john@doe.fr");
    assertThat(accountStartDate.getDayOfMonth()).isEqualTo(1);
    assertThat(accountStartDate.getMonthNumber()).isEqualTo(1);
    assertThat(accountStartDate.getYear()).isEqualTo(2016);
    assertThat(account.getBudgets().size()).isEqualTo(2);
    assertThat(account.getExpenses().size()).isEqualTo(2);
    assertThat(account.getCharges().size()).isEqualTo(2);
    assertThat(account.getCredits().size()).isEqualTo(1);

    // BUDGETS
    Budget budgetManger = account.getBudgets().stream().filter(budget -> budget.getName() == "Manger").collect(Collectors.toList()).get(0);
    Budget budgetClopes = account.getBudgets().stream().filter(budget -> budget.getName() == "Clopes").collect(Collectors.toList()).get(0);
    assertThat(budgetManger.getInitialAmount()).isEqualTo(300);
    assertThat(budgetManger.getExpenses().size()).isEqualTo(2);
    assertThat(budgetClopes.getInitialAmount()).isEqualTo(200);

    // BUDGET EXPENSES
    Expense expenseG20 = budgetManger.getExpenses().stream().filter(expense -> expense.getDescription() == "G20").collect(Collectors.toList()).get(0);
    Expense expenseMonoprix = budgetManger.getExpenses().stream().filter(expense -> expense.getDescription() == "Monoprix").collect(Collectors.toList()).get(0);
    assertThat(expenseG20.getAmount()).isEqualTo(20);
    assertThat(expenseMonoprix.getAmount()).isEqualTo(30);

    // EXPENSES
    Expense expenseBar = account.getExpenses().stream().filter(expense -> expense.getDescription() == "Bar").collect(Collectors.toList()).get(0);
    Expense expenseRetrait = account.getExpenses().stream().filter(expense -> expense.getDescription() == "Retrait").collect(Collectors.toList()).get(0);
    assertThat(expenseBar.getAmount()).isEqualTo(50);
    assertThat(expenseRetrait.getAmount()).isEqualTo(60);

    // CHARGES
    Charge chargeLoyer = account.getCharges().stream().filter(charge -> charge.getDescription() == "Loyer").collect(Collectors.toList()).get(0);
    Charge chargeInternet = account.getCharges().stream().filter(charge -> charge.getDescription() == "Internet").collect(Collectors.toList()).get(0);
    assertThat(chargeLoyer.getAmount()).isEqualTo(1200);
    assertThat(chargeInternet.getAmount()).isEqualTo(40);

    // CREDITS
    Credit creditSalaire = account.getCredits().get(0);
    assertThat(creditSalaire.getAmount()).isEqualTo(2400);
  }

  @Test
  public void should_create_account() {
    Account accountToCreate = new Account(this.user, "Account create", 1000.0, new Moment("2016-01-20").getDate());

    Account createdAccount = this.accountPort.createAccount(accountToCreate);
    SAccount sCreatedAccount = this.sAccountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "account-create");
    Moment startDate = new Moment(createdAccount.getStartDate());

    assertThat(createdAccount).isNotNull();
    assertThat(createdAccount.getId()).isGreaterThan(0);
    assertThat(createdAccount.getName()).isEqualTo("Account create");
    assertThat(createdAccount.getSlug()).isEqualTo("account-create");
    assertThat(createdAccount.getInitialAmount()).isEqualTo(1000.0);
    assertThat(createdAccount.getUser().getUsername()).isEqualTo("john@doe.fr");
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(1);
    assertThat(startDate.getYear()).isEqualTo(2016);
    assertThat(sCreatedAccount.getId()).isEqualTo(createdAccount.getId());
    assertThat(sCreatedAccount.getInitialAmount()).isEqualTo(createdAccount.getInitialAmount());
    assertThat(sCreatedAccount.getSlug()).isEqualTo(createdAccount.getSlug());
  }

  @Test
  public void should_update_account() {
    SAccount accountToUpdate = this.sAccountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "account-one");

    Account accountWithNewData = new Account(this.user, "New account name", 2000.0, new Moment("2016-01-01").getDate());
    accountWithNewData.setId(accountToUpdate.getId());

    Account updatedAccount = this.accountPort.updateAccount(accountWithNewData);

    assertThat(updatedAccount.getId()).isEqualTo(accountToUpdate.getId());
    assertThat(updatedAccount.getName()).isEqualTo("New account name");
    assertThat(updatedAccount.getSlug()).isEqualTo("new-account-name");
    assertThat(updatedAccount.getInitialAmount()).isEqualTo(2000.0);
  }

  @Test
  public void should_delete_an_account() {
    SAccount myAccount = this.sAccountRepository.findByUserUsernameAndSlug(this.user.getUsername(), "account-one");

    boolean isDeleted = this.accountPort.deleteAccount(this.user, myAccount.getId());

    assertThat(isDeleted).isTrue();
  }
}