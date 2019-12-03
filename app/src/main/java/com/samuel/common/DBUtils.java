/**
 * Copyright (C) 2013 XiaMen Yaxon NetWorks Co.,LTD.
 */
package com.samuel.common;

import java.util.Locale;

import org.litepal.tablemanager.Connector;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

/**
 * ���ݿ����������
 * 
 * @author ���� 2012-02-15 ����<br>
 * 
 */
public class DBUtils {
    private static final String TAG = DBUtils.class.getSimpleName();
    private static DBUtils mInstance = null;
    private SQLiteDatabase mSQLiteDatabase = null;

    /**
     * ���ݿ⹹�� ����
     */
    private DBUtils() {
        super();
        mSQLiteDatabase = Connector.getDatabase();
        // mSQLiteDatabase = Database.getInstance().openSQLiteDatabase();
        try {
            // �������ݿ�ı��ػ�����Ϊ�й�, �Ա��ڶ������ַ�����������
            mSQLiteDatabase.setLocale(Locale.CHINA);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡ���ݿ����ʵ��
     * 
     * @return
     */
    public static DBUtils getInstance() {
        if (mInstance == null) {
            mInstance = new DBUtils();
        }
        return mInstance;
    }

    /**
     * ��ʼ���� ���ڶ������ݲ���ǰ���� Ŀǰ�����ŵꡢ��Ʒ����Э��ʹ��
     */
    public void beginTransaction() {
        mSQLiteDatabase.beginTransaction();
    }

    /**
     * �������� ���ڶ��������������� ���� ��ʱһ���ύ����
     */
    public void endTransaction() {
        try {
            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
    }

    /**
     * ɾ��һ�����ݿ��
     * 
     * @param tableName
     *            : ����
     * @return
     */
    public void deleteTable(String tableName) {
        try {
            mSQLiteDatabase.execSQL("DROP TABLE " + tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ���һ�����ݿ������
     * 
     * @param tableName
     *            ������
     * @return
     */
    public void clearTable(String tableName) {
        try {
            mSQLiteDatabase.execSQL("delete from  " + tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ��������
     * 
     * @param mSQLiteDatabase
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public void updateTable(String table, ContentValues cv, String whereClause, String[] whereArgs) {
        try {
            mSQLiteDatabase.update(table, cv, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����������ֵƥ����±�
     * 
     * @param table
     *            :���ݿ����
     * @param cv
     *            :����
     * @param condition
     *            :����
     * @param value
     *            :������ֵ
     */
    public void updateTable(String table, ContentValues cv, String condition, int value) {
        try {
            mSQLiteDatabase.update(table, cv, condition + "= ?", new String[] { Integer.toString(value) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����������ֵƥ����±�
     * 
     * @param table
     *            :���ݿ����
     * @param cv
     *            :����
     * @param condition
     *            :����
     * @param value
     *            :�ַ���
     */
    public void updateTable(String table, ContentValues cv, String condition, String value) {
        try {
            mSQLiteDatabase.update(table, cv, condition + " = ?", new String[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ɾ������
     * 
     * @param mSQLiteDatabase
     * @param table
     *            ����
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public void delete(String table, String whereClause, String[] whereArgs) {
        try {
            mSQLiteDatabase.delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����������ֵƥ��ɾ������ĳ��
     * 
     * @param table
     *            ����
     * @param condition
     *            ����
     * @param value
     *            ��ֵ
     * @return
     */
    public void DeleteDataByCondition(String table, String condition, Integer value) {
        try {
            mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + "="
                    + Integer.toString(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �����ַ���ƥ��ɾ��������
     * 
     * @param mSQLiteDatabase
     *            ���ݿ����
     * @param table
     *            ����
     * @param condition
     *            ����
     * @param value
     *            �ַ���
     * @return
     */
    public void DeleteDataByStr(String table, String condition,
            String value) {
        try {
            mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = ?",
                    new String[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ��������һ������
     * 
     * @param mSQLiteDatabase
     *            ���ݿ����
     * @param cv
     *            �������ݼ���
     * @param table
     *            ����
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long AddData(ContentValues cv, String table) {
        if (mSQLiteDatabase == null) {
            return -1;
        }
        long result = mSQLiteDatabase.insert(table, null, cv);
        return result;
    }

    /**
     * ִ��sql��ѯ����ȡ��Ӧ�α�
     * 
     * @param sql
     * @param selectionArgs
     */
    public Cursor QueryBySQL(String sql, String[] selectionArgs) {
        try {
            return mSQLiteDatabase.rawQuery(sql, selectionArgs);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ��ȡ����������
     * 
     * @param table
     */
    public long getTableCount(String table) {
        long count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM " + table;
            SQLiteStatement statement = mSQLiteDatabase.compileStatement(sql);
            count = statement.simpleQueryForLong();
        } catch (Exception e) {

        }
        return count;
    }

    /**
     * ����ID ����ĳ����ļ�¼�Ƿ����
     * 
     * @param sqlite
     *            ���ݿ����
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
     */
    public boolean isExistbyCondition(String tablename,
            String whereClause, String[] whereArgs) {
        Cursor cur = null;
        try {

            cur = mSQLiteDatabase.query(true, tablename, null, whereClause,
                    whereArgs, null, null, null, null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * ����ID ����ĳ����ļ�¼�Ƿ����
     * 
     * @param sqlite
     *            ���ݿ����
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
     */
    public boolean isExistbyId(String tablename,
            String columnname, int id) {
        Cursor cur = null;
        try {

            cur = mSQLiteDatabase.query(true, tablename, null, columnname + "=?",
                    new String[] { Integer.toString(id) }, null, null, null, null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * ����ID ����ĳ����ļ�¼�Ƿ����
     * 
     * @param sqlite
     *            ���ݿ����
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
     */
    public boolean isExistby2Id(String tablename,
            String columnname, int id, String columnname2, int id2) {
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null, columnname + "= ?"
                    + " and " + columnname2 + "= ?",
                    new String[] { Integer.toString(id), Integer.toString(id2) }, null, null, null, null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * ����str ����ĳ����ļ�¼�Ƿ����
     * 
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
     */
    public boolean isExistByStr(String tablename, String columnname, String str) {
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(tablename,
                    null, columnname + " = ?",
                    new String[] { str }, null, null, null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * ����ID���ַ��� ����ĳ����ļ�¼�Ƿ����
     * 
     * @param sqlite
     *            ���ݿ����
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
     */
    public boolean isExistbyIdAndStr(String tablename,
            String columnname, int id,
            String columnname2, String str2) {
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null, columnname + "= ?"
                    + " and " + columnname2
                    + " = ?", new String[] { Integer.toString(id), str2 }, null, null, null,
                    null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * �����������������ж����ݼ�¼�Ƿ����
     * 
     * @param tableName
     *            ���ݿ�ı���
     * @param params
     *            ����������, �����������к�����ֵ����ĸ�ʽ
     * @return true: ���ݼ�¼�Ѵ���
     */
    public boolean isExist(String tableName, Object... params) {
        boolean result = false;
        if (TextUtils.isEmpty(tableName)) {
            return result;
        }

        String selection = null;
        String[] args = null;
        if (params != null && params.length % 2 == 0) {
            int count = params.length / 2;
            int index = 0;
            selection = "";
            args = new String[count];
            for (int i = 0; i < params.length; i++) {
                // �ж����������ֶ�, �������ַ�������
                if (params[i] instanceof String) {
                    selection += params[i] + "=?";
                    if (index < count - 1) {
                        selection += " and ";
                    }
                } else {
                    Log.e(TAG, "Error selection type: " + params[i].toString());
                    throw new RuntimeException("Error selection type");
                }

                // �ж�������ֵ�ֶ�
                i++;
                args[index++] = String.valueOf(params[i]);
            }
        }
        try {
            Cursor cur = mSQLiteDatabase.query(true, tableName, null, selection, args, null, null, null, null);
            if (cur != null) {
                if (cur.getCount() > 0) {
                    result = true;
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * �����������������������ݿ��
     * 
     * @param tableName
     *            ���ݿ�ı���
     * @param cv
     *            ���µ���������
     * @param params
     *            ����������, �����������к�����ֵ����ĸ�ʽ
     * @return ��������
     */
    public int update(String tableName, ContentValues cv, Object... params) {
        if (TextUtils.isEmpty(tableName) || cv == null) {
            return 0;
        }

        String selection = null;
        String[] args = null;
        if (params != null && params.length % 2 == 0) {
            int count = params.length / 2;
            int index = 0;
            selection = "";
            args = new String[count];
            for (int i = 0; i < params.length; i++) {
                // �ж����������ֶ�, �������ַ�������
                if (params[i] instanceof String) {
                    selection += params[i] + "=?";
                    if (index < count - 1) {
                        selection += " and ";
                    }
                } else {
                    Log.e(TAG, "Error selection type: " + params[i].toString());
                    throw new RuntimeException("Error selection type");
                }

                // �ж�������ֵ�ֶ�
                i++;
                args[index++] = String.valueOf(params[i]);
            }
        }
        return mSQLiteDatabase.update(tableName, cv, selection, args);
    }
    
    public Cursor query(String tableName, Object... params) {
        if (TextUtils.isEmpty(tableName)) {
            return null;
        }

        String selection = null;
        String[] args = null;
        if (params != null && params.length % 2 == 0) {
            int count = params.length / 2;
            int index = 0;
            selection = "";
            args = new String[count];
            for (int i = 0; i < params.length; i++) {
                // �ж����������ֶ�, �������ַ�������
                if (params[i] instanceof String) {
                    selection += params[i] + "=?";
                    if (index < count - 1) {
                        selection += " and ";
                    }
                } else {
                    Log.e(TAG, "Error selection type: " + params[i].toString());
                    throw new RuntimeException("Error selection type");
                }

                // �ж�������ֵ�ֶ�
                i++;
                args[index++] = String.valueOf(params[i]);
            }
        }
        try {
            return mSQLiteDatabase.query(true, tableName, null, selection, args, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ���ݿ��Ƿ��
     * 
     * @return
     */
    public boolean isOpen() {
        return mSQLiteDatabase.isOpen();
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mSQLiteDatabase;
    }

}
