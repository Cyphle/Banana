package com.banana.utils;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class MomentTests {
  @Test
  public void should_get_today_date() {
    LocalDateTime localDate = LocalDateTime.now();

    Moment moment = new Moment();
    Date today = moment.getDate();
    Calendar cal = Calendar.getInstance();
    cal.setTime(today);

    assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(localDate.getDayOfMonth());
    assertThat((cal.get(Calendar.MONTH) + 1)).isEqualTo(localDate.getMonth().getValue());
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(localDate.getYear());
    assertThat(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(localDate.getHour());
  }

  @Test
  public void should_get_day_of_month_for_today() {
    LocalDate localDate = LocalDate.now();

    Moment moment = new Moment();

    assertThat(moment.getDayOfMonth()).isEqualTo(localDate.getDayOfMonth());
  }

  @Test
  public void should_get_month_number() {
    LocalDate localDate = LocalDate.now();

    Moment moment = new Moment();

    assertThat(moment.getMonthNumber()).isEqualTo(localDate.getMonth().getValue());
  }

  @Test
  public void should_get_month_string_value() {
    String[] monthName = { "janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre" };
    Calendar cal = Calendar.getInstance();
    String month = monthName[cal.get(Calendar.MONTH)];

    Moment moment = new Moment();

    assertThat(moment.getMonthName()).isEqualTo(month);
  }

  @Test
  public void should_get_year() {
    LocalDate localDate = LocalDate.now();

    Moment moment = new Moment();

    assertThat(moment.getYear()).isEqualTo(localDate.getYear());
  }

  @Test
  public void should_get_current_hour() {
    LocalDateTime localTime = LocalDateTime.now();

    Moment moment = new Moment();

    assertThat(moment.getHour()).isEqualTo(localTime.getHour());
  }

/*
  @Test
  public void shouldConstructWithMonthYear() throws ParseException {
    BananaDate bTime = new BananaDate("03/2017");

    Calendar cal = Calendar.getInstance();
    cal.setTime(bTime.toDate());

    assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(1);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldConstructWithMonthYearDashSeparator() throws ParseException {
    BananaDate bDate = new BananaDate("03-2017");

    Calendar cal = Calendar.getInstance();
    cal.setTime(bDate.toDate());

    assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(1);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldReturnFirstDayOfGivenMonth() throws ParseException {
    BananaDate bTime = new BananaDate("10/03/2017");
    Date firstDay = bTime.getFirstDayOfMonth();

    Calendar cal = Calendar.getInstance();
    cal.setTime(firstDay);

    assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(1);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldReturnLastDayOfGivenMonth() throws ParseException {
    BananaDate bTime = new BananaDate("10/03/2017");
    Date lastDay = bTime.getLastDayOfMonth();

    Calendar cal = Calendar.getInstance();
    cal.setTime(lastDay);

    assertThat(cal.get(Calendar.DAY_OF_MONTH)).isEqualTo(31);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldGeFirstDayOfPreviousMonth() throws ParseException {
    BananaDate bTime = new BananaDate("10/03/2017");
    Date lastMonth = bTime.getFirstDayOfPreviousMonth();

    Calendar cal = Calendar.getInstance();
    cal.setTime(lastMonth);

    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.FEBRUARY);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldGetLastMonthWhenJanuary() throws ParseException {
    BananaDate bTime = new BananaDate("10/01/2017");
    Date lastMonth = bTime.getFirstDayOfPreviousMonth();

    Calendar cal = Calendar.getInstance();
    cal.setTime(lastMonth);

    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.DECEMBER);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2016);
  }

  @Test
  public void shouldGetLastDayOfPreviousMonth() throws ParseException {
    BananaDate bTime = new BananaDate("10/04/2017");
    Date lastDayPreviousMonth = bTime.getLastDayOfPreviousMonth();

    Calendar cal = Calendar.getInstance();
    cal.setTime(lastDayPreviousMonth);

    assertThat(cal.get(Calendar.DATE)).isEqualTo(31);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldGetLastDayOfPreviousMonthWhenJanuary() throws ParseException {
    BananaDate bTime = new BananaDate("10/01/2017");
    Date lastDayPreviousMonth = bTime.getLastDayOfPreviousMonth();

    Calendar cal = Calendar.getInstance();
    cal.setTime(lastDayPreviousMonth);

    assertThat(cal.get(Calendar.DATE)).isEqualTo(31);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.DECEMBER);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2016);
  }

  @Test
  public void shouldGetNextMonth() throws ParseException {
    BananaDate bTime = new BananaDate("04/03/2017");
    Date lastMonth = bTime.getFirstDayOfNextMonth();

    Calendar cal = Calendar.getInstance();
    cal.setTime(lastMonth);

    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.APRIL);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldGetNextMonthWhenDecember() throws ParseException {
    BananaDate bTime = new BananaDate("10/12/2017");
    Date lastMonth = bTime.getFirstDayOfNextMonth();

    Calendar cal = Calendar.getInstance();
    cal.setTime(lastMonth);

    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.JANUARY);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2018);
  }

  @Test
  public void shouldCombineDatesToGetDayOfArgumentWithRestOfCurrent() throws ParseException {
    BananaDate bDate = new BananaDate("10/03/2017");
    Date otherDate = (new BananaDate("03/04/2017")).toDate();

    Date newDate = bDate.getMonthYearWithDayOf(otherDate);
    Calendar cal = Calendar.getInstance();
    cal.setTime(newDate);

    assertThat(cal.get(Calendar.DATE)).isEqualTo(03);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldCombineDatesToLastDayOfCurrentMontIfCurrentMonthIsFebruary() throws ParseException {
    BananaDate bDate = new BananaDate("10/02/2017");
    Date otherDate = (new BananaDate("30/04/2017")).toDate();

    Date newDate = bDate.getMonthYearWithDayOf(otherDate);
    Calendar cal = Calendar.getInstance();
    cal.setTime(newDate);

    assertThat(cal.get(Calendar.DATE)).isEqualTo(28);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.FEBRUARY);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }

  @Test
  public void shouldCombineDatesIfCurrentMonthHas30Days() throws ParseException {
    BananaDate bDate = new BananaDate("10/04/2017");
    Date otherDate = (new BananaDate("31/05/2017")).toDate();

    Date newDate = bDate.getMonthYearWithDayOf(otherDate);
    Calendar cal = Calendar.getInstance();
    cal.setTime(newDate);

    assertThat(cal.get(Calendar.DATE)).isEqualTo(30);
    assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.APRIL);
    assertThat(cal.get(Calendar.YEAR)).isEqualTo(2017);
  }*/
}
