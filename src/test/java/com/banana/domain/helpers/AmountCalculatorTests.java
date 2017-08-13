package com.banana.domain.helpers;

import com.banana.domain.models.*;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AmountCalculatorTests {
  private User user;
  private Account account;
  private AmountCalculator amountCalculator;

  @Before
  public void setup() {
    this.user = new User(1, "John", "Doe", "john@doe.fr");

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
    budgetTwoExpenses.add(new Expense(5, "Clopes", 100, new Moment("2016-06-28").getDate()));
    budgetTwo.setExpenses(budgetTwoExpenses);
    budgets.add(budgetTwo);

    Budget budgetThree = new Budget(3, "Clopes", 200, new Moment("2016-01-01").getDate());
    budgetThree.setEndDate(new Moment("2016-12-31").getDate());
    List<Expense> budgetThreeExpenses = new ArrayList<>();
    budgetThreeExpenses.add(new Expense(6, "Clopes", 35, new Moment("2017-04-12").getDate()));
    budgetThreeExpenses.add(new Expense(7, "Ange20", 35, new Moment("2016-10-28").getDate()));
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

    this.amountCalculator = new AmountCalculator(account);
  }

  @Test
  public void should_calculate_start_amount_of_today_month_for_budgets() {
    Moment today = new Moment("2017-08-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthBudgetExpenses(today.getDate());
    assertThat(startAmount).isEqualTo(310);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_after_today_for_budgets() {
    Moment nextMonth = new Moment("2017-09-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthBudgetExpenses(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(360);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_before_today_for_budgets() {
    Moment previousMonth = new Moment("2017-07-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthBudgetExpenses(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(200);
  }

  @Test
  public void should_calculate_start_amount_of_today_month_for_credits() {
    Moment today = new Moment("2017-08-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCredits(today.getDate());
    assertThat(startAmount).isEqualTo(2900);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_after_today_for_credits() {
    Moment nextMonth = new Moment("2017-09-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCredits(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(5300);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_before_today_for_credits() {
    Moment previousMonth = new Moment("2017-07-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCredits(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(2900);
  }

  @Test
  public void should_calculate_start_amount_of_today_month_for_charges() {
    Moment today = new Moment("2017-08-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCharges(today.getDate());
    assertThat(startAmount).isEqualTo(8820);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_after_today_for_charges() {
    Moment nextMonth = new Moment("2017-09-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCharges(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(10020);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_before_today_for_charges() {
    Moment previousMonth = new Moment("2017-07-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCharges(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(7620);
  }

  @Test
  public void should_calculate_start_amount_of_today_month_for_expenses() {
    Moment today = new Moment("2017-08-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthExpenses(today.getDate());
    assertThat(startAmount).isEqualTo(150);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_after_today_for_expenses() {
    Moment nextMonth = new Moment("2017-09-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthExpenses(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(210);
  }

  @Test
  public void should_calculate_start_amount_of_given_month_before_today_for_expenses() {
    Moment previousMonth = new Moment("2017-07-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthExpenses(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(150);
  }

  @Test
  public void should_calculate_current_amount_of_today_month_for_budgets() {
    Moment today = new Moment("2017-08-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthBudgetExpenses(today.getDate());
    assertThat(startAmount).isEqualTo(360);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_after_today_for_budgets() {
    Moment nextMonth = new Moment("2017-09-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthBudgetExpenses(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(360);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_before_today_for_budgets() {
    Moment previousMonth = new Moment("2017-07-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthBudgetExpenses(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(310);
  }

  @Test
  public void should_calculate_current_amount_of_today_month_for_credits() {
    Moment today = new Moment("2017-08-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCredits(today.getDate());
    assertThat(startAmount).isEqualTo(5300);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_after_today_for_credits() {
    Moment nextMonth = new Moment("2017-09-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCredits(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(5300);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_before_today_for_credits() {
    Moment previousMonth = new Moment("2017-07-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCredits(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(2900);
  }

  @Test
  public void should_calculate_current_amount_of_today_month_for_charges() {
    Moment today = new Moment("2017-08-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCharges(today.getDate());
    assertThat(startAmount).isEqualTo(10020);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_after_today_for_charges() {
    Moment nextMonth = new Moment("2017-09-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCharges(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(11220);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_before_today_for_charges() {
    Moment previousMonth = new Moment("2017-07-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthCharges(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(8820);
  }

  @Test
  public void should_calculate_current_amount_of_today_month_for_expenses() {
    Moment today = new Moment("2017-08-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthExpenses(today.getDate());
    assertThat(startAmount).isEqualTo(210);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_after_today_for_expenses() {
    Moment nextMonth = new Moment("2017-09-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthExpenses(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(210);
  }

  @Test
  public void should_calculate_current_amount_of_given_month_before_today_for_expenses() {
    Moment previousMonth = new Moment("2017-07-06").getLastDateOfMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthExpenses(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(150);
  }

  @Test
  public void should_calculate_free_amount_of_today_month_for_budgets() {
    Moment today = new Moment("2017-08-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthFreeAmountForBudgets(today.getDate());
    assertThat(startAmount).isEqualTo(1010);
  }

  @Test
  public void should_calculate_free_amount_of_given_month_after_today_for_budgets() {
    Moment nextMonth = new Moment("2017-09-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthFreeAmountForBudgets(nextMonth.getDate());
    assertThat(startAmount).isEqualTo(1060);
  }

  @Test
  public void should_calculate_free_amount_of_given_month_before_today_for_budgets() {
    Moment previousMonth = new Moment("2017-07-06").getLastDayOfPreviousMonth();
    double startAmount = this.amountCalculator.calculateGivenMonthFreeAmountForBudgets(previousMonth.getDate());
    assertThat(startAmount).isEqualTo(900);
  }
}
