package com.banana.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

public class Moment implements IMoment {
  private ZoneId zone;
  private Locale locale;
  private ZonedDateTime zonedTime;
  private Instant date;

  public Moment() {
    this.zone = ZoneId.systemDefault();
    this.locale = Locale.FRANCE;
    this.date = Instant.now();
    this.zonedTime = this.date.atZone(this.zone);
  }

  public Moment(Date date) {
    this();
    this.zonedTime = ZonedDateTime.ofInstant(date.toInstant(), this.zone);
    this.date = this.zonedTime.toInstant();
  }

  public Moment(String date) {
    this();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    formatter = formatter.withLocale(this.locale);
    LocalDate localDate = LocalDate.parse(date, formatter);
    this.zonedTime = localDate.atStartOfDay(this.zone);
    this.date = this.zonedTime.toInstant();
  }

  public Date getDate() {
    return Date.from(this.date);
  }

  public int getDayOfMonth() {
    return zonedTime.getDayOfMonth();
  }

  public int getMonthNumber() {
    return zonedTime.getMonth().getValue();
  }

  public String getMonthName() {
    return zonedTime.getMonth().getDisplayName(TextStyle.FULL, this.locale);
  }

  public int getYear() {
    return zonedTime.getYear();
  }

  public int getHour() {
    return zonedTime.getHour();
  }

  public Moment getFirstDateOfMonth() {
    String monthNumber = this.getMonthNumber() < 10 ? ("0" + this.getMonthNumber()) : "" + this.getMonthNumber();
    return new Moment(this.getYear() + "-" + monthNumber + "-01");
  }

  public Moment getLastDateOfMonth() {
    LocalDate initial = this.zonedTime.toLocalDate();
    LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
    return new Moment(end.toString());
  }

  public int getLastDayOfMonth() {
    LocalDate initial = this.zonedTime.toLocalDate();
    LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
    return end.getDayOfMonth();
  }

  public boolean isInMonthOfYear(int month, int year) {
    if (this.getYear() != year)
      return false;
    else if (this.getMonthNumber() != month)
      return false;
    return true;
  }

  public Moment getLastDayOfPreviousMonth() {
    int year = this.getYear();
    int month = this.getMonthNumber();
    if (this.getMonthNumber() == 1) {
      month = 12;
      --year;
    } else
      --month;
    LocalDate initial = LocalDate.of(year, month, 1);
    LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
    return new Moment(Date.from(end.atStartOfDay(this.zone).toInstant()));
  }

  public Moment getLastDayOfNextMonth() {
    int year = this.getYear();
    int month = this.getMonthNumber();
    if (this.getMonthNumber() == 12) {
      month = 1;
      ++year;
    } else
      ++month;
    LocalDate initial = LocalDate.of(year, month, 1);
    LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
    return new Moment(Date.from(end.atStartOfDay(this.zone).toInstant()));
  }

  /**
   *
   * @param momentCompare
   * @return -1 is this is before momentCompare, 0 if equal, 1 if this is after momentCompare
   */
  public int compareTo(Moment momentCompare) {
    if (this.getYear() < momentCompare.getYear())
      return -1;
    else if (this.getYear() > momentCompare.getYear())
      return 1;
    else {
      if (this.getMonthNumber() < momentCompare.getMonthNumber())
        return -1;
      else if (this.getMonthNumber() > momentCompare.getMonthNumber())
        return 1;
      else {
        if (this.getDayOfMonth() < momentCompare.getDayOfMonth())
          return -1;
        else if (this.getDayOfMonth() > momentCompare.getDayOfMonth())
          return 1;
        else
          return 0;
      }
    }
  }

  public int getNumberOfMonthsBetweenExcludingCurrent(Moment momentToCalculate) {
    int numberMonthBetweenYears = (momentToCalculate.getYear() - this.getYear())*12;
    int numberOfMonthBetweenMonths = momentToCalculate.getMonthNumber() - this.getMonthNumber();
    return Math.abs(numberMonthBetweenYears + numberOfMonthBetweenMonths);
  }

  public String toString() {
    return this.getYear() + "/" + this.getMonthNumber() + "/" + this.getDayOfMonth();
  }
}
