package com.banana.utils;

import java.util.Date;

public interface IMoment {
  Date getDate();
  int getDayOfMonth();
  int getMonthNumber();
  String getMonthName();
  int getYear();
  int getHour();
  Moment getFirstDateOfMonth();
  int getLastDayOfMonth();
  boolean isInMonthOfYear(int month, int year);
}
