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
package com.tomeokin.utils.jocalendarutils;

import android.util.SparseIntArray;
import com.tomeokin.widget.jocalendarview.BaseCalendarView;

public class DeferredHolidaysUtils {
  public static final int DAY_NONE = BaseCalendarView.OnDeferredHolidaysDraw.NONE; // 普通
  public static final int DAY_HOLIDAY = BaseCalendarView.OnDeferredHolidaysDraw.HOLIDAY; // 休息、放假
  public static final int DAY_DEFERRED = BaseCalendarView.OnDeferredHolidaysDraw.DEFERRED; // 上班、补假

  private static final SparseIntArray deferredHolidays = new SparseIntArray();
  static {
    // 元旦
    deferredHolidays.put(20150101, DAY_HOLIDAY);
    deferredHolidays.put(20150102, DAY_HOLIDAY);
    deferredHolidays.put(20150103, DAY_HOLIDAY);
    deferredHolidays.put(20150104, DAY_DEFERRED);

    deferredHolidays.put(20160101, DAY_HOLIDAY);
    deferredHolidays.put(20160102, DAY_HOLIDAY);
    deferredHolidays.put(20160103, DAY_HOLIDAY);

    // 春节
    deferredHolidays.put(20150215, DAY_DEFERRED);
    deferredHolidays.put(20150216, DAY_DEFERRED);
    deferredHolidays.put(20150217, DAY_DEFERRED);
    deferredHolidays.put(20150218, DAY_HOLIDAY);
    deferredHolidays.put(20150219, DAY_HOLIDAY);
    deferredHolidays.put(20150220, DAY_HOLIDAY);
    deferredHolidays.put(20150221, DAY_HOLIDAY);
    deferredHolidays.put(20150222, DAY_HOLIDAY);
    deferredHolidays.put(20150223, DAY_HOLIDAY);
    deferredHolidays.put(20150224, DAY_HOLIDAY);
    deferredHolidays.put(20150228, DAY_DEFERRED);

    deferredHolidays.put(20160206, DAY_DEFERRED);
    deferredHolidays.put(20160207, DAY_HOLIDAY);
    deferredHolidays.put(20160208, DAY_HOLIDAY);
    deferredHolidays.put(20160209, DAY_HOLIDAY);
    deferredHolidays.put(20160210, DAY_HOLIDAY);
    deferredHolidays.put(20160211, DAY_HOLIDAY);
    deferredHolidays.put(20160212, DAY_HOLIDAY);
    deferredHolidays.put(20160213, DAY_HOLIDAY);
    deferredHolidays.put(20160214, DAY_DEFERRED);

    // 清明节
    deferredHolidays.put(20150404, DAY_HOLIDAY);
    deferredHolidays.put(20150405, DAY_HOLIDAY);
    deferredHolidays.put(20150406, DAY_HOLIDAY);

    deferredHolidays.put(20160402, DAY_HOLIDAY);
    deferredHolidays.put(20160403, DAY_HOLIDAY);
    deferredHolidays.put(20160404, DAY_HOLIDAY);

    // 劳动节
    deferredHolidays.put(20150501, DAY_HOLIDAY);
    deferredHolidays.put(20150502, DAY_HOLIDAY);
    deferredHolidays.put(20150503, DAY_HOLIDAY);

    deferredHolidays.put(20160430, DAY_HOLIDAY);
    deferredHolidays.put(20160501, DAY_HOLIDAY);
    deferredHolidays.put(20160502, DAY_HOLIDAY);

    // 端午节
    deferredHolidays.put(20150620, DAY_HOLIDAY);
    deferredHolidays.put(20150621, DAY_HOLIDAY);
    deferredHolidays.put(20150622, DAY_HOLIDAY);

    deferredHolidays.put(20160609, DAY_HOLIDAY);
    deferredHolidays.put(20160610, DAY_HOLIDAY);
    deferredHolidays.put(20160611, DAY_HOLIDAY);
    deferredHolidays.put(20160612, DAY_DEFERRED);

    // 抗战胜利纪念日
    deferredHolidays.put(20150903, DAY_HOLIDAY);
    deferredHolidays.put(20150904, DAY_HOLIDAY);
    deferredHolidays.put(20150905, DAY_HOLIDAY);
    deferredHolidays.put(20150906, DAY_DEFERRED);

    // 中秋节
    deferredHolidays.put(20150926, DAY_HOLIDAY);
    deferredHolidays.put(20150927, DAY_HOLIDAY);

    deferredHolidays.put(20160915, DAY_HOLIDAY);
    deferredHolidays.put(20160916, DAY_HOLIDAY);
    deferredHolidays.put(20160917, DAY_HOLIDAY);
    deferredHolidays.put(20160918, DAY_DEFERRED);

    // 国庆节
    deferredHolidays.put(20151001, DAY_HOLIDAY);
    deferredHolidays.put(20151002, DAY_HOLIDAY);
    deferredHolidays.put(20151003, DAY_HOLIDAY);
    deferredHolidays.put(20151004, DAY_HOLIDAY);
    deferredHolidays.put(20151005, DAY_HOLIDAY);
    deferredHolidays.put(20151006, DAY_HOLIDAY);
    deferredHolidays.put(20151007, DAY_HOLIDAY);
    deferredHolidays.put(20151008, DAY_DEFERRED);
    deferredHolidays.put(20151009, DAY_DEFERRED);
    deferredHolidays.put(20151010, DAY_DEFERRED);

    deferredHolidays.put(20161001, DAY_HOLIDAY);
    deferredHolidays.put(20161002, DAY_HOLIDAY);
    deferredHolidays.put(20161003, DAY_HOLIDAY);
    deferredHolidays.put(20161004, DAY_HOLIDAY);
    deferredHolidays.put(20161005, DAY_HOLIDAY);
    deferredHolidays.put(20161006, DAY_HOLIDAY);
    deferredHolidays.put(20161007, DAY_HOLIDAY);
    deferredHolidays.put(20161008, DAY_DEFERRED);
    deferredHolidays.put(20161009, DAY_DEFERRED);
  }

  public static int checkDay(int year, int month, int day) {
    int date = year * 10000 + month * 100 + day;
    return deferredHolidays.get(date, DAY_NONE);
  }
}
