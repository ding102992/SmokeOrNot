package com.jingcai.apps.smokeornot.entity.binding;

import android.databinding.ObservableInt;

/**
 * Created by JasonDing on 15/9/23.
 */
public class IndexStatisticData  {

    public ObservableInt todaySmokeCount = new ObservableInt();
    public ObservableInt todayNotSmokeCount = new ObservableInt();
    public ObservableInt yesterdaySmokeCount = new ObservableInt();
    public ObservableInt yesterdayNotSmokeCount = new ObservableInt();
    public ObservableInt totalSmokeCount = new ObservableInt();
    public ObservableInt totalNotSmokeCount = new ObservableInt();

}
