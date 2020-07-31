package org.micah.core.util;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @program: eladmin-cloud
 * @description: 日期工具类
 * @author: Micah
 * @create: 2020-07-30 17:26
 **/
@Slf4j
public class DateUtil {

    /**
     * 日期和时间格式
     */
    public static final DateTimeFormatter DFY_MD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /** 日期格式*/
    public static final DateTimeFormatter DFY_MD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * LocalDateTime转时间戳
     * @param localDateTime
     * @return
     */
    public static Long getTimeStamp(LocalDateTime localDateTime){
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 时间戳转LocalDateTime
     * @param timeStamp
     * @return
     */
    public static LocalDateTime getLocalDateTime(Long timeStamp){
        return LocalDateTime.ofEpochSecond(timeStamp,0, OffsetDateTime.now().getOffset());
    }

    /**
     * LocalDateTime转化为Date
     * @param localDateTime
     * @return
     */
    public static Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate转化为Date
     * @param localDate
     * @return
     */
    public static Date toDate(LocalDate localDate){
        return DateUtil.toDate(localDate.atTime(LocalTime.now(ZoneId.systemDefault())));
    }

    /**
     * Date转化为LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault());
    }

    /**
     * 格式化LocalDateTime
     * @param localDateTime
     * @param patten
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime,String patten){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(patten);
        return dtf.format(localDateTime);
    }

    /**
     * 格式化LocalDateTime
     * @param localDateTime
     * @param dtf
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime,DateTimeFormatter dtf){
        return dtf.format(localDateTime);
    }

    /**
     * 格式化LocalDateTime为 yyyy-MM-dd HH:mm:ss
     * @param localDateTime
     * @return
     */
    public static String formatLocalDateTime2YmdHms(LocalDateTime localDateTime){
        return DFY_MD_HMS.format(localDateTime);
    }

    /**
     * 格式化LocalDateTime为 yyyy-MM-dd
     * @param localDateTime
     * @return
     */
    public static String formatLocalDateTime2Ymd(LocalDateTime localDateTime){
        return DFY_MD.format(localDateTime);
    }

    /**
     * 将字符串解析成日期，字符串格式为 yyyy-MM-dd HH:mm:ss
     * @param localDateTime
     * @param patten
     * @return
     */
    public static LocalDateTime parseString2LocalDateTime(String localDateTime,String patten){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(patten);
        return LocalDateTime.from(dtf.parse(localDateTime));
    }

    /**
     * 将字符串解析成日期，字符串格式为 yyyy-MM-dd
     * @param localDateTime
     * @param patten
     * @return
     */
    public static LocalDate parseString2LocalDate(String localDateTime,String patten){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(patten);
        return LocalDate.from(dtf.parse(localDateTime));
    }

    /**
     * 将字符串解析成日期，字符串格式为 yyyy-MM-dd
     * @param localDateTime
     * @param dtf
     * @return
     */
    public static LocalDateTime parseString2LocalDateTime(String localDateTime,DateTimeFormatter dtf){
        return LocalDateTime.from(dtf.parse(localDateTime));
    }

    /**
     * 将字符串解析成日期，字符串格式为 yyyy-MM-dd
     * @param localDateTime
     * @param dtf
     * @return
     */
    public static LocalDate parseString2LocalDate(String localDateTime,DateTimeFormatter dtf){
        return LocalDate.from(dtf.parse(localDateTime));
    }
    /**
     * 将字符串解析成日期，字符串格式为 yyyy-MM-dd HH:mm:ss
     * @param localDateTime
     * @return
     */
    public static LocalDateTime parseString2LocalDateTime(String localDateTime){
        return LocalDateTime.from(DFY_MD_HMS.parse(localDateTime));
    }

    /**
     * 将字符串解析成日期，字符串格式为 yyyy-MM-dd
     * @param localDateTime
     * @return
     */
    public static LocalDate parseString2LocalDate(String localDateTime){
        return LocalDate.from(DFY_MD.parse(localDateTime));
    }

    /**
     * 获取指定格式的当前时间字符串
     */
    public static String getNowLocalDateTime(String patten){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(patten);
        return dtf.format(LocalDate.now(ZoneId.systemDefault()));
    }

}
