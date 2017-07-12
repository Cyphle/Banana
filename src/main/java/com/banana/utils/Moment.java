package com.banana.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
}
