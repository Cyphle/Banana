'use strict';

import { Account } from "../models/Account";
import { User } from "../models/User";
import { Instant } from "../utils/Instant";
import { Budget } from "../models/Budget";
import { Expense } from "../models/Expense";
import { Charge } from "../models/Charge";
import { Credit } from "../models/Credit";
import { MonthData } from "../models/MonthData";

export default class AccountDataSorter {
  constructor() {
    this.account = null;
  }

  buildAccountData(accountData) {
    let user = new User(accountData.user.id, accountData.user.lastname, accountData.user.firstname, accountData.user.username);
    let account = new Account(accountData.id, user, accountData.name, accountData.slug, accountData.initialAmount, new Instant(accountData.startDate));

    accountData.budgets.forEach(jsonBudget => {
      let budget = new Budget(jsonBudget.id, jsonBudget.name, jsonBudget.initialAmount, new Instant(jsonBudget.startDate), jsonBudget.endDate !== null ? new Instant(jsonBudget.endDate) : null);
      jsonBudget.expenses.forEach(jsonExpense => budget.addExpense(new Expense(jsonExpense.id, jsonExpense.description, jsonExpense.amount, new Instant(jsonExpense.expenseDate), jsonExpense.debitDate !== null ? new Instant(jsonExpense.debitDate) : null)));
      account.addBudget(budget);
    });

    accountData.charges.forEach(jsonCharge => account.addCharge(new Charge(jsonCharge.id, jsonCharge.description, jsonCharge.amount, new Instant(jsonCharge.startDate), jsonCharge.endDate !== null ? new Instant(jsonCharge.endDate) : null)));
    accountData.expenses.forEach(jsonExpense => account.addExpense(new Expense(jsonExpense.id, jsonExpense.description, jsonExpense.amount, new Instant(jsonExpense.expenseDate), jsonExpense.debitDate !== null ? new Instant(jsonExpense.debitDate) : null)));
    accountData.credits.forEach(jsonCredit => account.addCredit(new Credit(jsonCredit.id, jsonCredit.description, jsonCredit.amount, new Instant(jsonCredit.creditDate))));

    this.account = account;
  }

  getDataOfMonth(requestedMonth) {
    let tempAccount = this.account.clone();
    let monthData = new MonthData(this.account.id, this.account.name, requestedMonth);
    monthData.budgets = this.getBudgetsOfMonth(tempAccount, requestedMonth);
    monthData.charges = this.getChargesOfMonth(tempAccount, requestedMonth);
    monthData.expenses = this.getExpensesOfMonth(tempAccount, requestedMonth);
    monthData.credits = this.getCreditsOfMonth(tempAccount, requestedMonth);
    [monthData.startAmount, monthData.currentAmount, monthData.freeAmount] = this.calculateAccountAmountsOfMonth(requestedMonth);
    return monthData;
  }

  getBudgetsOfMonth(account, requestedMonth) {
    let budgets = account.budgets.filter(budget => !!(requestedMonth.isAfterOrSame(budget.startDate) && (budget.endDate === null || requestedMonth.isBeforeOrSame(budget.endDate))));
    budgets.forEach(budget => budget.expenses = budget.expenses.filter(expense => !!requestedMonth.hasSameMonthAs(expense.expenseDate)));
    return budgets;
  }

  getChargesOfMonth(account, requestedMonth) {
    return account.charges.filter(charge => !!(requestedMonth.isAfterOrSame(charge.startDate.getFirstDateOfMonth()) && (charge.endDate === null || requestedMonth.isBeforeOrSame(charge.endDate))));
  }

  getExpensesOfMonth(account, requestedMonth) {
    return account.expenses.filter(expense => requestedMonth.hasSameMonthAs(expense.expenseDate));
  }

  getCreditsOfMonth(account, requestedMonth) {
    return account.credits.filter(credit => requestedMonth.hasSameMonthAs(credit.creditDate));
  }

  calculateAccountAmountsOfMonth(requestedMonth) {
    return [
      this.calculateStartAmountOfMonth(requestedMonth.getLastDateOfPreviousMonth()),
      this.calculateCurrentAmountOfMonth(requestedMonth.getLastDateOfMonth()),
      this.calculateFreeAmountsOfMonth(requestedMonth.getLastDateOfMonth())
    ];
  }

  calculateStartAmountOfMonth(requestedMonth) {
    let startAmount= this.account.startDate.isBeforeOrSame(requestedMonth) ? this.account.initialAmount : 0;
    startAmount -= this.calculateGivenMonthCharges(requestedMonth);
    startAmount -= this.calculateGivenMonthExpenses(requestedMonth);
    startAmount += this.calculateGivenMonthCredits(requestedMonth);
    startAmount -= this.calculateGivenMonthBudgetExpenses(requestedMonth);
    return startAmount;
  }

  calculateCurrentAmountOfMonth(requestedMonth) {
    let startAmount= this.account.startDate.isBeforeOrSame(requestedMonth) ? this.account.initialAmount : 0;
    startAmount -= this.calculateGivenMonthCharges(requestedMonth);
    startAmount -= this.calculateGivenMonthExpenses(requestedMonth);
    startAmount += this.calculateGivenMonthCredits(requestedMonth);
    startAmount -= this.calculateGivenMonthBudgetExpenses(requestedMonth);
    return startAmount;
  }

  calculateFreeAmountsOfMonth(requestedMonth) {
    let startAmount= this.account.startDate.isBeforeOrSame(requestedMonth) ? this.account.initialAmount : 0;
    startAmount -= this.calculateGivenMonthCharges(requestedMonth);
    startAmount -= this.calculateGivenMonthExpenses(requestedMonth);
    startAmount += this.calculateGivenMonthCredits(requestedMonth);
    startAmount -= this.calculateGivenMonthFreeAmountForBudgets(requestedMonth.getLastDateOfPreviousMonth());
    return startAmount;
  }

  calculateGivenMonthCharges(requestedMonth) {
    let amount = 0;
    this.account.charges.forEach(charge => {
      if (charge.endDate !== null && requestedMonth.isAfter(requestedMonth)) {
        amount += (charge.startDate.getDifferenceOfMonthBetweenAndExcluding(charge.endDate) + 1) * charge.amount;
      } else {
        amount += (requestedMonth.getDifferenceOfMonthBetweenAndExcluding(charge.startDate) + 1) * charge.amount;
      }
    });
    return amount;
  }

  calculateGivenMonthExpenses(requestedMonth) {
    return this.account
        .expenses
        .filter(expense => expense.expenseDate.isBeforeOrSame(requestedMonth))
        .map(expense => expense.amount)
        .reduce((a, b) => a + b, 0);
  }

  calculateGivenMonthCredits(requestedMonth) {
    return this.account
        .credits
        .filter(credit => credit.creditDate.isBeforeOrSame(requestedMonth))
        .map(credit => credit.amount)
        .reduce((a, b) => a + b, 0);
  }

  calculateGivenMonthBudgetExpenses(requestedMonth) {
    let amount = 0;
    this.account.budgets.forEach(budget => {
      amount += budget.expenses
          .filter(expense => expense.expenseDate.isBeforeOrSame(requestedMonth))
          .map(expense => expense.amount)
          .reduce((a, b) => a + b, 0);
    });
    return amount;
  }

  calculateGivenMonthFreeAmountForBudgets(requestedMonth) {
    let startAmount = this.calculateGivenMonthBudgetExpenses(requestedMonth);
    requestedMonth = requestedMonth.getLastDateOfNextMonth();
    startAmount += this.account
        .budgets
        .filter(budget => (budget.endDate !== null && requestedMonth.isBeforeOrSame(budget.endDate)) || budget.endDate === null)
        .map(budget => budget.initialAmount)
        .reduce((a, b) => a + b, 0);
    return startAmount;

  }
}