'use strict';

export class Charge {
  constructor(id, description, amount, startDate, endDate) {
    this.id = id;
    this.description = description;
    this.amount = amount;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  clone() {
    return new Charge(this.id, this.description, this.amount, this.startDate, this.endDate);
  }
}