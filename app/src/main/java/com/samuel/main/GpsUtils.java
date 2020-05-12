/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.samuel.main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;






import net.sourceforge.pinyin4j.PinyinHelper;



import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

//import net.sourceforge.pinyin4j.PinyinHelper;
//import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
//import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 工具函数类
 * 
 * @author 解玉芳 2012-01-08 创建<br>
 * 
 */
public class GpsUtils {

    static final int MAGIC_NUMBER = 0x55;
    static final int MONTH_DAY[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    /**
     * 将1/1000秒转换成1/1024单位的数据
     * 
     * @param value
     * @return
     */
    public static int get1024Changed(int value) {
        return (int) (value * ((float) 1024 / 1000));
    }

    /**
     * 将1/1024秒转换成1/1000单位的数据
     * 
     * @param value
     * @return
     */
    public static int get1000Changed(int value) {
        return (int) (value * ((float) 1000 / 1024));
    }
    /**
     * 获取当前日期和时间
     * 
     * @return YYYY-MM-dd HH:mm:ss格式输出时间
     */
    public static String getDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }

    /**
     * 获取当前日期
     * 
     * @return YYYY-MM-dd 格式输出时间
     */
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    /**
     * 获取打卡时间
     * 
     * @return HH:mm:ss格式输出时间
     */
    public static String getDTime() {
        String datetime = getDateTime();
        return datetime.substring(11);
    }

    /**
     * 获取时间
     * 
     * @return HH:mm格式输出时间
     */
    public static String getFTime() {
        String datetime = getDateTime();
        return datetime.substring(11, 16);
    }

    /**
     * 获取指定月份的月天数
     * 
     * @param year
     *            指定的年份
     * @param month
     *            指定的月份
     * 
     * @return 月天数
     */
    public static int getDay(int year, int month) {
        if (month > 12 || month == 0) {
            return 30;
        }
        if (month == 2) {
            // 判断是否为闰年
            if ((year % 4) == 0) {
                return 29;
            } else {
                return 28;
            }
        } else {
            return MONTH_DAY[month - 1];
        }
    }

    /**
     * 获取从2000年到date一共有多少天
     * 
     * @param date
     *            ：输入格林尼志日期
     * @return 总共天数
     */
    public static int getAllDays(int year, int month, int day) {
        int yearDays = 0, monthDays = 0, allDays = 0;

        if (year == 0) {
            yearDays = 0;
        } else {
            yearDays = year * 365 + ((year - 1) / 4 + 1) * 1;
        }
        switch (month) {
        case 1:
            monthDays = 0;
            break;
        case 2:
            monthDays = 31;
            break;
        case 3:
            monthDays = 31 + getDay(year, 2);
            break;
        case 4:
            monthDays = 62 + getDay(year, 2);
            break;
        case 5:
            monthDays = 92 + getDay(year, 2);
            break;
        case 6:
            monthDays = 123 + getDay(year, 2);
            break;
        case 7:
            monthDays = 153 + getDay(year, 2);
            break;
        case 8:
            monthDays = 184 + getDay(year, 2);
            break;
        case 9:
            monthDays = 215 + getDay(year, 2);
            break;
        case 10:
            monthDays = 245 + getDay(year, 2);
            break;
        case 11:
            monthDays = 276 + getDay(year, 2);
            break;
        case 12:
            monthDays = 306 + getDay(year, 2);
            break;
        default:
            break;
        }
        allDays = yearDays + monthDays + day;
        return allDays;
    }

    /**
     * 获取两个输入时间的差值(YYYY-MM-dd HH:mm:ss格式输入)
     * 
     * @return 单位s
     */
    public static long getDateTimeDiffer(String startTime, String endTime) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin;
        Date end;
        long between = 0;
        try {
            begin = dfs.parse(startTime);
            end = dfs.parse(endTime);
            between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Math.abs(between);
    }

    /** 判断date1是否在date2或date3的月份内 */
    public static boolean isBetweenDate(String date1, String date2, String date3) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        int month1,month2,month3;
        try {
            month1 = getCurDateBytes(formatter.format(formatter.parse(date1)))[1];
            month2 = getCurDateBytes(formatter.format(formatter.parse(date2)))[1];
            month3 = getCurDateBytes(formatter.format(formatter.parse(date3)))[1];
            if (month1 <= month2 && month1 <= month3) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 获取某一个日期的年月日并保存到数组中
     * 
     * @return 保存年月日信息的数组
     */
    public static int[] getCurDateBytes(String date) {
        int[] curDate = { 2000, 1, 1 };
        String[] array = date.split("-");
        if (array != null && array.length == 3) {
            curDate[0] = strToInt(array[0]);
            curDate[1] = strToInt(array[1]);
            curDate[2] = strToInt(array[2]);
            return curDate;
        }
        return curDate;
    }

    /**
     * 获取日期字节数组
     * 
     * @param date
     * @return
     */
    public static int[] getDateBytes(String date) {
        return getCurDateBytes(date);
    }

    /**
     * 获取时间字节数组
     * 
     * @param time
     * @return
     */
    public static int[] getTimeBytes(String time) {
        int[] result = { 0, 0 };
        try {
            String[] array = time.split(":");
            if (array != null && array.length == 2) {
                result[0] = strToInt(array[0]);
                result[1] = strToInt(array[1]);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取日期时间字节数组
     * 
     * @param time
     * @return
     */
    public static int[] getDateTimeBytes(String time) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (parserDateStr(time, calendar)) {
            int[] result = new int[6];
            result[0] = calendar.get(Calendar.YEAR);
            result[1] = calendar.get(Calendar.MONTH);
            result[2] = calendar.get(Calendar.DATE);
            result[3] = calendar.get(Calendar.HOUR_OF_DAY);
            result[4] = calendar.get(Calendar.MINUTE);
            result[5] = calendar.get(Calendar.SECOND);
            return result;
        } else {
            return null;
        }
    }

    /**
     * 获取当前星期 中文
     * 
     * @param weekday
     *            当前星期索引, 周日=1, 周一=2, 周二=3, 以此类推
     * 
     * @return string 星期一，星期二等
     */
    public static String getWeekdayChineseName(int weekday) {
        String result = null;
        switch (weekday) {
        case 1:
            result = "星期日";
            break;
        case 2:
            result = "星期一";
            break;
        case 3:
            result = "星期二";
            break;
        case 4:
            result = "星期三";
            break;
        case 5:
            result = "星期四";
            break;
        case 6:
            result = "星期五";
            break;
        case 7:
            result = "星期六";
            break;
        default:
            break;
        }
        return result;
    }

    /**
     * 判断当前日期是星期几
     * 
     * @param pTime
     *            设置的需要判断的时间 //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int getWeek(String pTime) {
        int Week = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week = 0;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week = 1;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week = 2;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week = 3;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week = 4;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week = 5;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week = 6;
        }
        return Week;
    }

    /**
     * 获取指定日期 当前星期 中文
     * 
     * @return string 星期一，星期二等
     */
    public static String getWeekdayChineseName(String date) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (parserDateStr(date, calendar)) {
            return getWeekdayChineseName(calendar.get(Calendar.DAY_OF_WEEK));
        }
        return null;
    }

    /**
     * 获取毫秒级时间 转化为十六进制字符串 补上成十六位 通常用于拍照本地生成的ID
     * 
     * @return
     */
    public static String getMilliTime() {
        String str = Long.toHexString(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        if (str.length() < 16) {
            for (int i = 0; i < 16 - str.length(); i++) {
                sb.append("0");
            }
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 获取time日期接下来days天后的日期,数组形式返回
     * 
     * @return 0:year,1:month,2:day
     */
    public static int[] getNextDate(String time, int days) {
        String nextDate = getNextDateString(time, days);
        return getCurDateBytes(nextDate);
    }

    /**
     * 获取time日期接下来days天后的日期,字符串形式返回
     * 
     * @return
     */
    public static String getNextDateString(String time, int days) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar calendar = new GregorianCalendar();
        if (parserDateStr(time, calendar)) {
            calendar.add(Calendar.DATE, days);
            return formatter.format(calendar.getTime());
        }
        return null;
    }

    /**
     * 开始日期是否早于或等于结束日期
     * 
     * @return true:是 false: 否
     */
    public static boolean isStartDateBeforeEndDate(String starttime,
            String endtime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date startdate = new Date();
        Date enddate = new Date();
        try {
            startdate = formatter.parse(starttime);
            enddate = formatter.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (startdate.compareTo(enddate) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 开始时间是否早于或等于结束时间
     * 
     * @return true:是 false: 否
     */
    public static boolean isStartTimeBeforeEndTime(String starttime,
            String endtime) {
        SimpleDateFormat formatter = null;
        if (starttime.length() > 5) {
            formatter = new SimpleDateFormat("HH:mm:ss");
        } else {
            formatter = new SimpleDateFormat("HH:mm");
        }
        Date startdate = new Date();
        Date enddate = new Date();
        try {
            startdate = formatter.parse(starttime);
            enddate = formatter.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (startdate.compareTo(enddate) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 开始日期是否早于或等于结束日期
     * 
     * @return true:是 false: 否
     */
    public static boolean isStartDateTimeBeforeEndDateTime(String starttime,
            String endtime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startdate = new Date();
        Date enddate = new Date();
        try {
            startdate = formatter.parse(starttime);
            enddate = formatter.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (startdate.compareTo(enddate) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 结束时间 是否大于等于 开始时间+天数
     * 
     * @return true:是 false: 否
     */
    public static boolean isEnddateAfterStartdateAddDays(String starttime,
            String endtime, int days) {
        String nextDay = getNextDateString(starttime, days);
        if (nextDay.compareTo(endtime) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 对CELL ID进行转换
     * 
     * @param [in] id: 待转换的CELL ID
     * @return 转换后的CELL ID
     */
    public static int changeCellId(int id) {
        short tmp1, tmp2, tmp3;

        // 低5位
        tmp1 = (short) (id & 0x001f);

        // 高5位
        tmp2 = (short) (id & 0xf800);

        // 中6位
        tmp3 = (short) (id & 0x07e0);

        tmp3 = (short) ~tmp3;
        tmp3 &= ~0xf81f;

        id = (tmp1 << 11) | tmp3 | (tmp2 >> 11);
        return id;
    }

    /**
     * 对读写数据进行掩码处理
     * 
     * @param mData
     */
    public static String handleDataMask(byte[] mData) {
        String str = "";
        for (int i = 0; i < mData.length; i++) {
            mData[i] ^= MAGIC_NUMBER;
        }
        try {
            str = new String(mData, 0, mData.length, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 判断字符串是否全为数字并且几位以上
     * 
     * @param str
     *            字符创
     * @param num
     *            包含数字至少几位
     * @return true是，false否
     * 
     */
    public static boolean isAllDigitalByNum(String str, int num) {
        String[] number = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        int sameNum = 0;
        int j, i;

        if (str == null || str.length() == 0) {
            return false;
        }
        for (i = 0; i < str.length(); i++) {
            for (j = 0; j < number.length; j++) {
                if (number[j].equals(str.substring(i, i + 1))) {
                    sameNum++;
                    break;
                }
            }
            if (j >= number.length) {
                return false;
            }
        }
        if (sameNum < num) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 将字符串中的0去除
     * 
     * @param str
     * @return string
     */
    public static String getStringNozero(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '\0') {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 冒泡排序
     * 
     * @param x
     */
    public static void Bubble(int[] x) {
        for (int i = 0; i < x.length; i++) {
            for (int j = i + 1; i < x.length; j++) {
                if (x[i] > x[j]) {
                    int temp = x[i];
                    x[i] = x[j];
                    x[j] = temp;
                }
            }
        }
    }

    /**
     * 查找指定字符位置
     * 
     * @param data
     *            待查找数据
     * @param findchar
     *            待查找字符
     * @param numchar
     *            待查找字符的序号,从0开始
     * @return 返回指定字符出现的位置,如果没有找到,返回数据总长度
     */
    public static int findCharPos(byte[] data, char findchar, int numchar) {
        if (data == null || data.length == 0) {
            return -1;
        }
        int size = data.length;
        int pos = 0;
        for (;;) {
            if (data[pos] == findchar) {
                if (numchar == 0) {
                    break;
                } else {
                    numchar--;
                }
            }
            size--;
            pos++;
            if (size == 0) {
                break;
            }
        }
        return pos;
    }

    /**
     * 查找指定字符位置
     * 
     * @param str
     *            待查找字符串
     * @param findchar
     *            待查找字符
     * @param numchar
     *            待查找字符的序号, 从0开始, 如: numchar=1, 则表示查找字符第2次出现的位置
     * @param maxlen
     *            待查找字符串的最大长度
     * @return 返回指定字符出现的位置, 从0开始, 如果没有找到, 返回字符串的长度
     */
    public static int findCharPos(String str, char findchar, int numchar, int maxlen) {
        int i, pos;
        if (str == null) {
            return 0;
        }
        pos = 0;
        maxlen = (str.length() > maxlen) ? maxlen : str.length();
        for (i = 0; i < maxlen; i++) {
            if (str.charAt(i) == findchar) {
                if (numchar == 0) {
                    break;
                } else {
                    numchar--;
                }
            }
            pos++;
        }
        return pos;
    }

    /**
     * 查找指定字符数量
     * 
     * @param data
     *            : 待查找数据
     * @param findchar
     *            :待查找字符
     * @return 返回指定字符数量
     */
    public static int findCharNum(byte[] data, char findchar) {
        if (data == null || data.length == 0) {
            return 0;
        }
        int size = data.length;
        int pos = 0;
        int numChar = 0;
        for (;;) {
            if (data[pos] == findchar) {
                numChar++;
            }
            size--;
            pos++;
            if (size == 0) {
                break;
            }
        }
        return numChar;
    }

    /**
     * 查找指定字符的数量
     * 
     * @param str
     *            待查找的字符串
     * @param findchar
     *            待查找字符
     * @param maxlen
     *            待查找字符串的最大长度
     * @return 指定字符的数量
     */
    public static int findCharNum(String str, char findchar, int maxlen) {
        int i, numchar = 0;
        if (str == null) {
            return 0;
        }
        maxlen = (str.length() > maxlen) ? maxlen : str.length();
        for (i = 0; i < maxlen; i++) {
            if (str.charAt(i) == findchar) {
                numchar++;
            }
        }

        return numchar;
    }

    /**
     * 将字符串被成六位+加单位为七位 后面加空格
     * 
     * @param str
     * @return 拼接后的字符串
     */
    public static String getSixBytesStr(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() >= 7) {
            return str;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(str);
            for (int i = str.length(); i < 7; i++) {
                sb.append(" ");
            }
            return sb.toString();
        }
    }

    /**
     * 获取数据区带进位累加校验码
     * 
     * @param byteData数据
     * @return 单字节带进位累加校验码
     */
    public static byte getChkSum(byte[] byteData) {
        short result = 0;
        int len = 0;
        if (byteData == null || byteData.length == 0) {
            return 0;
        }
        len = byteData.length;
        for (int i = 0; i < len; i++) {
            result += byteToInt(byteData[i]);
            if ((result & 0xff00) >> 8 != 0) {
                char low = (char) ((char) ((result & 0xff00) >> 8) + (char) (result & 0x00ff));
                result = (short) low;
            }
        }
        return (byte) (result & 0x00ff);
    }

    /**
     * 计算累加和 (使用时确保要计算累加和数据长度是byteData的长度)
     * 
     * @param byteData数据体
     * @return 累加和
     */
    public static int CalCheckSum(byte[] byteData) {
        return CalCheckSum(byteData, byteData.length);
    }

    /**
     * 计算累加和
     * 
     * @param byteData数据体
     * @param len
     *            数据长度
     * @return 累加和
     */
    public static int CalCheckSum(byte[] byteData, int len) {
        int chksum = 0;
        if (byteData == null || byteData.length < len) {
            return 0;
        }
        for (int i = 0; i < len; i++) {
            chksum += byteToInt(byteData[i]);
        }
        return chksum;
    }

    /**
     * 将字节转化为整型
     * 
     * @param a
     * @return 整数
     */
    public static int byteToInt(byte a) {
        return (a & 0xff);
    }

    /**
     * 将字节数组转成整数(大端模式,高字节在低位即byteArray[0])
     * 
     * @param byteArray
     * @return 整数
     */
    public static int byteArrayToInt(byte[] byteData) {
        int result = 0;
        int len = 0;
        if (byteData == null || byteData.length == 0) {
            return 0;
        }
        len = byteData.length;
        for (int i = 0; i < len; i++) {
            result |= (byteData[i] & 0xff) << (len - 1 - i) * 8;
        }
        return result;
    }

    /**
     * 将数组中存的四个数组为整数
     * 
     * @param a
     *            最高位
     * @param b
     * @param c
     * @param d
     *            最低位
     * @return 整数
     */
    public static int byteArraytoInt(byte a, byte b, byte c, byte d) {
        int result = 0;
        int a1 = byteToInt(a);
        int b1 = byteToInt(b);
        int c1 = byteToInt(c);
        int d1 = byteToInt(d);

        result += a1 * 256 * 256 * 256 + b1 * 256 * 256 + c1 * 256 + d1;
        return result;
    }

    /**
     * 整形转数组
     * 
     * @param intValue
     * @return
     */
    public static byte[] intToByteArray(int value) {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++) {
            result[i] = (byte) (value >> 8 * (3 - i) & 0xff);
        }
        return result;
    }

    /**
     * short型转数组
     * 
     * @param value
     * @return
     */
    public static byte[] shortToByteArray(short value) {
        byte[] result = new byte[2];
        for (int i = 0; i < 2; i++) {
            result[i] = (byte) (value >> 8 * (1 - i) & 0xff);
        }
        return result;
    }

    /**
     * 将arraylist转化为数组
     * 
     * @param arraylist
     * @return 数组
     */
    public static int[] getIntegerArraybyArraylist(ArrayList<Integer> arraylist) {
        if (arraylist != null) {
            int[] result = new int[arraylist.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = arraylist.get(i);
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 将arraylist转化为数组
     * 
     * @param arraylist
     * @return 数组
     */
    public static String[] getStringArraybyArraylist(ArrayList<String> arraylist) {
        if (arraylist != null) {
            String[] result = new String[arraylist.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = arraylist.get(i);
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 将数组转化为arraylist
     * 
     * @param arraylist
     * @return 数组
     */
    public static ArrayList<Integer> getArrayListByIntegerArray(int[] src) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (src != null) {
            for (int i = 0; i < src.length; i++) {
                result.add(src[i]);
            }
        }
        return result;
    }

    /**
     * 将字符串转换为ArrayList
     * 
     * @param src
     *            待分隔字符串
     * @param split
     *            分隔字符
     * @return
     */
    public static ArrayList<Integer> getArrayListByString(String src, String split) {
        ArrayList<Integer> idList = new ArrayList<Integer>();

        if (src != null && src.length() > 0) {
            String[] strs = src.split(split);
            for (int i = 0; i < strs.length; i++) {
                idList.add(GpsUtils.strToInt(strs[i]));
            }
        }

        return idList;
    }

    /**
     * 字符串转整形数
     * 
     * @param str
     * @return 整形数,如果字符串为空对象或字符串有效长度为0则返回0
     */
    public static int strToInt(String str) {
        if (str == null) {
            return 0;
        }

        String tmpStr = str.trim();
        if (tmpStr.length() <= 0) {
            return 0;
        }
        return Integer.parseInt(tmpStr);
    }

    /**
     * 根据分隔符拆分输入字符串为字符串数组, 允许两分隔符间无内容
     * 
     * @param src
     *            待分隔字符串
     * @param split
     *            分隔字符
     * @return 分隔后数组, 空数据返回空串
     */
    public static String[] stringToArray(String src, String split) {
        String[] strs;
        if (src == null || src.length() == 0) {
            return null;
        }
        strs = src.split(split);
        return strs;
    }

    /**
     * 根据分隔符拆分输入字符串为整型数组, 允许两分隔符间无内容
     * 
     * @param src
     *            待分隔字符串
     * @param split
     *            分隔字符
     * @return 分隔后数组, 空数据返回空串
     */
    public static int[] stringToArray(String src, char split) {
        int i, len, count, start, end;
        int[] pos;
        char[] chars;
        int[] strs;

        if (src == null || src.length() == 0) {
            return null;
        }

        chars = src.toCharArray();
        len = chars.length;
        count = 0;
        pos = new int[len];
        for (i = 0; i < len; i++) {
            // 获取每个分隔符的位置
            if (chars[i] == split) {
                pos[count++] = i;
            }
        }
        // 子串的个数等于分隔符的个数加1
        strs = new int[count + 1];
        start = 0;
        end = 0;
        for (i = 0; i < count + 1; i++) {
            end = pos[i];
            // 判断是否已到达字符串的最后一个子串, 让终点等于字符串的长度
            if (end == 0) {
                end = len;
            }
            if (end > start) {
                // 获取下一个分割点的起始位置
                strs[i] = Integer.parseInt(src.substring(start, end));
                start = end + 1;
            } else {
                strs[i] = 0;
            }
        }

        return strs;
    }

    /**
     * 将字符串数组根据分隔符整合成一个字符串
     * 
     * @param strArray
     * @param split
     * @return
     */
    public static String arrayToString(String[] strArray, String split) {
        String result = "";
        if (strArray == null) {
            return result;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strArray.length; i++) {
            sb.append(strArray[i]);
            if (i != strArray.length - 1) {
                sb.append(split);
            }
        }
        result = sb.toString();
        return result;
    }

    /**
     * 将字符串列表根据分隔符整合成一个字符串
     * 
     * @param strList
     * @param split
     * @return
     */
    public static String listToString(List<String> strList, String split) {
        if (strList == null || strList.size() < 1) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String item : strList) {
            sb.append(item).append(split);
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 将整型数组根据分隔符整合成一个字符串
     * 
     * @param strArray
     * @param split
     * @return
     */
    public static String arrayToString(int[] intArray, String split) {
        String result = "";
        if (intArray == null) {
            return result;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < intArray.length; i++) {
            sb.append(intArray[i]);
            if (i != intArray.length - 1) {
                sb.append(split);
            }
        }
        result = sb.toString();
        return result;
    }

    /**
     * 将一个整型数组的内容拼接成以分隔字符(如',')隔开的字符串
     * 
     * @param input
     *            整型数组
     * @param split
     *            分隔字符
     * @return 字符串
     */
    public static String arrayToString(JSONArray input, String split) {
        String output = "";
        if (input == null) {
            return output;
        }
        StringBuilder sb = new StringBuilder();

        try {
            for (int i = 0; i < input.length(); i++) {
                sb.append(input.getInt(i));
                sb.append(split);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (input.length() > 0) {
            output = sb.substring(0, sb.length() - 1);
        }
        sb = null;
        return output;
    }

    /**
     * 字符串转浮点型数
     * 
     * @param str
     * @return 浮点型数,如果字符串为空对象或字符串有效长度为0则返回0.0
     */
    public static float strToFloat(String str) {
        float result = (float) 0.0;

        if (str == null) {
            // 空对象直接返回0.0
            return result;
        }

        String tmpStr = str.trim();
        if (tmpStr.length() <= 0) {
            // 空字符串直接返回0.0
            return result;
        }

        result = Float.parseFloat(tmpStr);
        return result;
    }

    /**
     * 把价格字符串转成长整数（小数点后移两位，比如1.11 转成111；1.1转成100；1转成100）
     * 
     * @param priceStr
     * @return 转换后的整数
     */
    public static long priceStrToLong(String priceStr) {
        long result = 0;
        long intNum = 0, decimalsNum = 0;
        String[] strArr;

        if (priceStr == null || priceStr.length() <= 0) {
            return result;
        }

        strArr = priceStr.split("\\.");
        if (strArr != null && strArr.length > 0) {
            intNum = strToInt(strArr[0]) * 100;
            if (strArr.length >= 2 && strArr[1].length() > 0) {
                if (strArr[1].length() > 2) {
                    decimalsNum = strToInt(strArr[1].substring(0, 2));
                } else if (strArr[1].length() == 1) {
                    decimalsNum = strToInt(strArr[1]) * 10;
                } else {
                    decimalsNum = strToInt(strArr[1]);
                }
            }
        } else {
            return result;
        }

        result = intNum + decimalsNum;
        return result;
    }

    /**
     * 把长整数转成有两位小数点的价格字符串（例如1转成0.01；10转成0.10；100转成1.00）
     * 
     * @param priceNum
     * @return
     */
    public static String longToPriceStr(long priceNum) {
        String result = "";
        String priceStr = "";
        if (priceNum < 0) {
            long tmpNum = 0 - priceNum;
            priceStr = String.format("%03d", tmpNum);
        } else {
            priceStr = String.format("%03d", priceNum);
        }

        result = priceStr.substring(0, priceStr.length() - 2) + "."
                + priceStr.substring(priceStr.length() - 2, priceStr.length());

        if (priceNum < 0) {
            result = "-" + result;
        }
        return result;
    }

    /**
     * 计算单个字节的补码，即: 如b>=0，则返回b；反之，则返回b的反码加1
     * 
     * @param b
     *            待计算的单字节数据
     * @return 补码
     */
    public static int calComplement(byte b) {
        return (b >= 0) ? b : (256 + b);
    }

    /**
     * 将二进制转化为16进制字符串
     * 
     * @param b
     *            二进制字节数组
     * @return String
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if ((n + 1) % 4 == 0) {
                hs += " ";
            }
        }
        return hs.toLowerCase();
    }

    /**
     * 十六进制字符串转化为2进制
     * 
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) {
        byte[] ret = new byte[8];
        byte[] tmp = hex.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
     * 
     * @param src0
     *            byte
     * @param src1
     *            byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (b0 ^ b1);
        return ret;
    }

    /**
     * 根据分隔符拆分输入字符串为字符串数组, 允许两分隔符间无内容
     * 
     * @param src
     *            待分隔字符串
     * @param split
     *            分隔字符
     * @return 分隔后数组, 空数据返回空串
     */
    public static String[] yxStringSplit(String src, char split) {
        int i, len, count, start, end;
        String[] strs;

        if (src == null || src.length() == 0) {
            return null;
        }

        len = src.length();
        count = findCharNum(src, split, len) + 1;// 子串的个数等于分隔符的个数加1
        strs = new String[count];
        start = 0;
        end = 0;
        for (i = 0; i < count; i++) {
            end = findCharPos(src, split, i, len);
            strs[i] = src.substring(start, end);
            start = end + 1;
            if (start > len) {
                break;
            }
        }

        return strs;
    }

    /**
     * 判断某字符串是否包含关键字内容, 如果关键字是字母的话, 根据拼音进行模糊匹配
     * 
     * @param key
     *            关键字
     * @param pinyinStrs
     *            字符串的首字母缩写和全拼信息
     * @return true 包含; false 不包含
     */
    public static boolean isContainKey(String key, String[] pinyinStrs) {
        boolean isContain = false;

        if (key == null || pinyinStrs == null || pinyinStrs.length < 1) {
            return isContain;
        }
        if (pinyinStrs[0].contains(key)) {
            return true;
        }

        char c = key.charAt(0);
        if (Character.isLetter(c)) {
            key = key.toUpperCase(Locale.CHINA);
            int keyLen = key.length();
            // 先检查首字母是否匹配
            if (pinyinStrs[0].contains(key)) {
                return true;
            }

            // 然后逐字进行匹配
            for (int i = 1; i < pinyinStrs.length; i++) {
                int wordLen = pinyinStrs[i].length();
                if (wordLen > keyLen) {
                    if (pinyinStrs[i].startsWith(key)) {
                        return true;
                    }
                } else {
                    // 如果关键字长度比单字拼音长, 则从单字匹配的位置开始, 将其之后的内容拼接进行匹配
                    if (key.startsWith(pinyinStrs[i])) {
                        String tmp = "";
                        for (int j = i; j < pinyinStrs.length; j++) {
                            tmp += pinyinStrs[j];
                        }
                        if (tmp.startsWith(key)) {
                            return true;
                        }
                    }
                }
            }
        }

        return isContain;
    }

    /**
     * 测试输入的字符是否为GB码
     * 
     * @param ch
     * @return true: 是GB码; false: 不是GB码
     */
    public static boolean isGBCode(char ch) {
        if (ch > 0x80) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算字符串实际字符长度，汉字算2个字符长度，遇到换行时，整行填充长度为maxNum
     * 
     * @param str
     * @param maxNum
     *            每行最多显示的字符数（GB码算2个字符）
     * @return 需要显示的字符数
     */
    public static int calcTextRealCharNum(String str, int maxNum) {
        int i, codeNum = 0;
        char ch;

        if (str == null || str.length() == 0) {
            return 0;
        }

        for (i = 0; i < str.length(); i++) {
            ch = str.charAt(i);

            if (isGBCode(ch)) {
                codeNum += 2;
            } else if (ch == '\n' || ch == '\r') {
                // 如遇到'\r','\n',"\r\n"时换行处理
                if (ch == '\r' && (str.length() > i + 1 && str.charAt(i + 1) == '\n')) {
                    i++;
                }
                codeNum += maxNum - codeNum % maxNum;
            } else {
                codeNum++;
            }
        }

        return codeNum;
    }

    /**
     * 从字符串中截取相应字符长度的子串（汉字算2个字符长度，遇到换行时，整行填充长度为maxNum）
     * 
     * @param str
     *            完整的字符串
     * @param len
     *            需要截取的子串字符长度（GB码算2个字符）
     * @return 截取的子串
     */
    public static String getRealCharNumStr(String str, int len) {
        int i, codeNum = 0;
        char ch;
        String subStr = null;

        if (str == null || str.length() == 0) {
            return null;
        }

        for (i = 0; i < str.length(); i++) {
            ch = str.charAt(i);

            if (isGBCode(ch)) {
                codeNum += 2;
            } else if (ch == '\n' || ch == '\r') {
                // 如遇到'\r','\n',"\r\n"时换行处理
                if (ch == '\r' && (str.length() > i + 1 && str.charAt(i + 1) == '\n')) {
                    i++;
                }
            } else {
                codeNum++;
            }

            if (codeNum == len || codeNum > len) {
                subStr = str.substring(0, i);
                break;
            }
        }
        if (codeNum < len) {
            subStr = str;
        }

        return subStr;
    }

    /**
     * 检查输入的字符串是否是有效的无符号数, 例如: 1.23
     * 
     * @param src
     * @param count
     * @return 字符串格式是否正确
     */
    public static boolean checkFloatNumber(String src, int count) {
        if (src == null || src.length() == 0) {
            return true;
        }
        int len = src.length();
        int num = findCharNum(src, '.', len);
        if (num > 1) {
            return false;
        } else if (num == 1) {
            // 小数点前后都必须有数字
            int pos = findCharPos(src, '.', 0, len);
            if ((pos == 0) || (pos == len - 1)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 从游标获取数据存至列表数据集中
     * 
     * @param cur
     * @return
     */
    public static ArrayList<ContentValues> getDataFromCur(Cursor cur) {
        ArrayList<ContentValues> cvList = new ArrayList<ContentValues>();

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            ContentValues cv = null;
            do {
                cv = new ContentValues();
                for (int i = 0; i < cur.getColumnCount(); i++) {
                    cv.put(cur.getColumnName(i), cur.getString(i));
                }
                cvList.add(cv);
            } while (cur.moveToNext());

        }
        if (cur != null) {
            cur.close();
        }
        return cvList;
    }

    public static boolean isLeapYear(int year) {
        if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解析时间字符串为标准的日历对象
     * 
     * @param dateStr
     *            时间字符串, 支持的格式为YYYY-MM-DD[ hh:mm][:ss], 方括号内表示可选项
     * @param calendar
     *            格力高历对象, 用于保存解析后的时间信息
     * @return 成功或是失败, 字符串格式不正确时返回失败
     */
    private static boolean parserDateStr(String dateStr, GregorianCalendar calendar) {
        int start = 0, pos = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int strLen = dateStr.length();

        pos = findCharPos(dateStr, '-', 0, strLen);
        int year = strToInt(dateStr.substring(0, pos));

        start = pos + 1;
        if (start >= strLen) {
            return false;
        }
        pos = findCharPos(dateStr, '-', 1, strLen);
        int month = strToInt(dateStr.substring(start, pos));
        // 日历系统的月份是从0开始的
        if (month > 0) {
            month--;
        }

        start = pos + 1;
        if (start >= strLen) {
            return false;
        }
        pos = findCharPos(dateStr, ' ', 0, strLen);
        int day = strToInt(dateStr.substring(start, pos));
        start = pos + 1;

        if (strLen > start) {
            pos = findCharPos(dateStr, ':', 0, strLen);
            hour = strToInt(dateStr.substring(start, pos));
            start = pos + 1;
            if (start >= strLen) {
                return false;
            }

            pos = findCharPos(dateStr, ':', 1, strLen);
            minute = strToInt(dateStr.substring(start, pos));
            start = pos + 1;
            if (start < strLen) {
                pos = findCharPos(dateStr, '.', 0, strLen);
                second = strToInt(dateStr.substring(start, pos));
            }
        }
        calendar.set(year, month, day, hour, minute, second);
        return true;
    }
    
    /**
     * 跳转到设置wifi页面
     */
    public static void gotoSettingWifiActivity(Context context) {
        Intent intent = null;
        // 打开网络连接设置界面, 根据设备的安卓版本号调用对应的设置方法
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName(
                    "com.android.settings",
                    "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px 的单位 转成为 dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    /**
     * 获取屏幕高度
     * 
     * @return
     */
    public static int getScreenHeight(Context context) {
        // 因为手机的横竖屏状态以及导航栏是否显示会影响到显示区域, 因此每次都要重新获取
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕宽度
     * 
     * @return
     */
    public static int getScreenWidth(Context context) {
        // 因为手机的横竖屏状态以及导航栏是否显示会影响到显示区域, 因此每次都要重新获取
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 将GB码转换成拼音首字母字符串, 大写格式
     * 
     * @param str
     *            待转换的GB码
     * @return 拼首字母字符串
     */
    public static String GB2PinyinSzmStr(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            if (j > 10) { // 最大不能超过10个汉字
                break;
            }
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.toUpperCase(Locale.CHINA);
    }

    /**
     * 将人名转换为拼音首字母字符串, 大写格式, 对部分多音字进行特殊处理
     * 
     * @param str
     *            人员姓名字符串
     * @return 拼首字母字符串
     */
    public static String name2PinyinSzmStr(String str) {
        String convert = "";
//        str = str.trim();
//        if (str != null && str.length() > 0) {
//            char firstChar = str.charAt(0);
//            if ("曾".equals(String.valueOf(firstChar))) {
//                str = str.replaceFirst("曾", "Z");
//            } else if ("解".equals(String.valueOf(firstChar))) {
//                str = str.replaceFirst("解", "X");
//            } else if ("单".equals(String.valueOf(firstChar))) {
//                str = str.replaceFirst("单", "S");
//            }
//        }
        // 设置拼音输出格式, 小写, 带音标
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        try {
            for (int j = 0; j < str.length(); j++) {
                char word = str.charAt(j);
                // 提取汉字的拼音字母
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word, format);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    convert += pinyinArray[0];
                } else {
                    convert += word;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return convert; //.toUpperCase(Locale.CHINA);
    }

    /**
     * 输入流转换为字符串
     * 
     * @param inputStream
     *            输入流
     * @param encoding
     *            编码格式
     * @return 转换后的字符串
     * @throws Exception
     */
    public static String inputStreamToString(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }

        inputStream.close();
        return sb.toString();
    }

    /**
     * 输入流转换为二进制数据
     * 
     * @param inputStream
     *            输入流
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] inputStreamToByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 10];
        int len = 0;

        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        inputStream.close();

        return outputStream.toByteArray();
    }
}