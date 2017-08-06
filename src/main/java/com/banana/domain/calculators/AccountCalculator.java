package com.banana.domain.calculators;

import com.banana.domain.adapters.*;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.helpers.AmountCalculator;
import com.banana.domain.models.*;
import com.banana.domain.ports.AccountPort;
import com.banana.domain.validators.AccountVerifier;
import com.banana.utils.Moment;
import com.github.slugify.Slugify;

import java.util.Date;
import java.util.List;

public class AccountCalculator implements AccountPort {
  private IAccountFetcher accountFetcher;
  private IChargeFetcher chargeFetcher;
  private IBudgetFetcher budgetFetcher;
  private ICreditFetcher creditFetcher;
  private IExpenseFetcher expenseFetcher;

  public AccountCalculator(IAccountFetcher accountFetcher, IBudgetFetcher budgetFetcher, IChargeFetcher chargeFetcher, ICreditFetcher creditFetcher, IExpenseFetcher expenseFetcher) {
    this.accountFetcher = accountFetcher;
    this.budgetFetcher = budgetFetcher;
    this.chargeFetcher = chargeFetcher;
    this.creditFetcher = creditFetcher;
    this.expenseFetcher = expenseFetcher;
  }

  public List<Account> getAccountsOfUser(User user) {
    return this.accountFetcher.getAccountsOfUser(user);
  }

  public Account getAccountByUserAndAccountName(User user, String accountName) {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountName(user, accountName);
    this.getAccountItems(user, myAccount);
    return myAccount;
  }

  public Account getAccountByUserAndAccountSlug(User user, String accountSlug) {
    Account myAccount = this.accountFetcher.getAccountByUserAndAccountSlug(user, accountSlug);
    this.getAccountItems(user, myAccount);
    return myAccount;
  }

  public Account getAccountByUserAndAccountId(User user, long accountId) {
    Account myAccount = this.accountFetcher.getAccountByUserAndId(user, accountId);
    this.getAccountItems(user, myAccount);
    return myAccount;
  }

  public Account createAccount(Account account) throws CreationException {
    Account checkAccount = this.accountFetcher.getAccountByUserAndAccountSlug(account.getUser(), account.getSlug());
    if (checkAccount != null)
      throw new CreationException("Account already exists with this name");
    else {
      Slugify slg = new Slugify();
      String accountSlug = slg.slugify(account.getName());
      account.setSlug(accountSlug);
      account.setStartDate(new Moment(account.getStartDate()).getFirstDateOfMonth().getDate());
      return this.accountFetcher.createAccount(account);
    }
  }

  public Account updateAccount(Account account) {
    Account accountToUpdate = this.accountFetcher.getAccountByUserAndId(account.getUser(), account.getId());
    if (accountToUpdate == null) {
      throw new NoElementFoundException("No account found to update at id : " + account.getId());
    } else {
      accountToUpdate.setInitialAmount(account.getInitialAmount());
      accountToUpdate.setName(account.getName());
      Slugify slg = new Slugify();
      String accountSlug = slg.slugify(account.getName());
      accountToUpdate.setSlug(accountSlug);
      return this.accountFetcher.updateAccount(accountToUpdate);
    }
  }

  public boolean deleteAccount(User user, long accountId) {
    Account myAccount = this.accountFetcher.getAccountByUserAndId(user, accountId);
    boolean isDeleted = false;
    if (myAccount != null) {
      for (Budget budget : myAccount.getBudgets()) {
        for (Expense expense : budget.getExpenses()) {
          isDeleted = this.expenseFetcher.deleteExpense(expense);
          if (!isDeleted) break;
        }
        isDeleted = this.budgetFetcher.deleteBudget(budget);
        if (!isDeleted) break;
      }
      for (Charge charge : myAccount.getCharges()) {
        isDeleted = this.chargeFetcher.deleteCharge(charge);
        if (!isDeleted) break;
      }
      for (Credit credit : myAccount.getCredits()) {
        isDeleted = this.creditFetcher.deleteCredit(credit);
        if (!isDeleted) break;
      }
      for (Expense expense : myAccount.getExpenses()) {
        isDeleted = this.expenseFetcher.deleteExpense(expense);
        if (!isDeleted) break;
      }
      isDeleted = this.accountFetcher.deleteAccount(myAccount);
    }
    return isDeleted;
  }

  public double calculateGivenMonthStartAmount(Account account, Date month) {
    Moment askedDate = new Moment();
    if (month != null) askedDate = new Moment(month);
    final Moment calculationMonth = askedDate.getLastDayOfPrecedingMonth();
    return this.calculateAmounts(account, calculationMonth);
  }

  public double calculateGivenMonthCurrentAmount(Account account, Date month) {
    Moment askedDate = new Moment();
    if (month != null) askedDate = new Moment(month);
    final Moment calculationMonth = askedDate.getLastDateOfMonth();
    return this.calculateAmounts(account, calculationMonth);
  }

  public double calculateGivenMonthFreeAmount(Account account, Date month) {
    Moment askedDate = new Moment();
    if (month != null) askedDate = new Moment(month);
    final Moment calculationMonth = askedDate.getLastDateOfMonth();
    return this.calculateFreeAmounts(account, calculationMonth);
  }

  private void getAccountItems(User user, Account myAccount) {
    // BUDGETS
    List<Budget> budgets = this.budgetFetcher.getBudgetsOfUserAndAccount(user, myAccount.getId());

    for (Budget budget : budgets) {
      List<Expense> budgetExpenses = this.expenseFetcher.getExpensesOfBudget(budget);
      budget.setExpenses(budgetExpenses);
    }
    myAccount.setBudgets(budgets);
    // CHARGES
    List<Charge> charges = this.chargeFetcher.getChargesOfUserAndAccount(user, myAccount.getId());
    myAccount.setCharges(charges);
    // CREDITS
    List<Credit> credits = this.creditFetcher.getCreditsOfUserAndAccount(user, myAccount.getId());
    myAccount.setCredits(credits);
    // EXPENSES
    List<Expense> expenses = this.expenseFetcher.getExpensesOfAccount(myAccount);
    myAccount.setExpenses(expenses);
  }

  private double calculateAmounts(Account account, Moment calculationMonth) {
    // If account start date if before calculating month take its initial amount, else 0.0
    double startAmount = calculationMonth.compareTo(new Moment(account.getStartDate())) >= 0 ? account.getInitialAmount() : 0.0;

    AmountCalculator calculator = new AmountCalculator(account);
    startAmount -= calculator.calculateGivenMonthCharges(calculationMonth.getDate());
    startAmount -= calculator.calculateGivenMonthExpenses(calculationMonth.getDate());
    startAmount += calculator.calculateGivenMonthCredits(calculationMonth.getDate());
    startAmount -= calculator.calculateGivenMonthBudgetExpenses(calculationMonth.getDate());

    return startAmount;
  }

  private double calculateFreeAmounts(Account account, Moment calculationMonth) {
    // If account start date if before calculating month take its initial amount, else 0.0
    double startAmount = calculationMonth.compareTo(new Moment(account.getStartDate())) >= 0 ? account.getInitialAmount() : 0.0;

    AmountCalculator calculator = new AmountCalculator(account);
    startAmount -= calculator.calculateGivenMonthCharges(calculationMonth.getDate());
    startAmount -= calculator.calculateGivenMonthExpenses(calculationMonth.getDate());
    startAmount += calculator.calculateGivenMonthCredits(calculationMonth.getDate());
    startAmount -= calculator.calculateGivenMonthFreeAmountForBudgets(calculationMonth.getLastDayOfPrecedingMonth().getDate());

    return startAmount;
  }
}
