package com.wemade.board.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.wemade.board.common.constant.FrkConstants;
import com.wemade.board.framework.exception.BaseException;

public class DateUtil extends StringUtils {

    private static final String KOR_DATE_FORMA_STRING = "yyyy-MM-dd HH:mm:ss";
    private static final String KOR_DATE_STRING = "yyyyMMdd";
    private static final long DEFAULT_FINAL_TIME_STMAP = 253402268399L;    // '9999-12-31 23:59:59'을 초로 변환

    public static Date getCurDt() {
        Date dt = new Date();
        dt.setTime((dt.getTime() / 1000));
        return dt;
    }

    /**
     * '9999-12-31 23:59:59'을 초로 변환한 long타입의 데이터 리턴
     * @return
     */
    public static long getFnsDtLong() {
        return DEFAULT_FINAL_TIME_STMAP;
    }

    public static Timestamp getCurDtTimestamp() {
        return new Timestamp(getCurDt().getTime());
    }

    public static Timestamp getDtTimestamp(String dt) {
        return Timestamp.valueOf(dt);
    }

    public static Timestamp dateToTimestamp(Date dt) {
        if (dt == null)
            return null;

        return new Timestamp(dt.getTime());
    }

    public static String getCurDateString() {
        return getCurDtString(KOR_DATE_STRING);
    }

    public static String getCurDtString() {
        return getCurDtString(KOR_DATE_FORMA_STRING);
    }

    public static String getCurDtString(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(KOR_DATE_FORMA_STRING);
        String date = formatter.format(new Date());
        return date(format, date);
    }

    public static String date(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(KOR_DATE_FORMA_STRING);
        return date(format, formatter.format(date));
    }

    public static String date(String format, String date) {
        if (isEmpty(format))
            format = KOR_DATE_FORMA_STRING;
        if (isEmpty(date))
            return null;

        date = date.replaceAll("[^0-9]+", "");
        date = rightPad(date, 14, '0');
        date = date.replaceAll("(^[0-9]{4})([0-9]{2})([0-9]{2})([0-9]{2})([0-9]{2})([0-9]{2})", "$1-$2-$3 $4:$5:$6");
        SimpleDateFormat formatter = new SimpleDateFormat(KOR_DATE_FORMA_STRING);
        Date redate;
        try {
            redate = formatter.parse(date);
        } catch (ParseException e) {
            throw new BaseException(FrkConstants.CD_UNKNOWN);
        }
        formatter = new SimpleDateFormat(format);
        return formatter.format(redate);
    }

    /**
     * getTimeString
     *
     * @return (String) 시간
     * @throws Exception
     */
    public static String getTimeString() {
        return getTimeString("HH:mm:ss");
    }

    public static String getTimeString(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String time = formatter.format(new Date());
        return getTimeString(format, time);
    }

    public static String getTimeString(String format, String time) {
        time = time.replaceAll("[^0-9]+", "");
        time = rightPad(time, 6, '0');
        time = leftPad(time, 14, '0');
        return date(format, time);
    }

    /**
     * date에 날짜를 더하거나/빼는 메소드
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * 현재날짜에 날짜를 더하거나/빼는 메소드
     *
     * @param days
     * @return
     */
    public static Date addDays(int days) {
        Date targetDate = DateUtil.getCurDt();
        return addDays(targetDate, days);
    }

    /**
     * date에 시간을 더하거나/빼는 메소드
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours);
        return cal.getTime();
    }

    /**
     * 현재시간에 시간을 더하거나/빼는 메소
     *
     * @param hours
     * @return
     */
    public static Date addHours(int hours) {
        Date targetDate = DateUtil.getCurDt();
        return addHours(targetDate, hours);
    }

}
