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
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

@Deprecated
public class DayView extends TextView implements ValueAnimator.AnimatorUpdateListener {
  private Paint mPaint;
  //private @ColorInt int mColor = Color.parseColor("#34bec7");
  private ValueAnimator valueAnimator;
  private float mRadio;
  private boolean mSelected = false;
  private @DayConfig.DayState int mDayState = DayConfig.DAY_STATE_NORMAL;
  private Rect mTextBound = new Rect();

  public DayView(Context context) {
    this(context, null);
  }

  public DayView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    valueAnimator = ValueAnimator.ofObject(new FloatEvaluator(), 0, 1);
    valueAnimator.addUpdateListener(this);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (!mSelected) {
      mRadio = 0;
    }
    //float innerPadding =
    //    (getPaddingRight() + getPaddingLeft() + getPaddingTop() + getPaddingBottom()) / 4.0f;
    //float size = Math.min(getWidth(), getHeight()) - innerPadding * 2; // half as innerPadding
    float size = Math.max(mTextBound.width(), mTextBound.height());
    size *= mRadio;
    mPaint.setColor(DayConfig.DAY_SELECTED_BACKGROUND_COLOR);

    canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, size / 2.0f, mPaint);

    super.onDraw(canvas);
  }

  @Override
  public void onAnimationUpdate(ValueAnimator animation) {
    mRadio = (float) animation.getAnimatedValue();
    invalidate();
  }

  //public int getColor() {
  //  return mColor;
  //}
  //
  //public void setColor(int color) {
  //  this.mColor = color;
  //}

  @Override
  public void setText(CharSequence text, BufferType type) {
    super.setText(text, type);
    if (mTextBound == null) {
      mTextBound = new Rect();
    }
    getPaint().getTextBounds(getText().toString(), 0, getText().length(), mTextBound);
  }

  private void start() {
    mSelected = true;

    if (valueAnimator != null) {
      mRadio = 1.0f;
      valueAnimator.setObjectValues(1.0f, 1.5f);
      valueAnimator.setDuration(100);
      valueAnimator.start();
    }
  }

  public void setDayState(@DayConfig.DayState int dayState) {
    mDayState = dayState;
  }

  public int getDayState() {
    return mDayState;
  }

  public boolean isSelected() {
    return mSelected;
  }

  public void setSelected(boolean selected) {
    this.mSelected = selected;

    if (mSelected) {
      start();
    }
  }

  public void toggleSelected() {
    setSelected(!mSelected);
  }
}
