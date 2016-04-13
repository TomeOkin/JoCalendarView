/*
 * Copyright 2016 TomeOkin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomeokin.widget.jocalendarview.biz;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public abstract class CalendarUtils {
  private final Calendar mCalendar = Calendar.getInstance();

  /**
   * Build the festival date array of given year and month
   * 获取某年某月的节日数组
   *
   * @param year 某年
   * @param month 某月
   * @return 该月节日数组
   */
  public abstract String[][] getMonthFestivals(int year, int month);

  /**
   * Build the holiday date array of given year and month
   * 获取某年某月的假期数组
   *
   * @param year 某年
   * @param month 某月
   * @return 该月假期数组
   */
  //public abstract Set<String> getMonthHoliday(int year, int month);

  /**
   * 判断某年是否为闰年
   *
   * @param year 年份
   * @return true 表示闰年
   */
  public boolean isLeapYear(int year) {
    return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
  }

  /**
   * 判断给定日期是否为今天
   *
   * @param year 某年
   * @param month 某月
   * @param day 某天
   * @return ...
   */
  public boolean isToday(int year, int month, int day) {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.set(year, month - 1, day);
    return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) &&
        (c1.get(Calendar.MONTH) == (c2.get(Calendar.MONTH))) &&
        (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
  }

  private int getDaysInMonth(int year, int month) {
    int daysInMonth = 0;
    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 ||
        month == 12) {
      daysInMonth = 31;
    } else if (month == 4 || month == 6 || month == 9 || month == 11) {
      daysInMonth = 30;
    } else if (month == 2) {
      if (isLeapYear(year)) {
        daysInMonth = 29;
      } else {
        daysInMonth = 28;
      }
    }

    return daysInMonth;
  }

  /**
   * 生成某年某月的公历天数数组
   * 数组为6x7的二维数组因为一个月的周数永远不会超过六周
   * 天数填充对应相应的二维数组下标
   * 如果某个数组下标中没有对应天数那么则填充一个空字符串
   *
   * @param year 某年
   * @param month 某月
   * @return 某年某月的公历天数数组
   */
  public String[][] getMonthDays(int year, int month) {
    mCalendar.clear();
    String monthDay[][] = new String[6][7];
    mCalendar.set(year, month - 1, 1);

    int daysInMonth = getDaysInMonth(year, month);
    int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
    int day = 1;
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 7; j++) {
        monthDay[i][j] = "";
        if (i == 0 && j >= dayOfWeek) {
          monthDay[i][j] = "" + day;
          day++;
        } else if (i > 0 && day <= daysInMonth) {
          monthDay[i][j] = "" + day;
          day++;
        }
      }
    }

    return monthDay;
  }

  /**
   * 生成某年某月的周末日期集合
   *
   * @param year 某年
   * @param month 某月
   * @return 某年某月的周末日期集合
   */
  public Set<String> getMonthWeekend(int year, int month) {
    Set<String> weekends = new HashSet<>();
    mCalendar.clear();
    mCalendar.set(year, month - 1, 1);
    do {
      int day = mCalendar.get(Calendar.DAY_OF_WEEK);
      if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
        weekends.add(String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH)));
      }
      mCalendar.add(Calendar.DAY_OF_YEAR, 1);
    } while (mCalendar.get(Calendar.MONTH) == month - 1);
    return weekends;
  }

  public int getDefaultYear() {
    return mCalendar.get(Calendar.YEAR);
  }

  public int getDefaultMonth() {
    return mCalendar.get(Calendar.MONTH) + 1;
  }
}
