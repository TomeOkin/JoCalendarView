package com.tomeokin.jocalendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import com.tomeokin.utils.jocalendarutils.DeferredHolidaysUtils;
import com.tomeokin.widget.jocalendarview.BaseCalendarView;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
    implements BaseCalendarView.OnDateNoticeDraw, BaseCalendarView.OnDeferredHolidaysDraw {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //final DayView dayView = new DayView(this);
    //dayView.setText("10");
    //dayView.setTextColor(Color.BLACK);
    ////ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
    ////    ViewGroup.LayoutParams.WRAP_CONTENT);
    //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(102, 102);
    //dayView.setLayoutParams(lp);
    //dayView.setPadding(16, 16, 16, 16);
    //dayView.setGravity(Gravity.CENTER);
    //
    //dayView.setOnClickListener(new View.OnClickListener() {
    //  @Override
    //  public void onClick(View v) {
    //    dayView.toggleSelected();
    //    if (dayView.isSelected()) {
    //      dayView.setTextColor(Color.WHITE);
    //      dayView.setScaleX(1.1f);
    //      dayView.setScaleY(1.1f);
    //    } else {
    //      dayView.setTextColor(Color.BLACK);
    //      dayView.setScaleX(1.0f);
    //      dayView.setScaleY(1.0f);
    //    }
    //  }
    //});
    //setContentView(dayView);

    //WeekdayView weekdayView = new WeekdayView(this);
    //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
    //    ViewGroup.LayoutParams.WRAP_CONTENT);
    //weekdayView.setLayoutParams(lp);
    //setContentView(weekdayView);

    final BaseCalendarView baseCalendarView = new BaseCalendarView(this);
    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    baseCalendarView.setLayoutParams(lp);
    Calendar now = Calendar.getInstance();
    baseCalendarView.setMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
    baseCalendarView.setOnDateNoticeDraw(this);
    baseCalendarView.setOnDeferredHolidaysDraw(this);
    setContentView(baseCalendarView);

    //setContentView(R.layout.activity_main);
  }

  @Override
  public boolean hasNoticeOnDate(int year, int month, int day) {
    return year == 2016 && month == 4 && (day == 14 || day == 25);
  }

  @Override
  public int isDeferredHoliday(int year, int month, int day) {
    return DeferredHolidaysUtils.checkDay(year, month, day);
  }
}
