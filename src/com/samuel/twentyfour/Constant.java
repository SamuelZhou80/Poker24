package com.samuel.twentyfour;

import android.os.Environment;

public class Constant {
    /** ��Ŀ¼ */
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    /** ����ͨĿ¼ */
    public static final String CRM_DIR = ROOT_DIR + "/crm_test";
    /** ������Ŀ¼ */
    public static final String FILE_UPDATE_DIR = CRM_DIR + "/update/";
    /** ͼƬĿ¼ */
    public static final String FILE_IMAGE_DIR = CRM_DIR + "/image/";
    /** Ѳ�����ͼƬĿ¼ */
    public static final String CHECK_IMAGE_DIR = CRM_DIR + "/testimage/";
    /** ���������ص�ͼƬ��ŵ�Ŀ¼ */
    public static final String FILE_LOAD_IMAGE_DIR = CRM_DIR + "/loadimage/";
    /** ��������Ŀ¼ */
    public static final String FILE_DISTRICT_DIR = CRM_DIR + "/district/";
    /** ��������Ŀ¼ */
    public static final String FILE_INFO_DIR = CRM_DIR + "/info/";
    /** LOGĿ¼ */
    public static final String FILE_LOG_DIR = CRM_DIR + "/log/";
    /** �쳣Ŀ¼ */
    public static final String FILE_CRASH_DIR = CRM_DIR + "/crash/";
    /** 7���ڰݷü�¼Ŀ¼ */
    public static final String FILE_VISITED_DIR = CRM_DIR + "/visited/";
    /** �����ļ��� */
    public static final String PREFSSYS_NAME = "PrefsSys";
    /** ȫ�ֱ����ļ��� */
    public static final String PREFSGLOBAL_NAME = "PrefsGlobal";
    /** �ݷ������ļ��� */
    public static final String PREFSVISITINFO_NAME = "PrefsVisitInfo";
    /** ���ݿ��ļ�·�� */
    // public static final String DATABASE_DIR = SYS_DIR + "/databases/";

}
