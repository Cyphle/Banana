package com.banana.view.services;

import com.banana.domain.models.*;
import com.banana.infrastructure.orm.models.*;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.utils.Moment;
import com.banana.view.models.AccountView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class AccountServiceTests {
  @MockBean
  private SUserRepository sUserRepository;
  @MockBean
  private SAccountRepository sAccountRepository;
  @MockBean
  private SBudgetRepository sBudgetRepository;
  @MockBean
  private SChargeRepository sChargeRepository;
  @MockBean
  private SCreditRepository sCreditRepository;
  @MockBean
  private SExpenseRepository sExpenseRepository;
  @MockBean
  private UserService userService;

  private AccountService accountService;
  private SUser suser;
  private List<SAccount> accounts;
  private SAccount account;
  private List<SBudget> budgets;
  private List<SExpense> budgetExpenses;
  private List<SExpense> expenses;
  private List<SCharge> charges;
  private List<SCredit> credits;

  @Before
  public void setup() {
    this.accountService = new AccountService(this.sUserRepository, this.sAccountRepository, sBudgetRepository, sChargeRepository, sCreditRepository, sExpenseRepository, userService);
    this.suser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.accounts = new ArrayList<>();
    SAccount accountOne = new SAccount("Account one", 1000.0, new Moment("2016-01-01").getDate());
    accountOne.setId(1);
    accountOne.setUser(this.suser);
    this.accounts.add(accountOne);
    SAccount accountTwo = new SAccount("Account two", 2000.0, new Moment("2016-01-01").getDate());
    accountTwo.setId(2);
    accountTwo.setUser(this.suser);
    this.accounts.add(accountTwo);

    this.account = new SAccount(this.suser, "My account", "my-account", 2000, new Moment("2013-01-01").getDate());

    this.budgets = new ArrayList<>();
    SBudget budgetOne = new SBudget("Manger", 300, new Moment("2017-01-01").getDate());
    budgetOne.setAccount(this.account);
    budgets.add(budgetOne);
    SBudget budgetTwo = new SBudget("Clopes", 200, new Moment("2017-02-01").getDate());
    budgetTwo.setAccount(this.account);
    budgets.add(budgetTwo);

    this.budgetExpenses = new ArrayList<>();
    SExpense budgetExpenseOne = new SExpense("G20", 20, new Moment("2017-01-12").getDate());
    budgetExpenseOne.setBudget(budgetOne);
    this.budgetExpenses.add(budgetExpenseOne);
    SExpense budgetExpenseTwo = new SExpense("Monoprix", 30, new Moment("2017-02-13").getDate());
    budgetExpenseTwo.setBudget(budgetOne);
    this.budgetExpenses.add(budgetExpenseTwo);

    this.charges = new ArrayList<>();
    SCharge chargeOne = new SCharge("Loyer", 1200, new Moment("2016-01-01").getDate());
    chargeOne.setAccount(this.account);
    this.charges.add(chargeOne);
    SCharge chargeTwo = new SCharge("Internet", 40, new Moment("2017-01-01").getDate());
    chargeTwo.setAccount(this.account);
    this.charges.add(chargeTwo);

    this.expenses = new ArrayList<>();
    SExpense expenseOne = new SExpense("Bar", 50, new Moment("2017-07-20").getDate());
    expenseOne.setAccount(this.account);
    this.expenses.add(expenseOne);
    SExpense expenseTwo = new SExpense("Retrait", 60, new Moment("2017-07-23").getDate());
    expenseTwo.setAccount(this.account);
    this.expenses.add(expenseTwo);

    this.credits = new ArrayList<>();
    SCredit creditOne = new SCredit("Salaire", 2400, new Moment("2017-06-30").getDate());
    creditOne.setAccount(this.account);
    this.credits.add(creditOne);
  }

  @Test
  public void should_get_accounts_from_domain() {
    given(this.userService.getAuthenticatedUser()).willReturn(this.suser);
    given(this.sAccountRepository.findByUserUsername(any(String.class))).willReturn(this.accounts);

    List<AccountView> fetchedAccounts = this.accountService.getAccountsOfUser();

    assertThat(fetchedAccounts.size()).isEqualTo(2);
    assertThat(fetchedAccounts.get(0).getName()).isEqualTo("Account one");
    assertThat(fetchedAccounts.get(0).getInitialAmount()).isEqualTo(1000.0);
    assertThat(fetchedAccounts.get(1).getName()).isEqualTo("Account two");
    assertThat(fetchedAccounts.get(1).getInitialAmount()).isEqualTo(2000.0);
  }

  @Test
  public void should_get_account_from_domain_by_slug() {
    given(this.userService.getAuthenticatedUser()).willReturn(this.suser);
    given(this.sAccountRepository.findByUserUsernameAndSlug(any(String.class), any(String.class))).willReturn(this.account);
    given(this.sBudgetRepository.findByUserUsernameAndAccountId(any(String.class), any(long.class))).willReturn(this.budgets);
    given(this.sChargeRepository.findByUserUsernameAndAccountId(any(String.class), any(long.class))).willReturn(this.charges);
    given(this.sCreditRepository.findByUserUsernameAndAccountId(any(String.class), any(long.class))).willReturn(this.credits);
    given(this.sExpenseRepository.findByAccountId(any(long.class))).willReturn(this.expenses);
    given(this.sExpenseRepository.findByBudgetId(any(long.class))).willReturn(this.budgetExpenses);

    Account account = this.accountService.getAccountBySlug("my-account");

    assertThat(account.getName()).isEqualTo("My account");
    assertThat(account.getInitialAmount()).isEqualTo(2000);
    assertThat(account.getBudgets().size()).isEqualTo(2);
    assertThat(account.getCharges().size()).isEqualTo(2);
    assertThat(account.getExpenses().size()).isEqualTo(2);
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
}
