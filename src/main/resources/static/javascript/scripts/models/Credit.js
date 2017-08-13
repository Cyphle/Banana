'use strict';

export class Credit {
  constructor(id, description, amount, creditDate) {
    this.id = id;
    this.description = description;
    this.amount = amount;
    this.creditDate = creditDate;
  }

  clone() {
    return new Credit(this.id, this.description, this.amount, this.creditDate);
  }
}