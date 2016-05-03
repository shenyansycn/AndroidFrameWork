package org.android.framework.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Date;

/**
 * 获得时间类 格式：yyyy/MM/dd kk:mm:ss
 *
 * @author shenyan
 */
public class TimeUtil {
    /**
     * 获得当前年
     *
     * @return
     */
    public static String getCurrentYear() {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(System.currentTimeMillis());
        String time = DateFormat.format("yyyy", timeCalendar).toString();
        return time;
    }

    /**
     * 获得年份
     *
     * @param timeMillis
     * @return
     */
    public static String getYear(Long timeMillis) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(timeMillis);
        String time = DateFormat.format("yyyy", timeCalendar).toString();
        return time;
    }

    /**
     * 获得当前月份
     *
     * @return
     */
    public static String getCurrentMonth() {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(System.currentTimeMillis());
        String time = DateFormat.format("MM", timeCalendar).toString();
        return time;
    }

    /**
     * 获得月份
     *
     * @param timeMillis
     * @return
     */
    public static String getMonth(Long timeMillis) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(timeMillis);
        String time = DateFormat.format("MM", timeCalendar).toString();
        return time;
    }

    /**
     * 获得当前天
     *
     * @return
     */
    public static String getCurrentDay() {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(System.currentTimeMillis());
        String time = DateFormat.format("dd", timeCalendar).toString();
        return time;

    }

    /**
     * 获得天
     *
     * @param timeMillis
     * @return
     */
    public static String getDay(Long timeMillis) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(timeMillis);
        String time = DateFormat.format("dd", timeCalendar).toString();
        return time;
    }

    /**
     * 获得当前小时
     *
     * @return
     */
    public static String getCurrentHour() {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(System.currentTimeMillis());
        String time = DateFormat.format("kk", timeCalendar).toString();
        return time;
    }

    /**
     * 获得小时
     *
     * @return
     */
    public static String getHour(Long timeMillis) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(timeMillis);
        String time = DateFormat.format("kk", timeCalendar).toString();
        return time;
    }

    /**
     * @param pattern 格式：yyyy/MM/dd kk:mm:ss
     * @return
     */
    public static String getTime(String pattern) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(System.currentTimeMillis());
        String time = DateFormat.format(pattern, timeCalendar).toString();
        return time;
    }

    /**
     * 返回时间
     *
     * @param pattern    格式：yyyy/MM/dd kk:mm:ss
     * @param timeMillis
     * @return
     */
    public static String getTime(String pattern, Long timeMillis) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(timeMillis);
        String time = DateFormat.format(pattern, timeCalendar).toString();
        return time;
    }


    public static String getCurrenMinute() {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(System.currentTimeMillis());
        String time = DateFormat.format("mm", timeCalendar).toString();
        return time;
    }

    public static String getMinute(Long timeMillis) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(timeMillis);
        String time = DateFormat.format("mm", timeCalendar).toString();
        return time;
    }

    public static String getCurrentSecond() {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(System.currentTimeMillis());
        String time = DateFormat.format("ss", timeCalendar).toString();
        return time;
    }

    public static String getSecond(Long timeMillis) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(timeMillis);
        String time = DateFormat.format("ss", timeCalendar).toString();
        return time;
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 格式化时间显示
     *
     * @param context
     * @param date
     * @return
     */
    public static String formatTimeStampString(Context context, long date) {
        return formatTimeStampString(context, date, false);
    }

    public static String formatTimeStampString(Context context, long date, boolean fullFormat) {
        Time then = new Time();
        then.set(date);
        Time now = new Time();
        now.setToNow();

        // Basic settings for formatDateTime() we want for all cases.
        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_CAP_AMPM;

        // If the message is from a different year, show the date and year.
        if (then.year != now.year) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (then.yearDay != now.yearDay) {
            // If it is from a different day than today, show only the date.
            format_flags |= DateUtils.FORMAT_SHOW_DATE;
        } else {
            // Otherwise, if the message is from today, show the time.
            format_flags |= DateUtils.FORMAT_SHOW_TIME;
        }

        // If the caller has asked for full details, make sure to show the date
        // and time no matter what we've determined above (but still make
        // showing
        // the year only happen if it is a different year from today).
        if (fullFormat) {
            format_flags |= (DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
        }

        return DateUtils.formatDateTime(context, date, format_flags);
    }

    public static String formatTimeStampString(Context context, long date, int flags) {
        Time then = new Time();
        then.set(date);
        // Basic settings for formatDateTime() we want for all cases.
        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_CAP_AMPM;
        format_flags |= (flags);
        return DateUtils.formatDateTime(context, date, format_flags);
    }
}
