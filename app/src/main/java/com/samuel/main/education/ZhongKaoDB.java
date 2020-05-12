package com.samuel.main.education;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.samuel.common.DBUtils;

import java.util.ArrayList;

public class ZhongKaoDB {

    private static ZhongKaoDB mInstance;
    // ====基础数据表====//
    public final static String TABLE_ZHONGKAO = "ZhongKao";

    public static ZhongKaoDB getInstance() {
        if (mInstance == null) {
            mInstance = new ZhongKaoDB();
        }
        return mInstance;
    }

    /**
     * 清除单例对象
     */
    public void clearInstance() {
        if (mInstance != null) {
            mInstance = null;
        }
    }

    /**
     * 列名
     */
    public interface AckZhongkaoColumns extends BaseColumns {
        final static String SCHOOLNAME = "name";
        final static String YEAR = "year";
        final static String TOTAL_NUM = "totalnum";
        final static String YIZHONG_BENBU = "yizhongbenbu";
        final static String SHUAGNSHI_BENBU = "shuangshibenbu";
        final static String WAIGUOYU = "waiguoyu";
        final static String TOP = "top3";
        final static String YIZHONG_HAICANG = "yizhonghaicang";
        final static String TO_HIGHSCHOOL = "toHighSchool";
    }

    /**
     * 根据学校名称和年份获取中考数据
     * 
     * @return 中考数据对象，若无找到则返回null
     */
    public ZhongKao getShopDetailInfo(String schoolName, int year) {
        Cursor cur = null;
        ZhongKao formItem = new ZhongKao();

        cur = DBUtils.getInstance().query(TABLE_ZHONGKAO,
                AckZhongkaoColumns.SCHOOLNAME, schoolName, AckZhongkaoColumns.YEAR, year);
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            setItemFromCur(cur, formItem);
        }
        if (cur != null) {
            cur.close();
        }

        return formItem;
    }

    /**
     * 根据学校名称和年份获取中考数据
     * 
     * @return 中考数据对象，若无找到则返回null
     */
    public ArrayList<ZhongKao> getShopDetailInfo(String schoolName) {
        Cursor cur = null;
        ArrayList<ZhongKao> list = new ArrayList<ZhongKao>();

        cur = DBUtils.getInstance().query(TABLE_ZHONGKAO, AckZhongkaoColumns.SCHOOLNAME, schoolName);
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ZhongKao formItem = new ZhongKao();
                setItemFromCur(cur, formItem);
                list.add(formItem);
            } while (cur.moveToNext());
        }
        if (cur != null) {
            cur.close();
        }

        return list;
    }

    /**
     * 保存门店详情信息
     */
    public void saveZhongkaoInfo(ZhongKao form) {
        ContentValues cv = new ContentValues();
        cv.put(AckZhongkaoColumns.SCHOOLNAME, form.getSchoolName());
        cv.put(AckZhongkaoColumns.SHUAGNSHI_BENBU, form.getShuangshibenbu());
        cv.put(AckZhongkaoColumns.TO_HIGHSCHOOL, form.getToHighSchool());
        cv.put(AckZhongkaoColumns.TOP, form.getTop3());
        cv.put(AckZhongkaoColumns.TOTAL_NUM, form.getTotalNum());
        cv.put(AckZhongkaoColumns.WAIGUOYU, form.getWaiguoyu());
        cv.put(AckZhongkaoColumns.YEAR, form.getYear());
        cv.put(AckZhongkaoColumns.YIZHONG_BENBU, form.getYizhongbenbu());
        cv.put(AckZhongkaoColumns.YIZHONG_HAICANG, form.getYizhonghaicang());
        if (DBUtils.getInstance().isExist(TABLE_ZHONGKAO,
                AckZhongkaoColumns.SCHOOLNAME, form.getSchoolName(),
                AckZhongkaoColumns.YEAR, form.getYear())) {
            DBUtils.getInstance().update(TABLE_ZHONGKAO, cv,
                    AckZhongkaoColumns.SCHOOLNAME, form.getSchoolName(),
                    AckZhongkaoColumns.YEAR, form.getYear());
        } else {
            DBUtils.getInstance().AddData(cv, TABLE_ZHONGKAO);
        }
    }

    /**
     * 将数据库查找到的记录写入门店详情对象中
     * 
     * @param cur
     * @param formItem
     */
    private void setItemFromCur(Cursor cur, ZhongKao formItem) {
        if (cur == null || formItem == null) {
            return;
        }
        formItem.setSchoolName(cur.getString(cur.getColumnIndex(AckZhongkaoColumns.SCHOOLNAME)));
        formItem.setShuangshibenbu(cur.getInt(cur.getColumnIndex(AckZhongkaoColumns.SHUAGNSHI_BENBU)));
        formItem.setToHighSchool(cur.getInt(cur.getColumnIndex(AckZhongkaoColumns.TO_HIGHSCHOOL)));
        formItem.setTop3(cur.getInt(cur.getColumnIndex(AckZhongkaoColumns.TOP)));
        formItem.setTotalNum(cur.getInt(cur.getColumnIndex(AckZhongkaoColumns.TOTAL_NUM)));
        formItem.setWaiguoyu(cur.getInt(cur.getColumnIndex(AckZhongkaoColumns.WAIGUOYU)));
        formItem.setYear(cur.getInt(cur.getColumnIndex(AckZhongkaoColumns.YEAR)));
        formItem.setYizhongbenbu(cur.getInt(cur.getColumnIndex(AckZhongkaoColumns.YIZHONG_BENBU)));
        formItem.setYizhonghaicang(cur.getInt(cur.getColumnIndex(AckZhongkaoColumns.YIZHONG_HAICANG)));
    }
}
