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
  Moment getLastDateOfMonth();
  int getLastDayOfMonth();
  boolean isInMonthOfYear(int month, int year);
  Moment getLastDayOfPrecedingMonth();
  Moment getLastDayOfNextMonth();
  int compareTo(Moment momentCompare);
  int getNumberOfMonthsBetweenExcludingCurrent(Moment momentToCalculate);
  String toString();
}
