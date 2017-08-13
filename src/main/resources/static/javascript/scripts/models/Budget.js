'use strict';

export class Budget {
  constructor(id, name, initialAmount, startDate, endDate) {
    this.id = id;
    this.name = name;
    this.initialAmount = initialAmount;
    this.startDate = startDate;
    this.endDate = endDate !== null ? endDate : null;
    this.expenses = [];
  }

  addExpense(expense) {
    this.expenses.push(expense);
  }

  clone() {
    let budget = new Budget(this.id, this.name, this.initialAmount, this.startDate, this.endDate);
    this.expenses.forEach(expense => budget.expenses.push(expense.clone()));
    return budget;
  }
}