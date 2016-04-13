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
package com.tomeokin.widget.jocalendarview;

import java.util.Calendar;

public class AdapterTest {
  public static void main(String[] args) {
    CalendarAdapter adapter = new CalendarAdapter(1, 2016, 4, 1);
    adapter.moveToLast();
    System.out.println(adapter.toString());

    adapter.moveToNext();
    adapter.moveToNext();
    System.out.println(adapter.toString());

    adapter.setCalendarModel(0);
    adapter.moveToLast();
    System.out.println(adapter.toString());

    adapter.moveToNext();
    adapter.moveToNext();
    System.out.println(adapter.toString());

    Calendar mCalendar = Calendar.getInstance();
    mCalendar.clear();
    String monthDay[][] = new String[6][7];

    Calendar time = Calendar.getInstance();
    time.clear();
    time.set(Calendar.YEAR, 2016); //year 为 int
    time.set(Calendar.MONTH, 4 - 1); //month 为int
    System.out.println(time.getActualMaximum(Calendar.DAY_OF_MONTH));

    //int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
    //int day = 1;
    //for (int i = 0; i < 6; i++) {
    //  for (int j = 0; j < 7; j++) {
    //    monthDay[i][j] = "";
    //    if (i == 0 && j >= dayOfWeek) {
    //      monthDay[i][j] = "" + day;
    //      day++;
    //    } else if (i > 0 && day <= daysInMonth) {
    //      monthDay[i][j] = "" + day;
    //      day++;
    //    }
    //  }
    //}
    String[][] str = new String[1][1];
    setString(str);
    System.out.println(str[0][0]);
  }

  private static void setString(String[][] a) {
    if (a != null) {
      a[0][0] = "hello";
    }
  }
}
