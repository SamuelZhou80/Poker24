package com.samuel.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.samuel.common.DBUtils;

import java.util.ArrayList;

public class LoanDB {

    private static LoanDB mInstance;
    // ====基础数据表====//
    private final static String TABLE_LOAN = "LoanInfo";

    public static LoanDB getInstance() {
        if (mInstance == null) {
            mInstance = new LoanDB();
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
    public interface AckLoanColumns extends BaseColumns {
        String LOAN_ID = "loanid";
        String START_DATE = "startdate";
        String YEAR = "years";
        String TOTAL_MONEY = "amount";
        String RATE = "rate";
    }

    /**
     * 获取已保存的贷款列表
     *
     * @return 贷款列表, 若无找到则返回空数组
     */
    public ArrayList<LoanInfo> getLoanList() {
        Cursor cur;
        ArrayList<LoanInfo> list = new ArrayList<LoanInfo>();

        cur = DBUtils.getInstance().query(TABLE_LOAN);
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                LoanInfo formItem = new LoanInfo();
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
     * 保存贷款详情信息
     */
    public void saveLoanInfo(LoanInfo form) {
        ContentValues cv = new ContentValues();
        cv.put(AckLoanColumns.LOAN_ID, form.getLoanId());
        cv.put(AckLoanColumns.RATE, form.getRate());
        cv.put(AckLoanColumns.START_DATE, form.getStartDate());
        cv.put(AckLoanColumns.TOTAL_MONEY, form.getAmount());
        cv.put(AckLoanColumns.YEAR, form.getYears());

        if (DBUtils.getInstance().isExist(TABLE_LOAN,
                AckLoanColumns.LOAN_ID, form.getLoanId())) {
            DBUtils.getInstance().update(TABLE_LOAN, cv,
                    AckLoanColumns.LOAN_ID, form.getLoanId());
        } else {
            DBUtils.getInstance().AddData(cv, TABLE_LOAN);
        }
    }

    /**
     * 将数据库查找到的记录写入贷款详情对象中
     *
     * @param cur 数据库游标
     * @param formItem 数据对象
     */
    private void setItemFromCur(Cursor cur, LoanInfo formItem) {
        if (cur == null || formItem == null) {
            return;
        }
        formItem.setAmount(cur.getInt(cur.getColumnIndex(AckLoanColumns.TOTAL_MONEY)));
        formItem.setRate(cur.getDouble(cur.getColumnIndex(AckLoanColumns.RATE)));
        formItem.setStartDate(cur.getString(cur.getColumnIndex(AckLoanColumns.START_DATE)));
        formItem.setYears(cur.getInt(cur.getColumnIndex(AckLoanColumns.YEAR)));
        formItem.setLoanId(cur.getString(cur.getColumnIndex(AckLoanColumns.LOAN_ID)));
    }
}
