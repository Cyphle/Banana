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
}
