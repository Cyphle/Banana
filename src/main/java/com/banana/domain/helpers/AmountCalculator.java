package com.banana.domain.helpers;

import com.banana.domain.models.*;
import com.banana.utils.Moment;

import java.util.Date;

public class AmountCalculator {
  private Account account;

  public AmountCalculator(Account account) {
    this.account = account;
  }

  public double calculateGivenMonthBudgetExpenses(Date calculationDate) {
    return this.calculateBudgetExpenses(calculationDate);
  }

  public double calculateGivenMonthFreeAmountForBudgets(Date calculationDate) {
    double startAmount = this.calculateBudgetExpenses(calculationDate);
    // Current month budget
    final Moment calculationMonthForRunningBudgets = new Moment(calculationDate).getLastDayOfNextMonth();
    startAmount += this.account.getBudgets()
            .stream()
            .filter(budget -> (budget.getEndDate() != null && calculationMonthForRunningBudgets.compareTo(new Moment(budget.getEndDate())) <= 0) || budget.getEndDate() == null)
            .map(Budget::getInitialAmount)
            .reduce(0.0, (a, b) -> a + b);
    return startAmount;
  }

  public double calculateGivenMonthCredits(Date calculationDate) {
    final Moment calculationMonth = new Moment(calculationDate);
    return account.getCredits()
            .stream()
            .filter(credit -> calculationMonth.compareTo(new Moment(credit.getCreditDate())) >= 0)
            .map(Credit::getAmount)
            .reduce(0.0, (a, b) -> a + b);
  }

  public double calculateGivenMonthCharges(Date calculationDate) {
    final Moment calculationMonth = new Moment(calculationDate);
    double startAmount = 0.0;
    for (Charge charge : account.getCharges()) {
      if (charge.getEndDate() != null && calculationMonth.compareTo(new Moment(charge.getEndDate()).getLastDateOfMonth()) > 0) {
        // If calculation month is before end date, multiply by number of month
        startAmount += (new Moment(charge.getEndDate()).getNumberOfMonthsBetweenExcludingCurrent(new Moment(charge.getStartDate())) + 1) * charge.getAmount();
      } else {
        // If charge is still active
        startAmount += (calculationMonth.getNumberOfMonthsBetweenExcludingCurrent(new Moment(charge.getStartDate())) + 1) * charge.getAmount();
      }
    }
    return startAmount;
  }

  public double calculateGivenMonthExpenses(Date calculationDate) {
    final Moment calculationMonth = new Moment(calculationDate);
    return account.getExpenses()
            .stream()
            .filter(expense -> calculationMonth.compareTo(new Moment(expense.getExpenseDate())) >= 0)
            .map(Expense::getAmount)
            .reduce(0.0, (a, b) -> a + b);
  }

  private double calculateBudgetExpenses(Date calculationDate) {
    final Moment calculationMonth = new Moment(calculationDate);
    double startAmount = 0.0;
    // For previous month budget expenses
    for (Budget budget : this.account.getBudgets()) {
      startAmount += budget
              .getExpenses()
              .stream()
              .filter(expense -> calculationMonth.compareTo(new Moment(expense.getExpenseDate())) >= 0)
              .map(Expense::getAmount)
              .reduce(0.0, (a, b) -> a + b);
    }
    return startAmount;
  }
}
