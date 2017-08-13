'use strict';

import { Months } from './Months';

export class Instant {
  constructor(dateParam) {
    if (dateParam !== undefined && dateParam !== null) {
      if (dateParam instanceof Date)
        this.date = dateParam;
      else
        this.date = new Date(dateParam);
    } else {
      this.date = new Date();
    }
  }

  getYear() {
    return this.date.getFullYear();
  }

  getMonthNumber() {
    return Months[this.date.getMonth()].value;
  }

  getMonthName() {
    return Months[this.date.getMonth()].name;
  }

  getDayNumber() {
    return this.date.getDate();
  }

  isAfterOrSame(instantCompare) {
    return this.isAfter(instantCompare) ? true : this.isSame(instantCompare);
  }

  isAfter(instantCompare) {
    if (instantCompare.getYear() < this.getYear()) {
      return true;
    } else if (instantCompare.getYear() === this.getYear()) {
      if (instantCompare.getMonthNumber() < this.getMonthNumber())
        return true;
      else if (instantCompare.getMonthNumber() === this.getMonthNumber()) {
        if (instantCompare.getDayNumber() < this.getDayNumber())
          return true;
        else
          return false;
      }
    }
    return false;
  }

  isBeforeOrSame(instantCompare) {
    return this.isBefore(instantCompare) ? true : this.isSame(instantCompare);
  }

  isBefore(instantCompare) {
    if (instantCompare.getYear() > this.getYear()) {
      return true;
    } else if (instantCompare.getYear() === this.getYear()) {
      if (instantCompare.getMonthNumber() > this.getMonthNumber())
        return true;
      else if (instantCompare.getMonthNumber() === this.getMonthNumber()) {
        if (instantCompare.getDayNumber() > this.getDayNumber())
          return true;
        else
          return false;
      }
    }
    return false;
  }

  isSame(instantCompare) {
    return instantCompare.getYear() === this.getYear() && instantCompare.getMonthNumber() === this.getMonthNumber() && instantCompare.getDayNumber() === this.getDayNumber();
  }

  hasSameMonthAs(instantCompare) {
    return instantCompare.getYear() === this.getYear() && instantCompare.getMonthNumber() === this.getMonthNumber();
  }

  getFirstDateOfMonth() {
    return new Instant(new Date(this.getYear(), this.getMonthNumber() - 1, 1));
  }

  getLastDateOfMonth() {
    return new Instant(new Date(this.getYear(), this.getMonthNumber(), 0));
  }

  getLastDateOfPreviousMonth() {
    return new Instant(new Date(this.getYear(), this.getMonthNumber() - 1, 0));
  }

  getLastDateOfNextMonth() {
    return new Instant(new Date(this.getYear(), this.getMonthNumber() + 1, 0));
  }

  getDifferenceOfMonthBetweenAndExcluding(instantCompare) {
    let numberMonthBetweenYears = (instantCompare.getYear() - this.getYear())*12;
    let numberOfMonthBetweenMonths = instantCompare.getMonthNumber() - this.getMonthNumber();
    return Math.abs(numberMonthBetweenYears + numberOfMonthBetweenMonths);
  }

  toString() {
    return this.getDayNumber() + ' ' + this.getMonthName() + ' ' + this.getYear();
  }

  toShortString() {
    return (this.getDayNumber() < 10 ? '0' + this.getDayNumber() : this.getDayNumber()) + '/' + (this.getMonthNumber() < 10 ? '0' + this.getMonthNumber() : this.getMonthNumber()) + '/' + this.getYear();
  }
}
