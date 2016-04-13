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

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class WeekdayView extends ViewGroup {
  private static final int DEFAULT_DAYS_IN_WEEK = 7;
  private int mWeeks = 6;

  private final String[] mFieldItems = new String[] { "日", "一", "二", "三", "四", "五", "六" };
  private Paint mPaint;
  private int mFieldTextSize = (int) sp2px(12);
  private final Rect mFieldItemBound = new Rect();
  private int mCellSize;
  private int mFieldItemVerPadding = (int) dp2px(8);
  private final List<DayView> mDayViews;

  public WeekdayView(Context context) {
    this(context, null);
  }

  public WeekdayView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WeekdayView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setTextSize(mFieldTextSize);
    mPaint.getTextBounds(mFieldItems[0], 0, mFieldItems[0].length(), mFieldItemBound);

    mDayViews = new ArrayList<>(DEFAULT_DAYS_IN_WEEK * mWeeks);
    buildDayViews();
  }

  private void buildDayViews() {
    // TODO
    for (int i = 0; i < DEFAULT_DAYS_IN_WEEK * mWeeks; i++) {
      final DayView dayView = new DayView(getContext());
      dayView.setText("10");
      dayView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          dayView.toggleSelected();
          if (dayView.isSelected()) {
            dayView.setTextColor(Color.WHITE);
            dayView.setScaleX(1.1f);
            dayView.setScaleY(1.1f);
          } else {
            dayView.setTextColor(Color.BLACK);
            dayView.setScaleX(1.0f);
            dayView.setScaleY(1.0f);
          }
        }
      });
      LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
          LayoutParams.MATCH_PARENT);
      dayView.setLayoutParams(lp);
      dayView.setGravity(Gravity.CENTER);
      mDayViews.add(dayView);
      addView(dayView);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

    int width = getDefaultSize(0, widthMeasureSpec);
    int height = getDefaultSize(0, heightMeasureSpec);
    int size = Math.min(width, height);

    mCellSize = size / DEFAULT_DAYS_IN_WEEK;
    int cellSpec = MeasureSpec.makeMeasureSpec(mCellSize, MeasureSpec.EXACTLY);
    measureChildren(mCellSize, cellSpec);
    Log.i("take", "cellSize: " + mCellSize);

    setMeasuredDimension(size, size + mFieldItemBound.height() + mFieldItemVerPadding * 2);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    if (!changed) {
      return;
    }

    Log.i("take", "onLayout");
    final int fieldItemHeight = mFieldItemBound.height() + mFieldItemVerPadding * 2;
    final int childCount = getChildCount();
    int row, col, left, top;
    Log.i("take", "childCount: " + childCount);
    for (int i = 0; i < childCount; i++) {
      Log.i("take", "child");
      View child = getChildAt(i);
      row = i / DEFAULT_DAYS_IN_WEEK;
      col = i % DEFAULT_DAYS_IN_WEEK;
      left = col * mCellSize;
      top = row * mCellSize;
      child.layout(left, top + fieldItemHeight, left + mCellSize,
          top + fieldItemHeight + mCellSize);
    }
  }

  private float dp2px(float dp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        getResources().getDisplayMetrics());
  }

  private float sp2px(float sp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
        getResources().getDisplayMetrics());
  }
}
