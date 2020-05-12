package com.samuel.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import org.litepal.tablemanager.Connector;

import java.util.Locale;

/**
 * 数据库操作工具类
 * 
 * @author 解玉芳 2012-02-15 创建<br>
 * 
 */
public class DBUtils {
    private static final String TAG = DBUtils.class.getSimpleName();
    private static DBUtils mInstance = null;
    private SQLiteDatabase mSQLiteDatabase = null;

    /**
     * 数据库构造 行数
     */
    private DBUtils() {
        super();
        mSQLiteDatabase = Connector.getDatabase();
        // mSQLiteDatabase = Database.getInstance().openSQLiteDatabase();
        try {
            // 设置数据库的本地化区域为中国, 以便于对中文字符进行排序处理
            mSQLiteDatabase.setLocale(Locale.CHINA);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库操作实例
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
     * 开始事务 用于多条数据插入前调用 目前区域、门店、商品三条协议使用
     */
    public void beginTransaction() {
        mSQLiteDatabase.beginTransaction();
    }

    /**
     * 结束事务 用于多条插入语句结束后 调用 此时一起提交数据
     */
    public void endTransaction() {
        try {
            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
    }

    /**
     * 删除一个数据库表
     * 
     * @param tableName
     *            : 表名
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
     * 清空一个数据库表内容
     * 
     * @param tableName
     *            ：表名
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
     * 更新数据
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
     * 根据整型数值匹配更新表
     * 
     * @param table
     *            :数据库表名
     * @param cv
     *            :内容
     * @param condition
     *            :条件
     * @param value
     *            :整型数值
     */
    public void updateTable(String table, ContentValues cv, String condition, int value) {
        try {
            mSQLiteDatabase.update(table, cv, condition + "= ?", new String[] { Integer.toString(value) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据整型数值匹配更新表
     * 
     * @param table
     *            :数据库表名
     * @param cv
     *            :内容
     * @param condition
     *            :条件
     * @param value
     *            :字符串
     */
    public void updateTable(String table, ContentValues cv, String condition, String value) {
        try {
            mSQLiteDatabase.update(table, cv, condition + " = ?", new String[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据
     * 
     * @param mSQLiteDatabase
     * @param table
     *            表名
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
     * 根据整型数值匹配删除表中某项
     * 
     * @param table
     *            表名
     * @param condition
     *            列名
     * @param value
     *            数值
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
     * 根据字符串匹配删除表中项
     * 
     * @param mSQLiteDatabase
     *            数据库对象
     * @param table
     *            表名
     * @param condition
     *            列名
     * @param value
     *            字符串
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
     * 向表中添加一条数据
     * 
     * @param mSQLiteDatabase
     *            数据库对象
     * @param cv
     *            数据内容集合
     * @param table
     *            表名
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
     * 执行sql查询语句获取对应游标
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
     * 获取表数据条数
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
     * 根据ID 查找某个表的记录是否存在
     * 
     * @param sqlite
     *            数据库对象
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
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
     * 根据ID 查找某个表的记录是否存在
     * 
     * @param sqlite
     *            数据库对象
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
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
     * 根据ID 查找某个表的记录是否存在
     * 
     * @param sqlite
     *            数据库对象
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
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
     * 根据str 查找某个表的记录是否存在
     * 
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
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
     * 根据ID和字符串 查找某个表的记录是否存在
     * 
     * @param sqlite
     *            数据库对象
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
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
     * 根据输入条件参数判断数据记录是否存在
     * 
     * @param tableName
     *            数据库的表名
     * @param params
     *            条件参数集, 必须是条件列和条件值交替的格式
     * @return true: 数据记录已存在
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
                // 判断条件的列字段, 必须是字符串类型
                if (params[i] instanceof String) {
                    selection += params[i] + "=?";
                    if (index < count - 1) {
                        selection += " and ";
                    }
                } else {
                    Log.e(TAG, "Error selection type: " + params[i].toString());
                    throw new RuntimeException("Error selection type");
                }

                // 判断条件的值字段
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
     * 根据输入条件参数更新数据库表
     * 
     * @param tableName
     *            数据库的表名
     * @param cv
     *            更新的数据内容
     * @param params
     *            条件参数集, 必须是条件列和条件值交替的格式
     * @return 更新条数
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
                // 判断条件的列字段, 必须是字符串类型
                if (params[i] instanceof String) {
                    selection += params[i] + "=?";
                    if (index < count - 1) {
                        selection += " and ";
                    }
                } else {
                    Log.e(TAG, "Error selection type: " + params[i].toString());
                    throw new RuntimeException("Error selection type");
                }

                // 判断条件的值字段
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
                // 判断条件的列字段, 必须是字符串类型
                if (params[i] instanceof String) {
                    selection += params[i] + "=?";
                    if (index < count - 1) {
                        selection += " and ";
                    }
                } else {
                    Log.e(TAG, "Error selection type: " + params[i].toString());
                    throw new RuntimeException("Error selection type");
                }

                // 判断条件的值字段
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
     * 数据库是否打开
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
