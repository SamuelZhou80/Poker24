package com.samuel.debug.worklog;

/**
 * 待上传日志的数据类
 * 
 * @author Samuel Zhou
 * 
 */
public class WorklogForm {
    /** 一次上传的数据包的大小, 设置为500K字节 */
    public static final int LOG_PACKET_SIZE = (500 * 1024);
    private int mTotalPack;// 日志的总包数
    private int mCurPack;// 日志的当前包序号, 从1开始
    private String mDate = "";// 日志的日期
    private String mLog = "";// 当前待上传的日志包

    public void setTotalPack(int total) {
        mTotalPack = total;
    }

    public int getTotalPack() {
        return mTotalPack;
    }

    public void setCurPack(int cur) {
        mCurPack = cur;
    }

    public int getCurPack() {
        return mCurPack;
    }

    public void setLogDate(String date) {
        mDate = date;
    }

    public String getLogDate() {
        return mDate;
    }

    public void setLog(String log) {
        mLog = log;
    }

    public String getLog() {
        return mLog;
    }

}
