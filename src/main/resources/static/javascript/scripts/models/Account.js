'use strict';

export class Account {
  constructor(id, user, name, slug, initialAmount, startDate) {
    this.id = id;
    this.user = user;
    this.name = name;
    this.slug = slug;
    this.initialAmount = initialAmount;
    this.startDate = startDate;
    this.budgets = [];
    this.charges = [];
    this.expenses = [];
    this.credits = [];
  }

  addBudget(budget) {
    this.budgets.push(budget);
  }

  addCharge(charge) {
    this.charges.push(charge);
  }

  addExpense(expense) {
    this.expenses.push(expense);
  }

  addCredit(credit) {
    this.credits.push(credit);
  }

  clone() {
    let account = new Account(this.id, this.user.clone(), this.name, this.slug, this.initialAmount, this.startDate);
    this.budgets.forEach(budget => account.addBudget(budget.clone()));
    this.charges.forEach(charge => account.addCharge(charge.clone()));
    this.expenses.forEach(expense => account.addExpense(expense.clone()));
    this.credits.forEach(credit => account.addCredit(credit.clone()));
    return account;
  }
}