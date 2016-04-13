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

import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class CalendarManager {
  public static final int DEFAULT_DAYS_IN_WEEK = 7;
  public static final int DEFAULT_WEEKS_IN_MONTH = 6;
  private CalendarUtils mCalendarUtils;

  private static final HashMap<Integer, HashMap<Integer, DateInfo[][]>> mDateCache =
      new HashMap<>();

  public static CalendarManager getInstance() {
    return SingletonHolder.mInstance;
  }

  private CalendarManager() {
    String locale = Locale.getDefault().getCountry().toLowerCase();
    if (locale.equals("cn")) {
      initCalendar(new DPCNCalendar()); // TODO
    } else {
      initCalendar(new USCalendarUtils());
    }
  }

  public int getDefaultYear() {
    return mCalendarUtils.getDefaultYear();
  }

  public int getDefaultMonth() {
    return mCalendarUtils.getDefaultMonth();
  }

  /**
   * Initialization Calendar
   * 初始化日历对象
   */
  public void initCalendar(CalendarUtils calendarUtils) {
    mCalendarUtils = calendarUtils;
  }

  /**
   * 获取年月对应的日历信息
   *
   * @param year 公历年
   * @param month 公历月
   * @return 指定的月份信息数组，数组长度为 6x7，无对应数据则为 null
   */
  public DateInfo[][] obtainDateInfo(int year, int month) {
    Log.i("weekend", "obtainDateInfo: " + "year == " + year + " month == " + month);
    HashMap<Integer, DateInfo[][]> yearCache = mDateCache.get(year);
    DateInfo[][] monthCache;

    if (yearCache != null && yearCache.size() != 0) {
      monthCache = yearCache.get(month);

      if (monthCache == null) {
        monthCache = buildDPInfo(year, month);
        yearCache.put(month, monthCache);
      }
    } else {
      if (yearCache == null) {
        yearCache = new HashMap<>();
      }

      monthCache = buildDPInfo(year, month);
      yearCache.put(month, monthCache);
      mDateCache.put(year, yearCache);
    }

    return monthCache;
  }

  private DateInfo[][] buildDPInfo(int year, int month) {
    DateInfo[][] dateInfos = new DateInfo[DEFAULT_WEEKS_IN_MONTH][DEFAULT_DAYS_IN_WEEK];
    String[][] monthDays = mCalendarUtils.getMonthDays(year, month);
    String[][] monthFestivals = mCalendarUtils.getMonthFestivals(year, month);

    //Set<String> strHoliday = mCalendarUtils.getMonthHoliday(year, month);
    Set<String> strWeekend = mCalendarUtils.getMonthWeekend(year, month);
    Log.i("weekend", "year == " + year + " month == " + month);
    for (String week : strWeekend) {
      Log.i("weekend", "week: " + week);
    }

    for (int i = 0; i < dateInfos.length; i++) {
      for (int j = 0; j < dateInfos[i].length; j++) {
        DateInfo tmp = new DateInfo();
        tmp.day = monthDays[i][j];
        if (mCalendarUtils instanceof DPCNCalendar) {
          tmp.festival = monthFestivals[i][j].replace("F", "");
        } else {
          tmp.festival = monthFestivals[i][j];
        }
        //if (!TextUtils.isEmpty(tmp.day) && strHoliday.contains(tmp.day)) {
        //  tmp.isHoliday = true;
        //}
        if (!TextUtils.isEmpty(tmp.day)) {
          tmp.isToday = mCalendarUtils.isToday(year, month, Integer.valueOf(tmp.day));
        }
        if (strWeekend.contains(tmp.day)) tmp.isWeekend = true;
        if (mCalendarUtils instanceof DPCNCalendar) {
          if (!TextUtils.isEmpty(tmp.day)) {
            tmp.isSolarTerms = ((DPCNCalendar) mCalendarUtils).isSolarTerm(year, month,
                Integer.valueOf(tmp.day));
          }
          if (!TextUtils.isEmpty(monthFestivals[i][j]) && monthFestivals[i][j].endsWith("F")) {
            tmp.isFestival = true;
          }
          //if (!TextUtils.isEmpty(tmp.day)) {
          //  tmp.isDayOff = ((DPCNCalendar) mCalendarUtils).isDeferred(year, month,
          //      Integer.valueOf(tmp.day));
          //}
        } else {
          tmp.isFestival = !TextUtils.isEmpty(monthFestivals[i][j]);
        }
        dateInfos[i][j] = tmp;
      }
    }
    return dateInfos;
  }

  private static class SingletonHolder {
    static final CalendarManager mInstance = new CalendarManager();
  }
}
