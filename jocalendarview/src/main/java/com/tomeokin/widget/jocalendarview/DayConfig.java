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

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DayConfig {
  public static final int DAY_STATE_NORMAL = 0;
  public static final int DAY_STATE_TODAY = 1;
  public static final int DAY_STATE_WEEKEND = 2;

  @IntDef({DAY_STATE_NORMAL, DAY_STATE_TODAY, DAY_STATE_WEEKEND})
  @Retention(RetentionPolicy.SOURCE)
  public @interface DayState {}

  public static @ColorInt int DAY_NORMAL_TEXT_COLOR = Color.BLACK;
  public static @ColorInt int DAY_WEEKEND_TEXT_COLOR = Color.parseColor("#757575");
  public static @ColorInt int DAY_FESTIVAL_TEXT_COLOR = Color.parseColor("#414141");

  public static @ColorInt int DAY_SELECTED_TEXT_COLOR = Color.WHITE;
  public static @ColorInt int DAY_SELECTED_BACKGROUND_COLOR = Color.parseColor("#03a9f4");
  public static @ColorInt int DAY_TODAY_BACKGROUND_COLOR = Color.parseColor("#03a9f4");
  public static @ColorInt int DAY_DEFERRED_COLOR = Color.parseColor("#ff5722");
  public static @ColorInt int DAY_HOLIDAY_COLOR = Color.parseColor("#4fc3f7");

  public static @ColorInt int WEEK_DIVIDE_LINE = Color.parseColor("#bdbdbd");

  private DayConfig() {
  }
}
