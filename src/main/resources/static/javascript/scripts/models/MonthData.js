'use strict';

export class MonthData {
  constructor(accountId, accountName, requestedMonth) {
    this.accountId = accountId;
    this.accountName = accountName;
    this.requestedMonth = requestedMonth;
    this.startAmount = 0;
    this.currentAmount = 0;
    this.freeAmount = 0;
    this.budgets = [];
    this.charges = [];
    this.expenses = [];
    this.credits = [];
  }
}