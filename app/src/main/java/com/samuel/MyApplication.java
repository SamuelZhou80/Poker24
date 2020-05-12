package com.samuel;

import android.content.Context;

import com.framework.FileManager;
import com.samuel.common.Constant;
import com.samuel.common.PrefsSys;
import com.samuel.debug.CrashExceptionHandler;
import com.samuel.debug.worklog.WorklogManage;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

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
        // 获取主Context
        mAppContext = getApplicationContext();

        initApp();
        initSetting();
        Connector.getDatabase();
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        mIsCrmStop = true;

        // 关闭所有service和接收者, 因为关闭操作是异步的可能会有些延迟, 所以这部分操作要放在靠前的位置
        stopAllService(mAppContext);
        
        Connector.getDatabase().close();
    }

    /**
     * 获取应用对象实例
     */
    public static MyApplication getApp() {
        return mApp;
    }

    /**
     * 获取应用程序上下文
     * 
     * @return 主上下文
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
     * 初始化设置
     */
    private void initSetting() {

    }

    /**
     * 应用程序初始化
     */
    private void initApp() {
        // 启动程序未捕获异常监控
        CrashExceptionHandler.getInstance().init(this);
        FileManager.createDir(Constant.FILE_IMAGE_DIR);
        WorklogManage.init();
        PrefsSys.init(this, Constant.PREFSSYS_NAME);
    }

    /**
     * 启动所有Service服务
     * 
     * @param context
     */
    public void startAllService(Context context) {

    }

    /**
     * 停止所有Service服务, 日志上报服务单独处理
     * 
     * @param context
     */
    public void stopAllService(Context context) {

    }

    /**
     * CRM是否停止
     * 
     * @return
     */
    public boolean isCrmStop() {
        return mIsCrmStop;
    }
}
