package com.pikachu.record.tool;


import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 时间操作类工具
 */





public class ToolTime {



    ///////////时间操作//////////////////////


    /**
     * 获取系统时间
     *
     * @param itemType
     * @return
     */
    public static String getItem(String itemType){
        //"yyyy年MM月dd日 HH:mm:ss"
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(itemType);
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    public static String date2String(Date date,String itemType){
        //"yyyy年MM月dd日 HH:mm:ss"
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(itemType);
        return simpleDateFormat.format(date);
    }
    

    /**
     * 比较两个时间相差多少天
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            /*System.out.println("时间相差：" + day + "天" + hour + "小时" + min
                    + "分钟" + sec + "秒。");*/

            if (day>=1) {
                return day;
            }else {
                if (day==0) return 1;
                else return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 比较两个时间大小
     * @param date1
     * @param date2
     * @param format
     * @return
     */
    public static int compareDate(String date1, String date2, String format) {

        DateFormat df = new SimpleDateFormat(format);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                //返回 date1>date2
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //返回 date1<date2
                return -1;
            } else {
                //返回 date1=date2
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    /**
     * 判断两个时间是否相差 {long item}
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param item  相差的时间 (天)
     * @param eq  =0{相差的时间=item 返回true}  ，1{相差的时间<item 返回true} ，2{相差的时间>item 返回true}，3{相差的时间<=item 返回true},4{相差的时间>=item 返回true}
     * @param format
     * @return
     */
    public static boolean dateDiff(String startTime, String endTime,long item,int eq, String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
     /*       long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒*/

            if (eq==0){
                if (day==item)
                    return true;
                else
                    return false;
            }else if (eq==1){
                if (day<item)
                    return true;
                else
                    return false;

            }else if (eq==2){
                if (day>item)
                    return true;
                else
                    return false;

            }else if (eq==3){
                if (day<=item)
                    return true;
                else
                    return false;

            }else if (eq==4){
                if (day>=item)
                    return true;
                else
                    return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


   
    /**
     * 将时间戳转换成描述性时间（昨天、今天、明天）
     *
     * @return 描述性日期
     */
    public static String getTimeH(String dataStr, String itemType) {
        String descriptiveText = null;
        String format = itemType;
        
        SimpleDateFormat sdf = new SimpleDateFormat(itemType, Locale.CHINA);
        Date date=null;
        try {
            date = sdf.parse(dataStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        //当前时间
        Calendar currentTime = Calendar.getInstance();
        //要转换的时间
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        //年相同
        if (currentTime.get(Calendar.YEAR) == time.get(Calendar.YEAR)) {
            //获取一年中的第几天并相减，取差值
            switch (currentTime.get(Calendar.DAY_OF_YEAR) - time.get(Calendar.DAY_OF_YEAR)) {
                case 1://当前比目标多一天，那么目标就是昨天
                    descriptiveText = "昨天";
                    format = "HH:mm";
                    break;
                case 0://当前和目标是同一天，就是今天
                    descriptiveText = "今天";
                    format = "HH:mm";
                    break;
                case -1://当前比目标少一天，就是明天
                    descriptiveText = "明天";
                    format = "HH:mm";
                    break;
                default:
                    descriptiveText = null;
                    format = "MM/dd HH:mm";
                    break;
            }
            
        }else{
            descriptiveText = null;
            format = "yyyy/MM/dd HH:mm";
            
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        String formatDate = simpleDateFormat.format(time.getTime());
        if (!TextUtils.isEmpty(descriptiveText)) {
            return descriptiveText + " " + formatDate;
        }
        return formatDate;
    }
    
    
    
    /**
     * 获取当前月的第一天
     *
     * @return
     * @author
     */
    public static String  getThisMonthFirstDay(String itemType ){
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(itemType);
        return simpleDateFormat.format(cale.getTime());
    }
    /**
     * 获取当前月的最后一天
     *
     * @return
     * @author
     */
    public static String getThisMonthLastday(String itemType ) {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(itemType);
        return simpleDateFormat.format(cale.getTime());
    }


    /**
     * 获取本年的第一天
     * @return String
     * **/
    public static String getYearStart(){
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    
    
    // 把日期时间字符串的时间转换成毫秒值（RTC）
    public static long stringToMillis(String dateTime) {
        Calendar c = StringToGregorianCalendar(dateTime);

        return c.getTimeInMillis();
    }
    // 字符串转换成Calendar
    public static Calendar StringToGregorianCalendar(String dateTimeStr) {
        Date date = StringToDate(dateTimeStr);
        Calendar calendar = new GregorianCalendar();

        calendar.setTime(date);
        return calendar;
    }
    // 时间日期字符串转换成Date对象
    // 注：dateTimeStr带不带前导0都是可以的，比如"2011-01-01 01:02:03"和"2011-1-1 1:2:3"都是合法的
    public static Date StringToDate(String dateTimeStr) {
        Date date = new Date();
        // DateFormat fmt = DateFormat.getDateTimeInstance();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = fmt.parse(dateTimeStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    
    
    public static String getNextDay(Date date,int next,String itemType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, next);//+1今天的时间加一天
        date = calendar.getTime();
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(itemType);
        return simpleDateFormat.format(date);
        
    }
    




}
