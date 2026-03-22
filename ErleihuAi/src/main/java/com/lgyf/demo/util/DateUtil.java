package com.lgyf.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static void main(String[] args) throws ParseException {
//       System.out.println(getDateFormate(new Date(),"YYYYMMddhhmmssSSS"));
//        SimpleDateFormat sdf_old = new SimpleDateFormat("yyyy-MM-dd");
//        Date old_date =sdf_old.parse("2021-04-10");
//        System.out.println(DateUtil.get4yMd(old_date));
        Calendar calendar = Calendar.getInstance();
        System.out.println(Calendar.DATE);
        calendar.add(Calendar.DATE,-6);
        Date time = calendar.getTime();
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(time);
//        System.out.println(getDateFormate(new Date(),"yyyy-MM-dd"));
        System.out.println(yesterday);
    }


//    7(几)天前
    public static String other_day(int day){
        Calendar calendar = Calendar.getInstance();
//        System.out.println(Calendar.DATE);
        calendar.add(Calendar.DATE,-day);
        Date time = calendar.getTime();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(time);
        return date;
    }

    //得到当前的时间
    public static Date getDate() {
        Calendar canlendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        return canlendar.getTime();
    }

    public static long  StringToLong(String date) {
        return parse4yMdHms(date).getTime();
    }
     
    //提到指定的millis得到时间
    public static Date getDate(long millis) {
        Calendar canlendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        canlendar.clear();
        canlendar.setTimeInMillis(millis);
        return canlendar.getTime();
    }
 
    public static long getMillis() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).getTimeInMillis();
    }


    //得到指定日期的字符串(yyyy-MM-dd HH:mm:ss.SSS)
    public static String getDateFormate(Date date, String formate) {
        try {
            SimpleDateFormat simpleDateFormate = new SimpleDateFormat(formate);
            return simpleDateFormate.format(date);
        } catch (Exception e) {
        }
        return "";
    }
 
    //根据日期得到YYYY-MM-DD HH:MM:SS.SSS格式字符串
    public static String get4yMdHmsS(Date date) {
        return DateUtil.getDateFormate(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }
 
    //根据日期得到YYYY-MM-DD HH:MM:SS格式字符串
    public static String get4yMdHms(Date date) {
        return DateUtil.getDateFormate(date, "yyyy-MM-dd HH:mm:ss");
    }
 
    //根据日期得到YYYY-MM-DD HH:MM格式字符串
    public static String get4yMdHm(Date date) {
        return DateUtil.getDateFormate(date, "yyyy-MM-dd HH:mm");
    }
 
    //根据日期得到YYYY-MM-DD格式字符串
    public static String get4yMd(Date date) {
        return DateUtil.getDateFormate(date, "yyyy-MM-dd");
    }

    //根据日期得到YYYY-MM格式字符串
    public static String get4yM(Date date) {
        return DateUtil.getDateFormate(date, "yyyy-MM");
    }
    public static String getDhms(Date date) {
        return DateUtil.getDateFormate(date, "ddHHmmss");
    }
    public static String get4y(Date date) {
        return DateUtil.getDateFormate(date, "yyyy");
    }


    public static String get4hms(Date date) {
        return DateUtil.getDateFormate(date, "HH:mm:ss");
    }
    //把指定字符(yyyy-MM-dd HH:mm:ss.SSS)串转成Date
    public static Date parse4yMdHmsS(String sDate) {
        return DateUtil.parseDate(sDate, "yyyy-MM-dd HH:mm:ss.SSS");
    }
 
    //把指定字符(yyyy-MM-dd HH:mm:ss)串转成Date
    public static Date parse4yMdHms(String sDate) {
        return DateUtil.parseDate(sDate, "yyyy-MM-dd HH:mm:ss");
    }
 
    //把指定字符(yyyy-MM-dd HH:mm)串转成Date
    public static Date parse4yMdHm(String sDate) {
        return DateUtil.parseDate(sDate, "yyyy-MM-dd HH:mm");
    }
 
    //把指定字符(yyyy-MM-dd)串转成Date
    public static Date parse4yMd(String sDate) {
        return DateUtil.parseDate(sDate, "yyyy-MM-dd");
    }
 
    //根据指定格式,把字符串转成日期
    public static Date parseDate(String sDate, String formate) {
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat(formate);
        try {
            return simpleDateFormate.parse(sDate);
        } catch (ParseException e) {
            return null;
        }
    }
 
    //两个长整型的时间相差(时间的毫秒数),可以得到指定的毫秒数,秒数,分钟数,天数
    public static double getDifTwoTime(Date minuendTime, Date subtrahendTime, String tdatestr) {
        if (minuendTime == null || subtrahendTime != null) {
            return DateUtil.getDifTwoTime(minuendTime.getTime(), subtrahendTime.getTime(), tdatestr);
        }
        return 0;
    }
 
    //两个长整型的时间相差(时间的毫秒数),可以得到指定的毫秒数,秒数,分钟数,天数
    public static double getDifTwoTime(long minuendTime, long subtrahendTime, String tdatestr) {
        if (tdatestr == null || tdatestr.equals("")) {
            tdatestr = "MS";
        }
        double temp = 1;
        /** 毫秒数 */
        if ("MS".equalsIgnoreCase(tdatestr)) {
            temp = 1;
        }
        /** 得到秒 */
        if ("S".equalsIgnoreCase(tdatestr)) {
            temp = 1000;
        }
        /** 得到分 */
        if ("M".equalsIgnoreCase(tdatestr)) {
            temp = 1000 * 60;
        }
        /** 得到小时 */
        if ("H".equalsIgnoreCase(tdatestr)) {
            temp = 1000 * 60 * 60;
        }
        /** 得到天 */
        if ("D".equalsIgnoreCase(tdatestr)) {
            temp = 1000 * 60 * 60 * 24;
        }
        return (minuendTime - subtrahendTime) / temp;
    }
 
    //从日期中得到指定部分(YYYY/MM/DD/HH/SS/SSS)数字
    public static int getPartOfTime(Date date, String part) {
        Calendar canlendar = Calendar.getInstance();
        canlendar.clear();
        canlendar.setTime(date);
        if (part.equalsIgnoreCase("Y")) {//得到年
            return canlendar.get(Calendar.YEAR);
        }
        if (part.equalsIgnoreCase("M")) {//得到月
            return canlendar.get(Calendar.MONTH) + 1;
        }
        if (part.equalsIgnoreCase("D")) {//得到日
            return canlendar.get(Calendar.DAY_OF_MONTH);
        }
        if (part.equalsIgnoreCase("H")) {//得到时
            return canlendar.get(Calendar.HOUR_OF_DAY);
        }
        if (part.equalsIgnoreCase("M")) {//得到分
            return canlendar.get(Calendar.MINUTE);
        }
        if (part.equalsIgnoreCase("S")) {//得到秒
            return canlendar.get(Calendar.SECOND);
        }
        if (part.equalsIgnoreCase("MS")) {//得到毫秒
            return canlendar.get(Calendar.MILLISECOND);
        }
        return -1;
    }
    /**
      * 
      *********************************************************
      * @Title: getDayDate
      * @Description: 获取系统日期时间
      * @return Date
      * @date 2016-1-13
      ********************************************************
     */
    public static Date getDayDate(String format){
    	SimpleDateFormat sdf=new SimpleDateFormat(format);
    	Date dayDate=null;
    	try {
		 dayDate=sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
		}
    	return dayDate;
    }
    /**
     * 
     *********************************************************
     * @Title: getDayDateStr
     * @Description: 获取系统日期时间
     * @return Date
     * @date 2016-1-13
     ********************************************************
    */
   public static String getDayDateStr(String format){
   	SimpleDateFormat sdf=new SimpleDateFormat(format);
   	String dayDate=null;
   	try {
		 dayDate=sdf.format(new Date());
		} catch (Exception e) {
		}
   	return dayDate;
   }
   
   
}