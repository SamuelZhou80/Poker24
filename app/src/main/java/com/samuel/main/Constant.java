package com.samuel.main;

import java.io.File;

import android.os.Environment;

public class Constant {
    public static final String PACKAGE_NAME = "com.samuel.mytools";
    // 系统目录
    public static final String SYS_DIR = File.separator + "data" + File.separator + "data"
            + File.separator + PACKAGE_NAME;
    /** 根目录 */
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    /** 掌务通目录 */
    public static final String CRM_DIR = ROOT_DIR + "/crm_test";
    /** 升级包目录 */
    public static final String FILE_UPDATE_DIR = CRM_DIR + "/update/";
    /** 图片目录 */
    public static final String FILE_IMAGE_DIR = CRM_DIR + "/image/";
    /** 巡查测试图片目录 */
    public static final String CHECK_IMAGE_DIR = CRM_DIR + "/testimage/";
    /** 从中心下载的图片存放的目录 */
    public static final String FILE_LOAD_IMAGE_DIR = CRM_DIR + "/loadimage/";
    /** 行政区域目录 */
    public static final String FILE_DISTRICT_DIR = CRM_DIR + "/district/";
    /** 行政区域目录 */
    public static final String FILE_INFO_DIR = CRM_DIR + "/info/";
    /** LOG目录 */
    public static final String FILE_LOG_DIR = CRM_DIR + "/log/";
    /** 异常目录 */
    public static final String FILE_CRASH_DIR = CRM_DIR + "/crash/";
    /** 7天内拜访记录目录 */
    public static final String FILE_VISITED_DIR = CRM_DIR + "/visited/";
    /** ECO LOG目录 */
    public static final String FILE_ECOLOG_DIR = CRM_DIR + "/ecolog/";
    /** 参数文件名 */
    public static final String PREFSSYS_NAME = "PrefsSys";
    /** 全局变量文件名 */
    public static final String PREFSGLOBAL_NAME = "PrefsGlobal";
    /** 拜访数据文件名 */
    public static final String PREFSVISITINFO_NAME = "PrefsVisitInfo";
    /** 数据库文件路径 */
    public static final String DATABASE_DIR = SYS_DIR + "/databases/";
    /** 数据库文件名 */
    public static final String DATABASE_NAME = "education.db";
}
