package com.samuel.common.worklog;

import java.io.File;

import com.samuel.main.Constant;
import com.samuel.main.FileManager;
import com.samuel.main.GpsUtils;

/**
 * 
 * ��־����, ����������ɾ���������ݵĽӿ�
 * 
 * @author Samuel Zhou
 * 
 */
public class WorklogManage {
    /** Ĭ�ϵĲ������� */
    public static final int OPER_RUN = 0;
    /** ��¼���� */
    public static final int OPER_LOGIN = 1;
    /** ע������ */
    public static final int OPER_LOGOUT = 2;
    /** ��ʼ */
    public static final int OPER_START = 3;
    /** ��� */
    public static final int OPER_FINISH = 4;
    /** ���Ͳ��� */
    public static final int OPER_SEND = 5;
    /** Ӧ����� */
    public static final int OPER_ACK = 6;
    /** �����¼ */
    public static final int OPER_ERROR = 7;

    /** ��Ӧ�� */
    public static final int RESULT_WAIT = 0;
    /** �ɹ� */
    public static final int RESULT_SUCCESS = 1;
    /** Ӧ����� */
    public static final int RESULT_ERROR = 2;
    /** ��ʱ��Ӧ�� */
    public static final int RESULT_OVERTIME = 3;
    /** �ռ䲻�� */
    public static final int RESULT_NO_SPACE = 4;
    /** �ظ��ϴ� */
    public static final int RESULT_REPEAT = 5;

    /** ������־�ļ���С�����ֵ: 200K�ֽ� */
    private static final int CRM_LOG_SIZE_MAX = (500 * 1024);

    /**
     * ��ȡĳһ���ָ��������־��¼����
     * 
     * @param date
     *            ��־������
     * @param curPack
     *            ��ǰ�����, ��1��ʼ
     * @return ��־��¼����
     */
    public static WorklogForm getWorklog(String date, int curPack) {
        int filesize, totalPack, offset, length;
        String log;
        WorklogForm worklog = new WorklogForm();
        File dir;

        worklog.setTotalPack(1);
        worklog.setCurPack(1);
        worklog.setLogDate(date);
        worklog.setLog("��־Ϊ��");
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
            // �����ܰ����ͱ����ϴ���־�ĳ���
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
     * ����һ��������־���������־�ļ���
     * 
     * @param operType
     *            ��������, 1: ��¼; 2: �ǳ�; 3: ��ʼ; 4: ���; 5: ��ʼ����; 6: Ӧ��; 7: ����
     * @param id
     *            �����������ݵ�ID, ͨ�����ŵ�ID
     * @param text
     *            ��ʾ�ı�
     * @param result
     *            ������, 0: ��Ӧ��; 1: Ӧ��ɹ�; 2: ����; 3: ��ʱ
     * @return ����ɹ�����ʧ��
     */
    public static boolean saveWorklog(int operType, int id, String text, int result) {
        String logStr, timeStr;
        String operStr[] = { "Run", "Login", "Logout", "Start", "Finish", "Send", "Ack", "Error" };
        String resultStr[] = { "��Ӧ��", "�ɹ�", "����", "��ʱ", "�ռ䲻��", "�ظ�" };
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
        // ��֡��־�ַ���
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
     * ��ʼ���ӿ�, ����ɾ�����ڵ���־�ļ�, �������������ڵ���һ�ξ͹���
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
     * �����ļ����е������ж��Ƿ��ѹ���, ��ǰ��Ч���趨Ϊ100��֮��
     * 
     * @param filename
     *            ��־�ļ���
     * @return true: ��־�ļ�����Ч����; false: �ļ��ѹ���
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
