package com.samuel;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import android.content.Context;

import com.samuel.common.PrefsSys;
import com.samuel.common.debug.CrashExceptionHandler;
import com.samuel.common.worklog.WorklogManage;
import com.samuel.main.Constant;
import com.samuel.main.FileManager;

public class MyApplication extends LitePalApplication {
    @SuppressWarnings("unused")
    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mApp;
    private static Context mAppContext;
    private boolean mIsCrmStop = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        // ��ȡ��Context
        mAppContext = getApplicationContext();

        initApp();
        initSetting();
        Connector.getDatabase();
    }

    /**
     * �˳�Ӧ�ó���
     */
    public void exitApp() {
        mIsCrmStop = true;

        // �ر�����service�ͽ�����, ��Ϊ�رղ������첽�Ŀ��ܻ���Щ�ӳ�, �����ⲿ�ֲ���Ҫ���ڿ�ǰ��λ��
        stopAllService(mAppContext);
        
        Connector.getDatabase().close();
    }

    /**
     * ��ȡӦ�ö���ʵ��
     */
    public static MyApplication getApp() {
        return mApp;
    }

    /**
     * ��ȡӦ�ó���������
     * 
     * @return ��������
     */
    public static Context getAppContext() {
        return mAppContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * ��ʼ������
     */
    private void initSetting() {

    }

    /**
     * Ӧ�ó����ʼ��
     */
    private void initApp() {
        // ��������δ�����쳣���
        CrashExceptionHandler.getInstance().init(this);
        FileManager.createDir(Constant.FILE_IMAGE_DIR);
        WorklogManage.init();
        PrefsSys.init(this, Constant.PREFSSYS_NAME);
    }

    /**
     * ��������Service����
     * 
     * @param context
     */
    public void startAllService(Context context) {

    }

    /**
     * ֹͣ����Service����, ��־�ϱ����񵥶�����
     * 
     * @param context
     */
    public void stopAllService(Context context) {

    }

    /**
     * CRM�Ƿ�ֹͣ
     * 
     * @return
     */
    public boolean isCrmStop() {
        return mIsCrmStop;
    }
}
