package com.samuel.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PrefsSys {
    private static final String TAG = PrefsSys.class.getSimpleName();
    private static final boolean ISDEBUG = false;
    private static SharedPreferences mPrefsSys = null;
    private static Editor mEditorSys = null;

    /** 百度AI平台的访问令牌环 */
    private static final String PRE_ACCESS_TOKEN = "access_token";
    /** 百度AI平台的访问令牌环的截止有效期 */
    private static final String PRE_EXPIRES_DATE = "expires_date";
    
    private static final String PRE_RECENT_FILENAME = "recent_filename";

    /**
     * 获取系统参数Preferences,
     * 
     * @param context
     * @param prefersname
     */
    public static void init(Context context, String prefersname) {
        mPrefsSys = context.getSharedPreferences(prefersname, Context.MODE_PRIVATE);
        mEditorSys = mPrefsSys.edit();
    }

    /**
     * 清除所有数据
     */
    public static void clearPrefsSysData() {
        if (mEditorSys != null) {
            mEditorSys.clear().commit();
        }
    }

    /**
     * 提交保存字符串
     * 
     * @param value
     */
    private static void editCommit(String value) {
        if (!mEditorSys.commit()) {
            if (ISDEBUG) {
                Log.i(TAG, "fail to save" + value);
            }
        }
    }

    /**
     * 提交保存整数值
     * 
     * @param value
     */
    @SuppressWarnings("unused")
    private static void editCommit(int value) {
        if (!mEditorSys.commit()) {
            if (ISDEBUG) {
                Log.i(TAG, "fail to save" + value);
            }
        }
    }

    /**
     * 提交保存整数值
     * 
     * @param value
     */
    @SuppressWarnings("unused")
    private static void editCommit(long value) {
        if (!mEditorSys.commit()) {
            if (ISDEBUG) {
                Log.i(TAG, "fail to save" + value);
            }
        }
    }

    /**
     * 提交保存布尔值
     * 
     * @param value
     */
    @SuppressWarnings("unused")
    private static void editCommit(boolean value) {
        if (!mEditorSys.commit()) {
            if (ISDEBUG) {
                Log.i(TAG, "fail to save" + value);
            }
        }
    }

    /**
     * 获取百度AI平台的访问令牌环
     * 
     * @return 访问令牌环
     */
    public static String getAccessToken() {
        return mPrefsSys.getString(PRE_ACCESS_TOKEN, "");
    }

    /**
     * 设置百度AI平台的访问令牌环
     * 
     * @param token
     */
    public static void setAccessToken(String token) {
        mEditorSys.putString(PRE_ACCESS_TOKEN, token);
        editCommit(token);
    }
    
    /**
     * 获取访问令牌环的有效期
     * 
     * @return 访问令牌环
     */
    public static String getExpiresDate() {
        return mPrefsSys.getString(PRE_EXPIRES_DATE, "");
    }

    /**
     * 设置访问令牌环的有效期
     * 
     * @param date
     */
    public static void setExpiresDate(String date) {
        mEditorSys.putString(PRE_EXPIRES_DATE, date);
        editCommit(date);
    }
    
    /**
     * 获取最近访问的文件名称
     * 
     * @return 最近访问的文件名称
     */
    public static String getRecentFileName() {
        return mPrefsSys.getString(PRE_RECENT_FILENAME, "");
    }

    /**
     * 设置最近访问的文件名称
     * 
     * @param fileName
     */
    public static void setRecentFileName(String fileName) {
        mEditorSys.putString(PRE_RECENT_FILENAME, fileName);
        editCommit(fileName);
    }
}
