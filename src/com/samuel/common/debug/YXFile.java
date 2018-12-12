/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.samuel.common.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.samuel.common.worklog.WorklogManage;
import com.samuel.main.Constant;

/**
 * ������־��Ϣ
 * 
 * @author zzh 2012.11.15 ����<br>
 * 
 */
public class YXFile {
    /**
     * ���캯��
     */
    private YXFile() {

    }

    /**
     * ���������Ϣ���ļ���(ÿ������һ���ļ�)
     * 
     * @param ex
     * @return �����ļ�����
     */
    public static String saveException(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(writer));
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        WorklogManage.saveWorklog(WorklogManage.OPER_ERROR, 0, result, WorklogManage.RESULT_ERROR);
        try {
            long timestamp = System.currentTimeMillis();
            DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
            String time = mFormatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";

            File dir = new File(Constant.FILE_CRASH_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            } else {
                File[] fileList = dir.listFiles();
                if (fileList.length > 100) {
                    // ��ֹ�ļ�����,����ɾ��
                    for (int i = 0; i < 100; i++) {
                        File file = fileList[i];
                        file.delete();
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(Constant.FILE_CRASH_DIR + fileName);
            fos.write(result.getBytes());
            fos.close();
            return fileName;
        } catch (Exception e) {

        }
        return null;
    }

}
