package com.samuel.debug;

import com.samuel.common.Constant;
import com.samuel.debug.worklog.WorklogManage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 保存日志信息
 * 
 * @author zzh 2012.11.15 创建<br>
 * 
 */
public class YXFile {
    /**
     * 构造函数
     */
    private YXFile() {

    }

    /**
     * 保存错误信息到文件中(每个错误一个文件)
     * 
     * @param ex
     * @return 返回文件名称
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
            DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 用于格式化日期,作为日志文件名的一部分
            String time = mFormatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";

            File dir = new File(Constant.FILE_CRASH_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            } else {
                File[] fileList = dir.listFiles();
                if (fileList.length > 100) {
                    // 防止文件过多,启动删除
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
