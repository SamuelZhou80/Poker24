package com.samuel.common.worklog;

/**
 * ���ϴ���־��������
 * 
 * @author Samuel Zhou
 * 
 */
public class WorklogForm {
    /** һ���ϴ������ݰ��Ĵ�С, ����Ϊ500K�ֽ� */
    public static final int LOG_PACKET_SIZE = (500 * 1024);
    private int mTotalPack;// ��־���ܰ���
    private int mCurPack;// ��־�ĵ�ǰ�����, ��1��ʼ
    private String mDate = "";// ��־������
    private String mLog = "";// ��ǰ���ϴ�����־��

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
