package rule.engine.aviator.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author tao.yang
 * @date 2018-11-24
 */
public class DateUtils {

    /**
     * 默认的日期时间格式 : yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认的日期格式 : yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 紧凑的日期格式 yyyyMMdd
     */
    public static final String COMPACT_DATE_PATTERN = "yyyyMMdd";

    /**
     * 紧凑的日期时间格式:yyyyMMdd HH:mm:ss
     */
    public static final String COMPACT_DATETIME_PATTERN = "yyyyMMddHHmmss";

    /**
     * 以斜杠分割的日期格式：yyyy/MM/dd
     */
    public static final String SLASH_DATE_PATTERN = "yyyy/MM/dd";

    /**
     * 以斜杠分割的日期格式：yyyy/MM/dd HH:mm:ss
     */
    public static final String SLASH_DATETIME_PATTERN = "yyyy/MM/dd HH:mm:ss";

    // -----------------------------------------------------------------------

    /**
     * 将Date类型转换为字符串
     *
     * @param date 日期类型
     * @return 日期字符串 yyyy-MM-dd HH:mm:ss
     */
    public static String format(Date date) {
        return DateFormatUtils.format(date, DEFAULT_DATETIME_PATTERN);
    }

    /**
     * 将Date类型转换为字符串
     *
     * @param date 日期类型
     * @param pattern 字符串格式 @see DateUtils
     * @return pattern指定格式日期字符串
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 将Calendar转换为字符串
     *
     * @param calendar
     * @param pattern
     * @return pattern指定格式的字符串
     */
    public static String format(final Calendar calendar, final String pattern) {
        return DateFormatUtils.format(calendar, pattern);
    }

    // -----------------------------------------------------------------------

    /**
     * 将yyyy-MM-dd格式的字符串转为日期
     *
     * @param str 日期字符串
     * @return Date
     * @throws ParseException
     */
    public static Date toDate(final String str) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str, DEFAULT_DATE_PATTERN);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串转为日期
     *
     * @param str 日期字符串 yyyy-MM-dd HH:mm:ss
     * @throws ParseException
     */
    public static Date dateTime2Date(final String str) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str, DEFAULT_DATETIME_PATTERN);
    }

    /**
     * 将字符串转为日期
     *
     * @param str 日期字符串
     * @param parsePatterns 日期模块
     * @return Date
     * @throws ParseException
     */
    public static Date toDate(final String str, final String parsePatterns) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str, parsePatterns);
    }

    // -----------------------------------------------------------------------

    /**
     * Converts a {@code Date} into a {@code Calendar}.
     *
     * @param date the date to convert to a Calendar
     * @return the created Calendar
     * @throws NullPointerException if null is passed in
     * @since 3.0
     */
    public static Calendar toCalendar(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.toCalendar(date);
    }

    // -----------------------------------------------------------------------

    /**
     * Adds a number of years to a date returning a new object. The original {@code Date} is unchanged.
     *
     * @param date the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addYears(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addYears(date, amount);
    }

    /**
     * Adds a number of months to a date returning a new object. The original {@code Date} is unchanged.
     *
     * @param date the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMonths(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
    }

    /**
     * Adds a number of weeks to a date returning a new object. The original {@code Date} is unchanged.
     *
     * @param date the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addWeeks(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addWeeks(date, amount);
    }

    /**
     * Adds a number of days to a date returning a new object. The original {@code Date} is unchanged.
     *
     * @param date the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addDays(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
    }

    /**
     * Adds a number of hours to a date returning a new object. The original {@code Date} is unchanged.
     *
     * @param date the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addHours(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addHours(date, amount);
    }

    /**
     * Adds a number of minutes to a date returning a new object. The original {@code Date} is unchanged.
     *
     * @param date the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMinutes(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMinutes(date, amount);
    }

    /**
     * Adds a number of seconds to a date returning a new object. The original {@code Date} is unchanged.
     *
     * @param date the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addSeconds(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, amount);
    }

    /**
     * Adds a number of milliseconds to a date returning a new object. The original {@code Date} is unchanged.
     *
     * @param date the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMilliseconds(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMilliseconds(date, amount);
    }

    // -----------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        Date date = new Date();
        System.out.println(DateUtils.format(date));
        System.out.println(DateUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(DateUtils.format(date, COMPACT_DATE_PATTERN));
        System.out.println(DateUtils.format(date, COMPACT_DATETIME_PATTERN));
        System.out.println(DateUtils.format(date, SLASH_DATE_PATTERN));
        System.out.println(DateUtils.format(date, SLASH_DATETIME_PATTERN));

        System.out.println(org.apache.commons.lang3.time.DateUtils.setDays(date, 2));

        System.out.println(DateUtils.toDate("2018-01-01"));
        System.out.println(DateUtils.dateTime2Date("2018-01-01 01:01:01"));

    }

}
