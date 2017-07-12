package com.banana.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
