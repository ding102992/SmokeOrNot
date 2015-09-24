package com.jingcai.apps.smokeornot.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Json Ding on 2015/4/29.
 */
public class DateUtil {
    public static final Calendar calendar = Calendar.getInstance();
    public static final long ONE_DAY_MILLISECONDS = 3600 * 1000 * 24;
    public static final long FULL_MONTH_MILLISECONDS = ONE_DAY_MILLISECONDS * 30;



    /**
     * 返回指定格式的日期，如果格式不对，返回null;
     * @param dateStr 时间字符串
     * @param format 格式
     * @return 时间
     */
    public static Date parseDate(String dateStr,String format){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
            return dateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取星期数0-6
     */
    public static int getWeek(){
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取星期列表，截至日前的一个星期
     */
    public static ArrayList<String> getWeekList(){
        ArrayList<String> weekStr = new ArrayList<>();
        int curWeek = getWeek();
        for(int i = 0 ; i < 7; i++){
            curWeek ++;
            if(curWeek > 7){
                curWeek %= 7;
            }
            switch (curWeek){
                case Calendar.SUNDAY : weekStr.add("周日");
                    break;
                case Calendar.MONDAY : weekStr.add("周一");
                    break;
                case Calendar.TUESDAY : weekStr.add("周二");
                    break;
                case Calendar.WEDNESDAY : weekStr.add("周三");
                    break;
                case Calendar.THURSDAY : weekStr.add("周四");
                    break;
                case Calendar.FRIDAY : weekStr.add("周五");
                    break;
                case Calendar.SATURDAY : weekStr.add("周六");
                    break;
            }
        }
        return weekStr;
    }

    /**
     * 获取指定日期的TimeInfo
     * @param date 指定日期
     * @return timeInfo
     */
    public static TimeInfo getTimeInfo(Date date,TimeInfoScope scope) {
        calendar.setTimeInMillis(date.getTime());
        TimeInfo timeInfoDay = new TimeInfo();
        TimeInfo timeInfoMonth = new TimeInfo();
        switch (scope){
            case DAY:
                //获取天的最先时刻
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                timeInfoDay.startTime = calendar.getTimeInMillis();

                //获取天的最后时刻
                calendar.set(Calendar.HOUR_OF_DAY,23);
                calendar.set(Calendar.MINUTE,59);
                calendar.set(Calendar.SECOND,59);
                calendar.set(Calendar.MILLISECOND,999);
                timeInfoDay.endTime = calendar.getTimeInMillis();
            case MONTH:
                //获取月的最先时刻
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                timeInfoMonth.startTime = calendar.getTimeInMillis();

                calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                timeInfoMonth.endTime = calendar.getTimeInMillis();
                break;
        }
        switch (scope){
            case DAY:
                return timeInfoDay;
            case MONTH:
                return timeInfoMonth;
            default:
                return timeInfoDay;
        }

    }
    /**
     * 一段时间的开始时间和结束时间的封装
     */
    public static class TimeInfo{
        public long startTime; //开始时间
        public long endTime;  //结束时间
    }

    public enum TimeInfoScope{
        DAY,MONTH
    }

}
