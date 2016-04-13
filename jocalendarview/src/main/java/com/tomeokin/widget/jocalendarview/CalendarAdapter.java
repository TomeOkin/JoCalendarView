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

public class CalendarAdapter {
  private int mCalendarModel = 0; // 月
  private int mWeekCount = 0;

  //private CalendarManager mCalendarManager = CalendarManager.getInstance();

  // use for draw content
  private int mCenterYear, mCenterMonth, mCenterWeek;
  private int mLeftYear, mLeftMonth, mLeftWeek;
  private int mRightYear, mRightMonth, mRightWeek;

  //private DateInfo[][] mCenterDateInfos;
  //private DateInfo[][] mLeftDateInfos;
  //private DateInfo[][] mRightDateInfos;

  public CalendarAdapter(int model, int year, int month, int week) {
    mCalendarModel = model;
    mCenterYear = year;
    mCenterMonth = month;
    mCenterWeek = week;

    mWeekCount = mCalendarModel == 0 ? 6 : 1;
  }

  public void moveToNext() {
    if (mCalendarModel == 1) {
      mCenterWeek = (mCenterWeek + 1) % 8;
      if (mCenterWeek == 0) {
        mCenterWeek = 1;
        mCenterMonth++;
      }
    } else {
      mCenterMonth++;
    }

    mCenterMonth %= 13;
    if (mCenterMonth == 0) {
      mCenterMonth = 1;
      mCenterYear++;
    }
  }

  public void moveToLast() {
    if (mCalendarModel == 1) {
      mCenterWeek = (mCenterWeek - 1) % 7;
      if (mCenterWeek == 0) {
        mCenterWeek = 7;
        mCenterMonth--;
      }
    } else {
      mCenterMonth--;
    }

    mCenterMonth %= 12;
    if (mCenterMonth == 0) {
      mCenterMonth = 12;
      mCenterYear--;
    }
  }

  private void computeMonth() {
    mRightYear = mLeftYear = mCenterYear;
    mLeftMonth = mCenterMonth - 1;
    mRightMonth = mCenterMonth + 1;

    if (mLeftMonth == 0) {
      mLeftYear--;
      mLeftMonth = 12;
    }

    if (mRightMonth == 13) {
      mRightYear++;
      mRightMonth = 1;
    }

    //mLeftDateInfos = mCalendarManager.obtainDateInfo(mLeftYear, mLeftMonth);
    //mCenterDateInfos = mCalendarManager.obtainDateInfo(mCenterYear, mCenterMonth);
    //mRightDateInfos = mCalendarManager.obtainDateInfo(mRightYear, mRightMonth);
  }

  public int getWeekCount() {
    return mCalendarModel == 0 ? 6 : 1;
  }

  @Override
  public String toString() {
    return "[year, month, week] == [" + mCenterYear + ", " + mCenterMonth + ", " + mCenterWeek + "]";
  }

  public int getCalendarModel() {
    return mCalendarModel;
  }

  public void setCalendarModel(int calendarModel) {
    this.mCalendarModel = calendarModel;
  }
}