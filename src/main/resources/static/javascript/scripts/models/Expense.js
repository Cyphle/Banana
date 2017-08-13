'use strict';

export class Expense {
  constructor(id, description, amount, expenseDate, debitDate) {
    this.id = id;
    this.description = description;
    this.amount = amount;
    this.expenseDate = expenseDate;
    this.debitDate = debitDate;
  }

  clone() {
    return new Expense(this.id, this.description, this.amount, this.expenseDate, this.debitDate);
  }
}