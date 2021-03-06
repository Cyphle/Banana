package com.banana.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class MomentTests {
  @Test
  public void should_build_moment_from_date_object() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    formatter = formatter.withLocale(Locale.FRANCE);
    LocalDate localDate = LocalDate.parse("2017-11-12", formatter);
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    Moment myMoment = new Moment(date);

    assertThat(myMoment.getDayOfMonth()).isEqualTo(localDate.getDayOfMonth());
    assertThat(myMoment.getMonthNumber()).isEqualTo(localDate.getMonth().getValue());
    assertThat(myMoment.getYear()).isEqualTo(localDate.getYear());
  }

  @Test
  public void should_build_moment_from_string() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    formatter = formatter.withLocale(Locale.FRANCE);
    LocalDate localDate = LocalDate.parse("2017-07-14", formatter);

    Moment myMoment = new Moment("2017-07-14");

    assertThat(myMoment.getDayOfMonth()).isEqualTo(localDate.getDayOfMonth());
    assertThat(myMoment.getMonthNumber()).isEqualTo(localDate.getMonth().getValue());
    assertThat(myMoment.getMonthName()).isEqualTo("juillet");
    assertThat(myMoment.getYear()).isEqualTo(localDate.getYear());
  }

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

  @Test
  public void should_get_first_date_of_month() {
    Moment myMoment = new Moment("2017-07-14");
    Moment firstDayOfMonth = myMoment.getFirstDateOfMonth();

    assertThat(firstDayOfMonth.getDayOfMonth()).isEqualTo(1);
    assertThat(firstDayOfMonth.getMonthNumber()).isEqualTo(myMoment.getMonthNumber());
    assertThat(firstDayOfMonth.getYear()).isEqualTo(myMoment.getYear());
  }

  @Test
  public void should_get_last_date_of_month() {
    Moment myMoment = new Moment("2017-07-14");
    Moment lastDateOfMonth = myMoment.getLastDateOfMonth();
    Moment mySecondMoment = new Moment("2016-02-10");
    Moment secondLastDateOfMonth = mySecondMoment.getLastDateOfMonth();
    Moment thirdMoment = new Moment("2017-12-20").getLastDateOfMonth();

    assertThat(lastDateOfMonth.getDayOfMonth()).isEqualTo(31);
    assertThat(lastDateOfMonth.getMonthNumber()).isEqualTo(myMoment.getMonthNumber());
    assertThat(lastDateOfMonth.getYear()).isEqualTo(myMoment.getYear());
    assertThat(secondLastDateOfMonth.getDayOfMonth()).isEqualTo(29);
    assertThat(secondLastDateOfMonth.getMonthNumber()).isEqualTo(mySecondMoment.getMonthNumber());
    assertThat(secondLastDateOfMonth.getYear()).isEqualTo(mySecondMoment.getYear());
    assertThat(thirdMoment.getDayOfMonth()).isEqualTo(31);
    assertThat(thirdMoment.getMonthNumber()).isEqualTo(thirdMoment.getMonthNumber());
    assertThat(thirdMoment.getYear()).isEqualTo(thirdMoment.getYear());
  }

  @Test
  public void should_get_last_day_of_month() {
    Moment june = new Moment("2017-06-08");
    Moment july = new Moment("2017-07-10");
    Moment january = new Moment("2017-01-03");
    Moment december = new Moment("2017-12-07");
    Moment february = new Moment("2017-02-04");
    Moment februaryBissextil = new Moment("2016-02-05");

    assertThat(june.getLastDayOfMonth()).isEqualTo(30);
    assertThat(july.getLastDayOfMonth()).isEqualTo(31);
    assertThat(january.getLastDayOfMonth()).isEqualTo(31);
    assertThat(december.getLastDayOfMonth()).isEqualTo(31);
    assertThat(february.getLastDayOfMonth()).isEqualTo(28);
    assertThat(februaryBissextil.getLastDayOfMonth()).isEqualTo(29);
  }

  @Test
  public void should_return_true_if_date_is_in_month() {
    Moment date = new Moment("2017-07-16");
    Moment dateTwo = new Moment("2017-07-01");
    Moment dateThree = new Moment("2017-07-31");

    assertThat(date.isInMonthOfYear(Month.JULY.getValue(), 2017)).isTrue();
    assertThat(dateTwo.isInMonthOfYear(Month.JULY.getValue(), 2017)).isTrue();
    assertThat(dateThree.isInMonthOfYear(Month.JULY.getValue(), 2017)).isTrue();
  }

  @Test
  public void should_return_false_if_date_is_not_in_month() {
    Moment date = new Moment("2017-07-16");
    Moment dateTwo = new Moment("2017-07-01");
    Moment dateThree = new Moment("2017-07-31");

    assertThat(date.isInMonthOfYear(Month.AUGUST.getValue(), 2017)).isFalse();
    assertThat(dateTwo.isInMonthOfYear(Month.JUNE.getValue(), 2017)).isFalse();
    assertThat(dateThree.isInMonthOfYear(Month.AUGUST.getValue(), 2017)).isFalse();
  }

  @Test
  public void should_get_end_of_preceding_month() {
    Moment dateOne = new Moment("2017-08-10").getLastDayOfPreviousMonth();
    Moment dateTwo = new Moment("2017-01-12").getLastDayOfPreviousMonth();
    Moment dateThree = new Moment("2016-03-12").getLastDayOfPreviousMonth();
    Moment dateFour = new Moment("2017-07-31").getLastDayOfPreviousMonth();

    assertThat(dateOne.getDayOfMonth()).isEqualTo(31);
    assertThat(dateOne.getMonthNumber()).isEqualTo(7);
    assertThat(dateOne.getYear()).isEqualTo(2017);
    assertThat(dateTwo.getDayOfMonth()).isEqualTo(31);
    assertThat(dateTwo.getMonthNumber()).isEqualTo(12);
    assertThat(dateTwo.getYear()).isEqualTo(2016);
    assertThat(dateThree.getDayOfMonth()).isEqualTo(29);
    assertThat(dateThree.getMonthNumber()).isEqualTo(2);
    assertThat(dateThree.getYear()).isEqualTo(2016);
    assertThat(dateFour.getDayOfMonth()).isEqualTo(30);
    assertThat(dateFour.getMonthNumber()).isEqualTo(6);
    assertThat(dateFour.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_get_end_of_next_month() {
    Moment dateOne = new Moment("2017-08-10").getLastDayOfNextMonth();
    Moment dateTwo = new Moment("2017-12-20").getLastDayOfNextMonth();
    Moment dateThree = new Moment("2016-01-12").getLastDayOfNextMonth();
    Moment dateFour = new Moment("2017-07-31").getLastDayOfNextMonth();

    assertThat(dateOne.getDayOfMonth()).isEqualTo(30);
    assertThat(dateOne.getMonthNumber()).isEqualTo(9);
    assertThat(dateOne.getYear()).isEqualTo(2017);
    assertThat(dateTwo.getDayOfMonth()).isEqualTo(31);
    assertThat(dateTwo.getMonthNumber()).isEqualTo(1);
    assertThat(dateTwo.getYear()).isEqualTo(2018);
    assertThat(dateThree.getDayOfMonth()).isEqualTo(29);
    assertThat(dateThree.getMonthNumber()).isEqualTo(2);
    assertThat(dateThree.getYear()).isEqualTo(2016);
    assertThat(dateFour.getDayOfMonth()).isEqualTo(31);
    assertThat(dateFour.getMonthNumber()).isEqualTo(8);
    assertThat(dateFour.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_return_minus_one_if_date_is_before_compare() {
    Moment date = new Moment("2017-01-01");
    Moment compareTo = new Moment("2017-02-12");

    assertThat(date.compareTo(compareTo)).isEqualTo(-1);
  }

  @Test
  public void should_return_zero_if_date_are_equal() {
    Moment date = new Moment("2017-01-01");
    Moment compareTo = new Moment("2017-01-01");

    assertThat(date.compareTo(compareTo)).isEqualTo(0);
  }

  @Test
  public void should_return_one_if_date_is_after_compare() {
    Moment date = new Moment("2017-03-01");
    Moment compareTo = new Moment("2017-02-12");

    assertThat(date.compareTo(compareTo)).isEqualTo(1);
  }

  @Test
  public void should_calculate_number_of_month_between_two_dates_minus_current_month() {
    Moment date = new Moment("2017-03-02");
    Moment dateAfter = new Moment("2017-08-10");
    Moment dateAfterTwo = new Moment("2018-01-04");
    Moment dateWayAfter = new Moment("2018-12-23");
    Moment dateBefore = new Moment("2016-05-18");
    Moment dateWayBefore = new Moment("2015-02-03");
    Moment dateEqual = new Moment("2017-03-19");

    assertThat(date.getNumberOfMonthsBetweenExcludingCurrent(dateAfter)).isEqualTo(5);
    assertThat(date.getNumberOfMonthsBetweenExcludingCurrent(dateAfterTwo)).isEqualTo(10);
    assertThat(date.getNumberOfMonthsBetweenExcludingCurrent(dateWayAfter)).isEqualTo(21);
    assertThat(date.getNumberOfMonthsBetweenExcludingCurrent(dateBefore)).isEqualTo(10);
    assertThat(date.getNumberOfMonthsBetweenExcludingCurrent(dateWayBefore)).isEqualTo(25);
    assertThat(date.getNumberOfMonthsBetweenExcludingCurrent(dateEqual)).isEqualTo(0);
  }
}
