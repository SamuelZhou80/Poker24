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

    /** �ٶ�AIƽ̨�ķ������ƻ� */
    private static final String PRE_ACCESS_TOKEN = "access_token";
    /** �ٶ�AIƽ̨�ķ������ƻ��Ľ�ֹ��Ч�� */
    private static final String PRE_EXPIRES_DATE = "expires_date";
    
    private static final String PRE_RECENT_FILENAME = "recent_filename";

    /**
     * ��ȡϵͳ����Preferences,
     * 
     * @param context
     * @param prefersname
     */
    public static void init(Context context, String prefersname) {
        mPrefsSys = context.getSharedPreferences(prefersname, Context.MODE_PRIVATE);
        mEditorSys = mPrefsSys.edit();
    }

    /**
     * �����������
     */
    public static void clearPrefsSysData() {
        if (mEditorSys != null) {
            mEditorSys.clear().commit();
        }
    }

    /**
     * �ύ�����ַ���
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
     * �ύ��������ֵ
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
     * �ύ��������ֵ
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
     * �ύ���沼��ֵ
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
     * ��ȡ�ٶ�AIƽ̨�ķ������ƻ�
     * 
     * @return �������ƻ�
     */
    public static String getAccessToken() {
        return mPrefsSys.getString(PRE_ACCESS_TOKEN, "");
    }

    /**
     * ���ðٶ�AIƽ̨�ķ������ƻ�
     * 
     * @param token
     */
    public static void setAccessToken(String token) {
        mEditorSys.putString(PRE_ACCESS_TOKEN, token);
        editCommit(token);
    }
    
    /**
     * ��ȡ�������ƻ�����Ч��
     * 
     * @return �������ƻ�
     */
    public static String getExpiresDate() {
        return mPrefsSys.getString(PRE_EXPIRES_DATE, "");
    }

    /**
     * ���÷������ƻ�����Ч��
     * 
     * @param date
     */
    public static void setExpiresDate(String date) {
        mEditorSys.putString(PRE_EXPIRES_DATE, date);
        editCommit(date);
    }
    
    /**
     * ��ȡ������ʵ��ļ�����
     * 
     * @return ������ʵ��ļ�����
     */
    public static String getRecentFileName() {
        return mPrefsSys.getString(PRE_RECENT_FILENAME, "");
    }

    /**
     * ����������ʵ��ļ�����
     * 
     * @param fileName
     */
    public static void setRecentFileName(String fileName) {
        mEditorSys.putString(PRE_RECENT_FILENAME, fileName);
        editCommit(fileName);
    }
}
