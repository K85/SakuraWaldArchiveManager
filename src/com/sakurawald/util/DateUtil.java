package com.sakurawald.util;


import com.sakurawald.debug.LoggerManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * @return 返回本地当前时间的详细文本, 并且该文本是符合Windows文件命名规则的.
     */
    public static String getCurrentDateDetailString() {
        String result = DateUtil.getDateDetail(Calendar.getInstance());

        result = result.replace(":", "-");
        return result;
    }


    /**
     * 将<某个时间>与<当前时间>比较
     *
     * @return 输入的日期在现在之后，则返回1. 输入的日期在现在之前，则返回-1. 若输入日期与现在一样，则返回0.
     */
    public static int compareDate(Calendar date) {
        return DateUtil.compareDate(date, Calendar.getInstance());
    }

    public static int compareDate(Calendar date1, Calendar date2) {

        Calendar date_clone = (Calendar) date1.clone();
        Calendar date2_clone = (Calendar) date2.clone();

        DateUtil.setZero(date_clone);
        DateUtil.setZero(date2_clone);

        return date_clone.compareTo(date2_clone);
    }

    /**
     * 获取两个Date的时间差.
     *
     * @param date1 小时间
     * @param date2 大时间
     * @return 时间差
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {

        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));

        return days;

    }

    /**
     * @return 计算<两个日期>相差多少分钟.
     */
    public static long diffMinutes(Calendar big, Calendar small) {

        long nm = 1000 * 60;// 每分钟毫秒数

        long diff = big.getTime().getTime() - small.getTime().getTime(); // 获得两个时间的毫秒时间差异

        long result = diff / nm;

        return result;
    }

    /**
     * @return 计算两个日期相差多少秒.
     */
    public static long diffSeconds(Calendar big, Calendar small) {

        long nm = 1000;

        long diff = big.getTime().getTime() - small.getTime().getTime(); // 获得两个时间的毫秒时间差异

        long result = diff / nm;

        return result;
    }


    public static Calendar getDataDetail(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            LoggerManager.logException(e);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(d);

        return c;
    }

    public static Calendar getDataSimple(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            LoggerManager.logException(e);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(d);

        return c;
    }

    public static String getDateDetail(Calendar c) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(c.getTime());
    }

    public static String getDateSimple(Calendar c) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(c.getTime());
    }

    public static Date getNowDate() {
        return Calendar.getInstance().getTime();
    }

    public static int getNowDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getNowDayOfWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getNowHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getNowMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static int getNowMonth() {
        return (Calendar.getInstance().get(Calendar.MONTH) + 1);
    }

    public static int getNowSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    public static int getNowYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static long getUnixTime_Ms() {
        return System.currentTimeMillis();
    }

    public static long getUnixTime_S() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 将传入的Calendar的<小时>和<分钟>设定为0.
     */
    public static Calendar setZero(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        return c;
    }

    public static Calendar translate_Date_To_Calendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Calendar translate_TimeStamp_Ms_To_Calendar(long timestamp_Ms) {
        return translate_Date_To_Calendar(translate_TimeStamp_Ms_To_Date(timestamp_Ms));
    }

    public static Date translate_TimeStamp_Ms_To_Date(long timestamp_Ms) {
        return new Date(timestamp_Ms);
    }

}
