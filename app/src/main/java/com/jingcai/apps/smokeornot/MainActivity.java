package com.jingcai.apps.smokeornot;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jingcai.apps.smokeornot.Util.DateUtil;
import com.jingcai.apps.smokeornot.databinding.ActivityMainBinding;
import com.jingcai.apps.smokeornot.entity.SmokeRecord;
import com.jingcai.apps.smokeornot.entity.binding.IndexStatisticData;
import com.jingcai.apps.smokeornot.persist.Database;

import java.util.Date;

public class MainActivity extends Activity {

    public final IndexStatisticData statisticData = new IndexStatisticData();
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewDataBinding.setStatisticData(statisticData);
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = Database.getInstance(this);
        db.open();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    private void initData() {
        DateUtil.TimeInfo todayTimeInfo = DateUtil.getTimeInfo(new Date(), DateUtil.TimeInfoScope.DAY);
        DateUtil.TimeInfo yesterdayTimeInfo = DateUtil.getTimeInfo(new Date(System.currentTimeMillis() - 3600 * 1000 * 24), DateUtil.TimeInfoScope.DAY);

        int todaySmokeCount = db.getSmokeCount(todayTimeInfo.startTime, todayTimeInfo.endTime, true);
        int todayNotSmokeCount = db.getSmokeCount(todayTimeInfo.startTime, todayTimeInfo.endTime, false);
        int yesterdaySmokeCount = db.getSmokeCount(yesterdayTimeInfo.startTime, yesterdayTimeInfo.endTime, true);
        int yesterdayNotSmokeCount = db.getSmokeCount(yesterdayTimeInfo.startTime, yesterdayTimeInfo.endTime, false);
        int totalSmokeCount = db.getSmokeCount(true);
        int totalNotSmokeCount = db.getSmokeCount(false);

        statisticData.totalSmokeCount.set(totalSmokeCount);
        statisticData.totalNotSmokeCount.set(totalNotSmokeCount);
        statisticData.yesterdaySmokeCount.set(yesterdaySmokeCount);
        statisticData.yesterdayNotSmokeCount.set(yesterdayNotSmokeCount);
        statisticData.todaySmokeCount.set(todaySmokeCount);
        statisticData.todayNotSmokeCount.set(todayNotSmokeCount);
    }

    @BindingAdapter("android:layout_weight")
    public static void setLayout_weight(View view, int weight) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (weight == 0) weight = 1;
        layoutParams.weight = weight;
        view.setLayoutParams(layoutParams);
    }

    public void doSmoke(View view) {
        addRecord(true);
        statisticData.totalSmokeCount.set(statisticData.totalSmokeCount.get() + 1);
        statisticData.todaySmokeCount.set(statisticData.todaySmokeCount.get() + 1);
    }

    public void doNotSmoke(View view) {
        addRecord(false);
        statisticData.totalNotSmokeCount.set(statisticData.totalNotSmokeCount.get() + 1);
        statisticData.todayNotSmokeCount.set(statisticData.todayNotSmokeCount.get() + 1);
    }

    private void addRecord(boolean isSmoke) {
        SmokeRecord record = new SmokeRecord(0, new Date(), isSmoke);
        db.addSmokeRecord(record);
    }
}
