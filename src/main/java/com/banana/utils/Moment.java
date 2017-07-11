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



  /*private Date bDate;
  private SimpleDateFormat formatter;
  private Calendar currentCalendar;

  public Moment() {
    this.bDate = new Date();
    this.formatter = new SimpleDateFormat("dd/MM/yyyy");
    this.currentCalendar = this.dateToCalendar(this.bDate);
  }

  public Moment(String date) {
    if (date != null) {
      SimpleDateFormat constructorFormatter = this.getConstructorFormatter(date);
      try {
        this.bDate = constructorFormatter.parse(date);
      } catch (ParseException e) {
        this.bDate = new Date();
      }
    } else {
      this.bDate = new Date();
    }
    this.formatter = new SimpleDateFormat("dd/MM/yyyy");
    this.currentCalendar = this.dateToCalendar(this.bDate);
  }

  public Moment(Date date) {
    Calendar currentCalendar = this.dateToCalendar(date);
    this.formatter = new SimpleDateFormat("dd/MM/yyyy");
    try {
      this.bDate = this.formatter.parse(currentCalendar.get(Calendar.DATE) + "/" + (currentCalendar.get(Calendar.MONTH) + 1) + "/" + currentCalendar.get(Calendar.YEAR));
    } catch (ParseException e) {
      this.bDate = new Date();
    }
    this.currentCalendar = this.dateToCalendar(this.bDate);
  }*/
/*
  public Date toDate() {
    return this.bDate;
  }

  public void setDate(Date bDate) {
    this.bDate = bDate;
  }

  public Date getFirstDayOfMonth() throws ParseException {
    return this.formatter.parse(currentCalendar.getActualMinimum(currentCalendar.DAY_OF_MONTH) + "/" + (currentCalendar.get(Calendar.MONTH) + 1) + "/" + currentCalendar.get(Calendar.YEAR));
  }

  public Date getLastDayOfMonth() throws ParseException {
    return this.formatter.parse(currentCalendar.getActualMaximum(currentCalendar.DAY_OF_MONTH) + "/" + (currentCalendar.get(Calendar.MONTH) + 1) + "/" + currentCalendar.get(Calendar.YEAR));
  }

  public Date getFirstDayOfPreviousMonth() throws ParseException {
    return this.formatter.parse("01/" + currentCalendar.get(Calendar.MONTH) + "/" + currentCalendar.get(Calendar.YEAR));
  }

  public Date getLastDayOfPreviousMonth() throws ParseException {
    SimpleDateFormat tempFormatter = new SimpleDateFormat("MM/yyyy");
    Date previousMonth = tempFormatter.parse(this.currentCalendar.get(Calendar.MONTH) + "/" + this.currentCalendar.get(Calendar.YEAR));
    Calendar tempCalendar = this.dateToCalendar(previousMonth);
    return this.formatter.parse(tempCalendar.getActualMaximum(tempCalendar.DAY_OF_MONTH) + "/" + (tempCalendar.get(Calendar.MONTH) + 1) + "/" + tempCalendar.get(Calendar.YEAR));
  }

  public Date getFirstDayOfNextMonth() throws ParseException {
    return this.formatter.parse("01/" + (currentCalendar.get(Calendar.MONTH) + 2) + "/" + currentCalendar.get(Calendar.YEAR));
  }

  public Date getMonthYearWithDayOf(Date dateToGetDay) throws ParseException {
    Calendar dateToGetDayCalendar = this.dateToCalendar(dateToGetDay);
    int newDay = dateToGetDayCalendar.get(Calendar.DATE);
    if (this.currentCalendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
      int currentYear = this.currentCalendar.get(Calendar.YEAR);
      newDay = 28 + (currentYear%4 == 0 ? 1 : (currentYear%4 == 0 && currentYear%100 != 0 ? 1 : 0));
    } else if (this.currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 30)
      newDay = 30;

    return this.formatter.parse(newDay + "/" + (this.currentCalendar.get(Calendar.MONTH) + 1) + "/" + this.currentCalendar.get(Calendar.YEAR));
  }

  private Calendar dateToCalendar(Date dateToTransform) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(dateToTransform);
    return cal;
  }

  private SimpleDateFormat getConstructorFormatter(String date) {
    String[] dateParts = date.split("/");
    if (dateParts.length == 2) {
      return new SimpleDateFormat("MM/yyyy");
    } else if (dateParts.length == 3) {
      return new SimpleDateFormat("dd/MM/yyyy");
    } else {
      dateParts = date.split("-");
      if (dateParts.length == 2) {
        return new SimpleDateFormat("MM-yyyy");
      } else if (dateParts.length == 3) {
        return new SimpleDateFormat("dd-MM-yyyy");
      }
    }
    return null;
  }
  */
}
