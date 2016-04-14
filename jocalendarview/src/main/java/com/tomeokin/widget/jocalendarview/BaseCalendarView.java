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

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import com.tomeokin.widget.jocalendarview.biz.CalendarManager;
import com.tomeokin.widget.jocalendarview.biz.DateInfo;

public class BaseCalendarView extends View implements ValueAnimator.AnimatorUpdateListener {
  private static final int DEFAULT_DAYS_IN_WEEK = 7;
  private int mWeeksLines = 6;
  private int mShowWeeks = 1;

  private CalendarManager mCalendarManager = CalendarManager.getInstance();
  private float mCellSize;
  private TextPaint mTextPaint;
  private int mTextSize = (int) sp2px(14);
  private int mDecorateTextSize = (int) sp2px(9);

  private int mSelectedYear = -1;
  private int mSelectedMonth = -1;
  private int mSelectedX = -1;
  private int mSelectedY = -1;

  private ValueAnimator mValueAnimator;
  private float mRadio = 1.0f;
  private Paint mSelectedPaint;
  private Paint mTodayPaint;

  private static final float mDayBgRectSmall = 32;
  private static final float mDayCircleMaxRadio = 1.6f;

  private Scroller mScroller;
  private static final int TOUCH_STATE_REST = 0;
  private static final int TOUCH_STATE_SCROLLING = 1;
  private static final int TOUCH_STATE_DOWN = 2;
  private int mTouchState = TOUCH_STATE_REST;

  // 处理触摸事件 ~
  private float mThreadHoldWidth;
  private static final int SNAP_VELOCITY = 600;
  private int mTouchSlop = 0;
  private float mLastMotionX = 0;
  private float mLastPointX = 0;
  private VelocityTracker mVelocityTracker = null;
  private long mStartTime = 0;

  // use for draw offset
  private int mIndexYear, mIndexMonth;

  // use for draw content
  private int mCenterYear, mCenterMonth;
  private int mLeftYear, mLeftMonth;
  private int mRightYear, mRightMonth;

  private DateInfo[][] mCenterDateInfos;
  private DateInfo[][] mLeftDateInfos;
  private DateInfo[][] mRightDateInfos;

  private OnDateNoticeDraw mOnDateNoticeDraw;
  private OnDeferredHolidaysDraw mOnDeferredHolidaysDraw;

  public BaseCalendarView(Context context) {
    this(context, null);
  }

  public BaseCalendarView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BaseCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setColor(Color.BLACK);
    mTextPaint.setTextSize(mTextSize);
    mValueAnimator = ValueAnimator.ofObject(new FloatEvaluator(), 0, 1);
    mValueAnimator.addUpdateListener(this);
    mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTodayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTodayPaint.setStyle(Paint.Style.STROKE);
    mTodayPaint.setStrokeWidth(2f);

    setMonth(mCalendarManager.getDefaultYear(), mCalendarManager.getDefaultMonth());

    mScroller = new Scroller(context);
    // 初始化一个最小滑动距离
    mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
  }

  @Override
  public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      invalidate();
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = getDefaultSize(0, widthMeasureSpec);
    int height = getDefaultSize(0, heightMeasureSpec);
    float size = Math.min(width, height);

    mCellSize = size / DEFAULT_DAYS_IN_WEEK;

    setMeasuredDimension((int) size, (int) (mCellSize * mShowWeeks));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    drawView(canvas, getWidth() * (mIndexMonth - 1), getHeight() * mIndexYear, mLeftYear,
        mLeftMonth, mLeftDateInfos);
    drawView(canvas, getWidth() * mIndexMonth, getHeight() * mIndexYear, mCenterYear, mCenterMonth,
        mCenterDateInfos);
    drawView(canvas, getWidth() * (mIndexMonth + 1), getHeight() * mIndexYear, mRightYear,
        mRightMonth, mRightDateInfos);
  }

  private void drawView(Canvas canvas, float l, float t, int year, int month,
      DateInfo[][] dateInfos) {
    canvas.save();
    canvas.translate(l, t);

    float left, top;
    float offsetX;
    float centerX, centerY;
    boolean isSelected;
    DateInfo date;

    for (int j = 0; j < mShowWeeks; j++) {
      top = j * mCellSize;
      centerY = top + mCellSize / 2.0f;

      drawWeekView(canvas, centerY, j, year, month, dateInfos[j]);

      mTextPaint.setColor(DayConfig.WEEK_DIVIDE_LINE);
      canvas.drawLine(0, top + mCellSize, getWidth(), top + mCellSize, mTextPaint);
    }

    canvas.restore();
  }

  private void drawWeekView(Canvas canvas, float centerY, int j, int year, int month,
      DateInfo[] dateInfos) {
    float left;
    float offsetX;
    float centerX;
    boolean isSelected;
    DateInfo date;

    for (int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++) {
      left = i * mCellSize;
      centerX = left + mCellSize / 2.0f;
      isSelected =
          mSelectedYear == year && mSelectedMonth == month && i == mSelectedX && j == mSelectedY;
      date = dateInfos[i];

      if (isSelected) {
        drawClickableBg(canvas, centerX, centerY, date);

        mTextPaint.setColor(DayConfig.DAY_SELECTED_TEXT_COLOR);
        mTextPaint.setTextSize(mTextSize + 1);
      } else if (date.isWeekend) {
        mTextPaint.setColor(DayConfig.DAY_WEEKEND_TEXT_COLOR);
        mTextPaint.setTextSize(mTextSize);
      } else {
        mTextPaint.setColor(DayConfig.DAY_NORMAL_TEXT_COLOR);
        mTextPaint.setTextSize(mTextSize);
      }

      if (date.isToday) {
        mTodayPaint.setColor(DayConfig.DAY_TODAY_BACKGROUND_COLOR);
        canvas.drawCircle(centerX, centerY, mDayBgRectSmall * mDayCircleMaxRadio / 2.0f,
            mTodayPaint);

        mTextPaint.setTextSize(mTextSize + 1);
      }

      // 日期
      float w = mTextPaint.measureText(date.day);
      offsetX = (mCellSize - w) / 2.0f;
      canvas.drawText(date.day, left + offsetX,
          centerY - (mTextPaint.descent() + mTextPaint.ascent()) / 2.0f, mTextPaint);

      // 装饰物相对中心的偏移
      float centerOffset = mDayBgRectSmall / 2.0f;
      if (isSelected || date.isToday) {
        centerOffset *= mDayCircleMaxRadio;
      }

      // 右侧的 '休' 和 '班'
      if (mOnDeferredHolidaysDraw != null && !TextUtils.isEmpty(date.day)) {
        int deferredHoliday =
            mOnDeferredHolidaysDraw.isDeferredHoliday(year, month, Integer.parseInt(date.day));
        mTextPaint.setTextSize(mDecorateTextSize);
        if (deferredHoliday == OnDeferredHolidaysDraw.HOLIDAY) {
          mTextPaint.setColor(DayConfig.DAY_HOLIDAY_COLOR);
          canvas.drawText("休", centerX + centerOffset, centerY, mTextPaint);
        } else if (deferredHoliday == OnDeferredHolidaysDraw.DEFERRED) {
          mTextPaint.setColor(DayConfig.DAY_DEFERRED_COLOR);
          canvas.drawText("班", centerX + centerOffset, centerY, mTextPaint);
        }
      }

      // 底部农历和节日
      centerOffset += 14;
      if (date.isWeekend) {
        mTextPaint.setColor(DayConfig.DAY_WEEKEND_TEXT_COLOR);
      } else {
        mTextPaint.setColor(DayConfig.DAY_FESTIVAL_TEXT_COLOR);
      }
      mTextPaint.setTextSize(mDecorateTextSize);
      String festival = (String) TextUtils.ellipsize(date.festival, mTextPaint, mCellSize,
          TextUtils.TruncateAt.END);
      w = mTextPaint.measureText(festival);
      offsetX = (mCellSize - w) / 2.0f;
      canvas.drawText(festival, left + offsetX,
          centerY - (mTextPaint.descent() + mTextPaint.ascent()) / 2.0f + centerOffset, mTextPaint);

      // 上方 Notice
      centerOffset -= 4;
      if (mOnDateNoticeDraw != null && !TextUtils.isEmpty(date.day)) {
        if (mOnDateNoticeDraw.hasNoticeOnDate(year, month, Integer.parseInt(date.day))) {
          drawDateNotice(canvas, centerX, centerY - centerOffset, date);
        }
      }
    }
  }

  private void drawClickableBg(Canvas canvas, float centerX, float centerY, DateInfo date) {
    float size = mDayBgRectSmall * mRadio;
    mSelectedPaint.setColor(DayConfig.DAY_SELECTED_BACKGROUND_COLOR);
    canvas.drawCircle(centerX, centerY, size / 2.0f, mSelectedPaint);
  }

  private void drawDateNotice(Canvas canvas, float centerX, float centerY, DateInfo date) {
    mSelectedPaint.setColor(DayConfig.DAY_TODAY_BACKGROUND_COLOR);
    canvas.drawCircle(centerX, centerY, 4, mSelectedPaint);
  }

  // just for debug
  public void debugCurrentState() {
    Log.i("take", "mLeftYear: " + mLeftYear + " mLeftMonth: " + mLeftMonth);
    Log.i("take", "mCenterYear: " + mCenterYear + " mCurrentMonth: " + mCenterMonth);
    Log.i("take", "mRightYear: " + mRightYear + " mRightMonth: " + mRightMonth);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(event);

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (mScroller != null && !mScroller.isFinished()) {
          mScroller.abortAnimation();
        }

        mLastMotionX = event.getX();
        mLastPointX = event.getX();
        mTouchState = TOUCH_STATE_DOWN;

        mStartTime = System.currentTimeMillis();
        return true;
      case MotionEvent.ACTION_MOVE:
        int moveX = (int) (mLastMotionX - event.getX());
        scrollBy(moveX, 0);
        mLastMotionX = event.getX();
        mTouchState = TOUCH_STATE_SCROLLING;
        break;
      case MotionEvent.ACTION_UP:
        final VelocityTracker velocityTracker = mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000);

        int velocityX = (int) velocityTracker.getXVelocity();
        if (velocityX > SNAP_VELOCITY) { // 向右快速滑动
          moveToLast();
        } else if (velocityX < -SNAP_VELOCITY) { // 向左快速滑动
          moveToNext();
        } else if (event.getX() - mLastPointX > mThreadHoldWidth) { // 距离大于阀值，向左滑动
          moveToLast();
        } else if (event.getX() - mLastPointX < -mThreadHoldWidth) { // 距离小于负阀值，向右滑动
          moveToNext();
        } else if (mTouchState != TOUCH_STATE_SCROLLING || (mLastPointX - event.getX()
            < mTouchSlop)) { // not moving

          // 执行点击操作
          if (System.currentTimeMillis() - mStartTime <= 300) {
            mSelectedX = (int) Math.floor(event.getX() / mCellSize);
            mSelectedY = (int) Math.floor(event.getY() / mCellSize);
            mSelectedYear = mCenterYear;
            mSelectedMonth = mCenterMonth;
            start();
          }
        } else { // 保持在当前月份
          // TODO
        }

        computeMonth();
        smoothScrollTo(getWidth() * mIndexMonth, getHeight() * mIndexYear);

        mTouchState = TOUCH_STATE_REST;
        if (mVelocityTracker != null) {
          mVelocityTracker.recycle();
          mVelocityTracker = null;
        }
        break;
      case MotionEvent.ACTION_CANCEL:
        mTouchState = TOUCH_STATE_REST;
        if (mVelocityTracker != null) {
          mVelocityTracker.recycle();
          mVelocityTracker = null;
        }
        break;
    }

    return super.onTouchEvent(event);
  }

  private void moveToNext() {
    mIndexMonth++;
    mCenterMonth = (mCenterMonth + 1) % 13;
    if (mCenterMonth == 0) {
      mCenterMonth = 1;
      mCenterYear++;
    }
    //computeMonth();
    //smoothScrollTo(getWidth() * mIndexMonth, getHeight() * mIndexYear);
  }

  private void moveToLast() {
    mIndexMonth--;
    mCenterMonth = (mCenterMonth - 1) % 12;
    if (mCenterMonth == 0) {
      mCenterMonth = 12;
      mCenterYear--;
    }
  }

  private void smoothScrollTo(int fx, int fy) {
    int dx = fx - getScrollX();
    int dy = fy - getScrollY();
    mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, 500);
    invalidate();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mThreadHoldWidth = w * 0.5f;
  }

  public void setMonth(int year, int month) {
    mCenterYear = year;
    mCenterMonth = month;
    mIndexYear = 0;
    mIndexMonth = 0;
    computeMonth();

    requestLayout();
    invalidate();
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

    debugCurrentState();

    mLeftDateInfos = mCalendarManager.obtainDateInfo(mLeftYear, mLeftMonth);
    mCenterDateInfos = mCalendarManager.obtainDateInfo(mCenterYear, mCenterMonth);
    mRightDateInfos = mCalendarManager.obtainDateInfo(mRightYear, mRightMonth);
  }

  @Override
  public void onAnimationUpdate(ValueAnimator animation) {
    mRadio = (float) animation.getAnimatedValue();
    invalidate();
  }

  private void start() {
    if (mValueAnimator != null) {
      mRadio = 1.0f;
      mValueAnimator.setObjectValues(1.0f, mDayCircleMaxRadio);
      mValueAnimator.setDuration(100);
      mValueAnimator.start();
    }
  }

  private float sp2px(float sp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
        getResources().getDisplayMetrics());
  }

  public interface OnDateNoticeDraw {
    boolean hasNoticeOnDate(int year, int month, int day);
  }

  public OnDateNoticeDraw getOnDateNoticeDraw() {
    return mOnDateNoticeDraw;
  }

  public void setOnDateNoticeDraw(OnDateNoticeDraw onDateNoticeDraw) {
    this.mOnDateNoticeDraw = onDateNoticeDraw;
  }

  public interface OnDeferredHolidaysDraw {
    int NONE = 0;
    int HOLIDAY = 1;
    int DEFERRED = 2;

    int isDeferredHoliday(int year, int month, int day);
  }

  public OnDeferredHolidaysDraw getOnDeferredHolidaysDraw() {
    return mOnDeferredHolidaysDraw;
  }

  public void setOnDeferredHolidaysDraw(OnDeferredHolidaysDraw onDeferredHolidaysDraw) {
    this.mOnDeferredHolidaysDraw = onDeferredHolidaysDraw;
  }
}
