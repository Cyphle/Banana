package com.banana.utils;

import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
  public void should_get_first_day_of_month() {
    Moment myMoment = new Moment("2017-07-14");
    Moment firstDayOfMonth = myMoment.getFirstDateOfMonth();

    assertThat(firstDayOfMonth.getDayOfMonth()).isEqualTo(1);
    assertThat(firstDayOfMonth.getMonthNumber()).isEqualTo(myMoment.getMonthNumber());
    assertThat(firstDayOfMonth.getYear()).isEqualTo(myMoment.getYear());
  }
}
