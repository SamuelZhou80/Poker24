/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.samuel.twentyfour;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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

/**
 * ���ߺ�����
 * 
 * @author ���� 2012-01-08 ����<br>
 * 
 */
public class GpsUtils {

    static final int MAGIC_NUMBER = 0x55;
    static final int MONTH_DAY[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    /**
     * ��1/1000��ת����1/1024��λ������
     * 
     * @param value
     * @return
     */
    public static int get1024Changed(int value) {
        return (int) (value * ((float) 1024 / 1000));
    }

    /**
     * ��1/1024��ת����1/1000��λ������
     * 
     * @param value
     * @return
     */
    public static int get1000Changed(int value) {
        return (int) (value * ((float) 1000 / 1024));
    }

    /**
     * ��ȡָ���·ݵ�������
     * 
     * @param year
     *            ָ�������
     * @param month
     *            ָ�����·�
     * 
     * @return ������
     */
    public static int getDay(int year, int month) {
        if (month > 12 || month == 0) {
            return 30;
        }
        if (month == 2) {
            // �ж��Ƿ�Ϊ����
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
     * ��ȡ��2000�굽dateһ���ж�����
     * 
     * @param date
     *            �����������־����
     * @return �ܹ�����
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
     * ��ȡ��������ʱ��Ĳ�ֵ(YYYY-MM-dd HH:mm:ss��ʽ����)
     * 
     * @return ��λs
     */
    public static long getDateTimeDiffer(String startTime, String endTime) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin;
        Date end;
        long between = 0;
        try {
            begin = dfs.parse(startTime);
            end = dfs.parse(endTime);
            between = (end.getTime() - begin.getTime()) / 1000;// ����1000��Ϊ��ת������
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Math.abs(between);
    }

    /** �ж�date1�Ƿ���date2��date3���·��� */
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
     * ��ȡĳһ�����ڵ������ղ����浽������
     * 
     * @return ������������Ϣ������
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
     * ��ȡ�����ֽ�����
     * 
     * @param date
     * @return
     */
    public static int[] getDateBytes(String date) {
        return getCurDateBytes(date);
    }

    /**
     * ��ȡʱ���ֽ�����
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
     * ��ȡ����ʱ���ֽ�����
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
     * ��ȡ��ǰ���� ����
     * 
     * @param weekday
     *            ��ǰ��������, ����=1, ��һ=2, �ܶ�=3, �Դ�����
     * 
     * @return string ����һ�����ڶ���
     */
    public static String getWeekdayChineseName(int weekday) {
        String result = null;
        switch (weekday) {
        case 1:
            result = "������";
            break;
        case 2:
            result = "����һ";
            break;
        case 3:
            result = "���ڶ�";
            break;
        case 4:
            result = "������";
            break;
        case 5:
            result = "������";
            break;
        case 6:
            result = "������";
            break;
        case 7:
            result = "������";
            break;
        default:
            break;
        }
        return result;
    }

    /**
     * �жϵ�ǰ���������ڼ�
     * 
     * @param pTime
     *            ���õ���Ҫ�жϵ�ʱ�� //��ʽ��2012-09-08
     * @return dayForWeek �жϽ��
     * @Exception �����쳣
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
     * ��ȡָ������ ��ǰ���� ����
     * 
     * @return string ����һ�����ڶ���
     */
    public static String getWeekdayChineseName(String date) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (parserDateStr(date, calendar)) {
            return getWeekdayChineseName(calendar.get(Calendar.DAY_OF_WEEK));
        }
        return null;
    }

    /**
     * ��ȡ���뼶ʱ�� ת��Ϊʮ�������ַ��� ���ϳ�ʮ��λ ͨ���������ձ������ɵ�ID
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
     * ��ȡtime���ڽ�����days��������,������ʽ����
     * 
     * @return 0:year,1:month,2:day
     */
    public static int[] getNextDate(String time, int days) {
        String nextDate = getNextDateString(time, days);
        return getCurDateBytes(nextDate);
    }

    /**
     * ��ȡtime���ڽ�����days��������,�ַ�����ʽ����
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
     * ��ʼ�����Ƿ����ڻ���ڽ�������
     * 
     * @return true:�� false: ��
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
     * ��ʼʱ���Ƿ����ڻ���ڽ���ʱ��
     * 
     * @return true:�� false: ��
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
     * ��ʼ�����Ƿ����ڻ���ڽ�������
     * 
     * @return true:�� false: ��
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
     * ����ʱ�� �Ƿ���ڵ��� ��ʼʱ��+����
     * 
     * @return true:�� false: ��
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
     * ��CELL ID����ת��
     * 
     * @param [in] id: ��ת����CELL ID
     * @return ת�����CELL ID
     */
    public static int changeCellId(int id) {
        short tmp1, tmp2, tmp3;

        // ��5λ
        tmp1 = (short) (id & 0x001f);

        // ��5λ
        tmp2 = (short) (id & 0xf800);

        // ��6λ
        tmp3 = (short) (id & 0x07e0);

        tmp3 = (short) ~tmp3;
        tmp3 &= ~0xf81f;

        id = (tmp1 << 11) | tmp3 | (tmp2 >> 11);
        return id;
    }

    /**
     * �Զ�д���ݽ������봦��
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
     * �ж��ַ����Ƿ�ȫΪ���ֲ��Ҽ�λ����
     * 
     * @param str
     *            �ַ���
     * @param num
     *            �����������ټ�λ
     * @return true�ǣ�false��
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
     * ���ַ����е�0ȥ��
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
     * ð������
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
     * ����ָ���ַ�λ��
     * 
     * @param data
     *            ����������
     * @param findchar
     *            �������ַ�
     * @param numchar
     *            �������ַ������,��0��ʼ
     * @return ����ָ���ַ����ֵ�λ��,���û���ҵ�,���������ܳ���
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
     * ����ָ���ַ�λ��
     * 
     * @param str
     *            �������ַ���
     * @param findchar
     *            �������ַ�
     * @param numchar
     *            �������ַ������, ��0��ʼ, ��: numchar=1, ���ʾ�����ַ���2�γ��ֵ�λ��
     * @param maxlen
     *            �������ַ�������󳤶�
     * @return ����ָ���ַ����ֵ�λ��, ��0��ʼ, ���û���ҵ�, �����ַ����ĳ���
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
     * ����ָ���ַ�����
     * 
     * @param data
     *            : ����������
     * @param findchar
     *            :�������ַ�
     * @return ����ָ���ַ�����
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
     * ����ָ���ַ�������
     * 
     * @param str
     *            �����ҵ��ַ���
     * @param findchar
     *            �������ַ�
     * @param maxlen
     *            �������ַ�������󳤶�
     * @return ָ���ַ�������
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
     * ���ַ���������λ+�ӵ�λΪ��λ ����ӿո�
     * 
     * @param str
     * @return ƴ�Ӻ���ַ���
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
     * ��ȡ����������λ�ۼ�У����
     * 
     * @param byteData����
     * @return ���ֽڴ���λ�ۼ�У����
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
     * �����ۼӺ� (ʹ��ʱȷ��Ҫ�����ۼӺ����ݳ�����byteData�ĳ���)
     * 
     * @param byteData������
     * @return �ۼӺ�
     */
    public static int CalCheckSum(byte[] byteData) {
        return CalCheckSum(byteData, byteData.length);
    }

    /**
     * �����ۼӺ�
     * 
     * @param byteData������
     * @param len
     *            ���ݳ���
     * @return �ۼӺ�
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
     * ���ֽ�ת��Ϊ����
     * 
     * @param a
     * @return ����
     */
    public static int byteToInt(byte a) {
        return (a & 0xff);
    }

    /**
     * ���ֽ�����ת������(���ģʽ,���ֽ��ڵ�λ��byteArray[0])
     * 
     * @param byteArray
     * @return ����
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
     * �������д���ĸ�����Ϊ����
     * 
     * @param a
     *            ���λ
     * @param b
     * @param c
     * @param d
     *            ���λ
     * @return ����
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
     * ����ת����
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
     * short��ת����
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
     * ��arraylistת��Ϊ����
     * 
     * @param arraylist
     * @return ����
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
     * ��arraylistת��Ϊ����
     * 
     * @param arraylist
     * @return ����
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
     * ������ת��Ϊarraylist
     * 
     * @param arraylist
     * @return ����
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
     * ���ַ���ת��ΪArrayList
     * 
     * @param src
     *            ���ָ��ַ���
     * @param split
     *            �ָ��ַ�
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
     * �ַ���ת������
     * 
     * @param str
     * @return ������,����ַ���Ϊ�ն�����ַ�����Ч����Ϊ0�򷵻�0
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
     * ���ݷָ�����������ַ���Ϊ�ַ�������, �������ָ�����������
     * 
     * @param src
     *            ���ָ��ַ���
     * @param split
     *            �ָ��ַ�
     * @return �ָ�������, �����ݷ��ؿմ�
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
     * ���ݷָ�����������ַ���Ϊ��������, �������ָ�����������
     * 
     * @param src
     *            ���ָ��ַ���
     * @param split
     *            �ָ��ַ�
     * @return �ָ�������, �����ݷ��ؿմ�
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
            // ��ȡÿ���ָ�����λ��
            if (chars[i] == split) {
                pos[count++] = i;
            }
        }
        // �Ӵ��ĸ������ڷָ����ĸ�����1
        strs = new int[count + 1];
        start = 0;
        end = 0;
        for (i = 0; i < count + 1; i++) {
            end = pos[i];
            // �ж��Ƿ��ѵ����ַ��������һ���Ӵ�, ���յ�����ַ����ĳ���
            if (end == 0) {
                end = len;
            }
            if (end > start) {
                // ��ȡ��һ���ָ�����ʼλ��
                strs[i] = Integer.parseInt(src.substring(start, end));
                start = end + 1;
            } else {
                strs[i] = 0;
            }
        }

        return strs;
    }

    /**
     * ���ַ���������ݷָ������ϳ�һ���ַ���
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
     * ���ַ����б���ݷָ������ϳ�һ���ַ���
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
     * ������������ݷָ������ϳ�һ���ַ���
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
     * ��һ���������������ƴ�ӳ��Էָ��ַ�(��',')�������ַ���
     * 
     * @param input
     *            ��������
     * @param split
     *            �ָ��ַ�
     * @return �ַ���
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
     * �ַ���ת��������
     * 
     * @param str
     * @return ��������,����ַ���Ϊ�ն�����ַ�����Ч����Ϊ0�򷵻�0.0
     */
    public static float strToFloat(String str) {
        float result = (float) 0.0;

        if (str == null) {
            // �ն���ֱ�ӷ���0.0
            return result;
        }

        String tmpStr = str.trim();
        if (tmpStr.length() <= 0) {
            // ���ַ���ֱ�ӷ���0.0
            return result;
        }

        result = Float.parseFloat(tmpStr);
        return result;
    }

    /**
     * �Ѽ۸��ַ���ת�ɳ�������С���������λ������1.11 ת��111��1.1ת��100��1ת��100��
     * 
     * @param priceStr
     * @return ת���������
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
     * �ѳ�����ת������λС����ļ۸��ַ���������1ת��0.01��10ת��0.10��100ת��1.00��
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
     * ���㵥���ֽڵĲ��룬��: ��b>=0���򷵻�b����֮���򷵻�b�ķ����1
     * 
     * @param b
     *            ������ĵ��ֽ�����
     * @return ����
     */
    public static int calComplement(byte b) {
        return (b >= 0) ? b : (256 + b);
    }

    /**
     * ��������ת��Ϊ16�����ַ���
     * 
     * @param b
     *            �������ֽ�����
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
     * ʮ�������ַ���ת��Ϊ2����
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
     * ������ASCII�ַ��ϳ�һ���ֽڣ� �磺"EF"--> 0xEF
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
     * ���ݷָ�����������ַ���Ϊ�ַ�������, �������ָ�����������
     * 
     * @param src
     *            ���ָ��ַ���
     * @param split
     *            �ָ��ַ�
     * @return �ָ�������, �����ݷ��ؿմ�
     */
    public static String[] yxStringSplit(String src, char split) {
        int i, len, count, start, end;
        String[] strs;

        if (src == null || src.length() == 0) {
            return null;
        }

        len = src.length();
        count = findCharNum(src, split, len) + 1;// �Ӵ��ĸ������ڷָ����ĸ�����1
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
     * �ж�ĳ�ַ����Ƿ�����ؼ�������, ����ؼ�������ĸ�Ļ�, ����ƴ������ģ��ƥ��
     * 
     * @param key
     *            �ؼ���
     * @param pinyinStrs
     *            �ַ���������ĸ��д��ȫƴ��Ϣ
     * @return true ����; false ������
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
            // �ȼ������ĸ�Ƿ�ƥ��
            if (pinyinStrs[0].contains(key)) {
                return true;
            }

            // Ȼ�����ֽ���ƥ��
            for (int i = 1; i < pinyinStrs.length; i++) {
                int wordLen = pinyinStrs[i].length();
                if (wordLen > keyLen) {
                    if (pinyinStrs[i].startsWith(key)) {
                        return true;
                    }
                } else {
                    // ����ؼ��ֳ��ȱȵ���ƴ����, ��ӵ���ƥ���λ�ÿ�ʼ, ����֮�������ƴ�ӽ���ƥ��
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
     * ����������ַ��Ƿ�ΪGB��
     * 
     * @param ch
     * @return true: ��GB��; false: ����GB��
     */
    public static boolean isGBCode(char ch) {
        if (ch > 0x80) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �����ַ���ʵ���ַ����ȣ�������2���ַ����ȣ���������ʱ��������䳤��ΪmaxNum
     * 
     * @param str
     * @param maxNum
     *            ÿ�������ʾ���ַ�����GB����2���ַ���
     * @return ��Ҫ��ʾ���ַ���
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
                // ������'\r','\n',"\r\n"ʱ���д���
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
     * ���ַ����н�ȡ��Ӧ�ַ����ȵ��Ӵ���������2���ַ����ȣ���������ʱ��������䳤��ΪmaxNum��
     * 
     * @param str
     *            �������ַ���
     * @param len
     *            ��Ҫ��ȡ���Ӵ��ַ����ȣ�GB����2���ַ���
     * @return ��ȡ���Ӵ�
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
                // ������'\r','\n',"\r\n"ʱ���д���
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
     * ���������ַ����Ƿ�����Ч���޷�����, ����: 1.23
     * 
     * @param src
     * @param count
     * @return �ַ�����ʽ�Ƿ���ȷ
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
            // С����ǰ�󶼱���������
            int pos = findCharPos(src, '.', 0, len);
            if ((pos == 0) || (pos == len - 1)) {
                return false;
            }
        }

        return true;
    }

    /**
     * ���α��ȡ���ݴ����б����ݼ���
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
     * ����ʱ���ַ���Ϊ��׼����������
     * 
     * @param dateStr
     *            ʱ���ַ���, ֧�ֵĸ�ʽΪYYYY-MM-DD[ hh:mm][:ss], �������ڱ�ʾ��ѡ��
     * @param calendar
     *            ������������, ���ڱ���������ʱ����Ϣ
     * @return �ɹ�����ʧ��, �ַ�����ʽ����ȷʱ����ʧ��
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
        // ����ϵͳ���·��Ǵ�0��ʼ��
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
     * ��ת������wifiҳ��
     */
    public static void gotoSettingWifiActivity(Context context) {
        Intent intent = null;
        // �������������ý���, �����豸�İ�׿�汾�ŵ��ö�Ӧ�����÷���
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
}