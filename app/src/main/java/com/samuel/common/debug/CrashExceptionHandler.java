package com.samuel.common.debug;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 捕捉工程中未捕捉到的异常
 * 
 * @author zhangmingxun 2012-9-4 创建<br>
 * 
 */
public class CrashExceptionHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashExceptionHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler; // 系统默认的UncaughtException处理类
    private static CrashExceptionHandler mInstance;
    private Context mContext; // 程序的Context对象

    private CrashExceptionHandler() {

    }

    public static CrashExceptionHandler getInstance() {
        if (mInstance == null) {
            mInstance = new CrashExceptionHandler();
        }
        return mInstance;
    }

    /**
     * 初始化
     * 
     * @param context
     */
    public void init(Context context) {
        mContext = context;

        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该CrashHandler为程序的默认处理器
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
            // 退出程序

            // CrmApplication.getApp().exitApp(mContext);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * 
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {

        if (ex == null) {
            return true;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "\n\n很抱歉,程序出现异常,请联系客服.\n\n", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        YXFile.saveException(ex);
        return true;
    }
}
