/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.samuel.common.debug;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * ��׽������δ��׽�����쳣
 * 
 * @author zhangmingxun 2012-9-4 ����<br>
 * 
 */

public class CrashExceptionHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashExceptionHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler; // ϵͳĬ�ϵ�UncaughtException������
    private static CrashExceptionHandler mInstance;
    private Context mContext; // �����Context����

    private CrashExceptionHandler() {

    }

    public static CrashExceptionHandler getInstance() {
        if (mInstance == null) {
            mInstance = new CrashExceptionHandler();
        }
        return mInstance;
    }

    /**
     * ��ʼ��
     * 
     * @param context
     */
    public void init(Context context) {
        mContext = context;

        // ��ȡϵͳĬ�ϵ�UncaughtException������
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);

        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            // �˳�����

            // CrmApplication.getApp().exitApp(mContext);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
     * 
     * @param ex
     * @return true:��������˸��쳣��Ϣ;���򷵻�false.
     */
    private boolean handleException(Throwable ex) {

        if (ex == null) {
            return true;
        }
        // ʹ��Toast����ʾ�쳣��Ϣ
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "\n\n�ܱ�Ǹ,��������쳣,����ϵ�ͷ�.\n\n", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        YXFile.saveException(ex);
        return true;
    }
}
