package com.samuel.common.worklog;

import java.io.File;

import com.samuel.main.Constant;
import com.samuel.main.FileManager;
import com.samuel.main.GpsUtils;

/**
 * 
 * 日志管理, 包括新增和删除过期数据的接口
 * 
 * @author Samuel Zhou
 * 
 */
public class WorklogManage {
    /** 默认的操作类型 */
    public static final int OPER_RUN = 0;
    /** 登录操作 */
    public static final int OPER_LOGIN = 1;
    /** 注销操作 */
    public static final int OPER_LOGOUT = 2;
    /** 开始 */
    public static final int OPER_START = 3;
    /** 完成 */
    public static final int OPER_FINISH = 4;
    /** 发送操作 */
    public static final int OPER_SEND = 5;
    /** 应答操作 */
    public static final int OPER_ACK = 6;
    /** 错误记录 */
    public static final int OPER_ERROR = 7;

    /** 待应答 */
    public static final int RESULT_WAIT = 0;
    /** 成功 */
    public static final int RESULT_SUCCESS = 1;
    /** 应答错误 */
    public static final int RESULT_ERROR = 2;
    /** 超时无应答 */
    public static final int RESULT_OVERTIME = 3;
    /** 空间不足 */
    public static final int RESULT_NO_SPACE = 4;
    /** 重复上传 */
    public static final int RESULT_REPEAT = 5;

    /** 定义日志文件大小的最大值: 200K字节 */
    private static final int CRM_LOG_SIZE_MAX = (500 * 1024);

    /**
     * 获取某一天的指定包的日志记录对象
     * 
     * @param date
     *            日志的日期
     * @param curPack
     *            当前包序号, 从1开始
     * @return 日志记录对象
     */
    public static WorklogForm getWorklog(String date, int curPack) {
        int filesize, totalPack, offset, length;
        String log;
        WorklogForm worklog = new WorklogForm();
        File dir;

        worklog.setTotalPack(1);
        worklog.setCurPack(1);
        worklog.setLogDate(date);
        worklog.setLog("日志为空");
        try {
            dir = new File(Constant.FILE_LOG_DIR);
            if (!dir.exists()) {
                return worklog;
            }
            String fileName = Constant.FILE_LOG_DIR + date + ".txt";
            if (!FileManager.fileIsExist(fileName)) {
                return worklog;
            }
            filesize = (int) FileManager.getFileSize(fileName);
            if (curPack <= 1) {
                curPack = 1;
            }
            // 计算总包数和本次上传日志的长度
            offset = (curPack - 1) * WorklogForm.LOG_PACKET_SIZE;
            totalPack = filesize / WorklogForm.LOG_PACKET_SIZE;
            if (filesize % WorklogForm.LOG_PACKET_SIZE != 0) {
                totalPack = totalPack + 1;
            }
            if (offset + WorklogForm.LOG_PACKET_SIZE < filesize) {
                length = WorklogForm.LOG_PACKET_SIZE;
            } else {
                length = filesize - offset;
            }
            log = FileManager.readFile(fileName, offset, length);
            worklog.setTotalPack(totalPack);
            worklog.setCurPack(curPack);
            worklog.setLogDate(date);
            worklog.setLog(log);
        } catch (Exception e) {

        }
        return worklog;
    }

    /**
     * 新增一条工作日志到今天的日志文件中
     * 
     * @param operType
     *            操作类型, 1: 登录; 2: 登出; 3: 开始; 4: 完成; 5: 开始发送; 6: 应答; 7: 错误
     * @param id
     *            所操作的数据的ID, 通常是门店ID
     * @param text
     *            提示文本
     * @param result
     *            处理结果, 0: 待应答; 1: 应答成功; 2: 错误; 3: 超时
     * @return 保存成功或是失败
     */
    public static boolean saveWorklog(int operType, int id, String text, int result) {
        String logStr, timeStr;
        String operStr[] = { "Run", "Login", "Logout", "Start", "Finish", "Send", "Ack", "Error" };
        String resultStr[] = { "待应答", "成功", "错误", "超时", "空间不足", "重复" };
        File dir;

        if (operType < 0 || operType > 7) {
            operType = 0;
        }
        if (result < 0 || result > 5) {
            result = 0;
        }
        if (text == null) {
            text = "";
        }
        // 组帧日志字符串
        timeStr = GpsUtils.getDTime();
        logStr = String.format("%s [%s %d %s %s]\r\n",
                timeStr, operStr[operType], id, text, resultStr[result]);
        try {
            dir = new File(Constant.FILE_LOG_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = Constant.FILE_LOG_DIR + GpsUtils.getDate() + ".txt";
            if (FileManager.getFileSize(fileName) > CRM_LOG_SIZE_MAX) {
                return false;
            }
            FileManager.appendWriteFile(fileName, logStr);
            // FileManager.appendWriteFile(fileName, logStr, "UTF-8");
        } catch (Exception e) {
        }
        return true;
    }

    /**
     * 初始化接口, 用于删除过期的日志文件, 程序运行周期内调用一次就够了
     */
    public static void init() {
        File dir, file;
        File[] fileList;

        dir = new File(Constant.FILE_LOG_DIR);
        if (!dir.exists()) {
            return;
        }
        fileList = dir.listFiles();
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                file = fileList[i];
                if (!isLogFileValid(file.getName())) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 根据文件名中的日期判断是否已过期, 当前有效期设定为100天之内
     * 
     * @param filename
     *            日志文件名
     * @return true: 日志文件在有效期内; false: 文件已过期
     */
    private static boolean isLogFileValid(String filename) {
        int pos, dayOffset1, dayOffset2;
        int[] date, curDate;
        String dateStr;

        pos = GpsUtils.findCharPos(filename, '.', 0, filename.length());
        dateStr = filename.substring(0, pos);
        date = GpsUtils.getCurDateBytes(dateStr);
        curDate = GpsUtils.getCurDateBytes(GpsUtils.getDate());
        dayOffset1 = GpsUtils.getAllDays(date[0], date[1], date[2]);
        dayOffset2 = GpsUtils.getAllDays(curDate[0], curDate[1], curDate[2]);
        if (dayOffset1 + 100 < dayOffset2) {
            return false;
        }

        return true;
    }
}
