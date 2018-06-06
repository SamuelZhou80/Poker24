/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.samuel.twentyfour;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/**
 * �ļ�������
 * 
 * @author zzh 2012.11.15 ����<br>
 * 
 */
public class FileManager {
    // private static final String TAG = "FileManager";
    private static FileManager instance;
    private static Context mContext;

    private static int generateCount = 0;

    /**
     * ���캯����ʼ��
     */
    private FileManager(Context context) {
        mContext = context;
    }

    /**
     * ��ȡ�ļ�����ʵ��
     * 
     * @return
     */
    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager(mContext);
        }
        return instance;
    }

    /**
     * �������ļ�
     * 
     * @param fileName
     * @throws IOException
     */
    public static File createNewFile(String fileName) {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * ɾ���ļ�
     * 
     * @param fileName
     *            : �ļ���
     * @return
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        } else {
            return file.delete();
        }
    }

    /**
     * �ļ���Ŀ¼�Ƿ����
     * 
     * @param fileName
     *            : �ļ���
     * @return
     */
    public static Boolean fileIsExist(String fileName) {
        return (new File(fileName)).exists();
    }

    /**
     * ��ȡ�ļ���С
     * 
     * @param filename
     *            : �ļ���
     * @return
     */
    public static long getFileSize(String fileName) {
        long fileSize;
        if (fileName == null) {
            fileSize = 0L;
        } else {
            fileSize = (new File(fileName)).length();
        }
        return fileSize;
    }

    /**
     * ����������д���ļ�
     * 
     * @param file
     * @param buf
     *            : ���ݻ���
     * @param offset
     *            �����ݻ����ƫ��
     * @param length
     *            ��д�����ݳ���
     * @return
     * @throws IOException
     */
    public static void writeFile(File file, byte[] buf, int offset, int length) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write(buf, offset, length);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����������д���ļ�
     * 
     * @param fileName
     *            д���ļ���
     * @param write_str
     *            : д������
     */
    public static void writeFile(String fileName, String writeStr) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName);
            byte[] bytes = writeStr.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����������д�뵽/data/data/<Ӧ�ó�����>Ŀ¼�ϵ��ļ�
     * 
     * @param fileName
     *            д���ļ���
     * @param write_str
     *            : д������
     */
    public static void writeDataFile(String fileName, String writeStr) {
        FileOutputStream fos;
        try {
            fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] bytes = writeStr.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ԭ���ļ��ϼ���д�ļ�
     * 
     * @param fileName
     *            ԭ���ļ�
     * @param writeStr
     *            �ļ�����
     * @return
     * @throws IOException
     */
    public static void appendWriteFile(String fileName, String writeStr) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName, true);
            byte[] bytes = writeStr.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ԭ���ļ��ϼ���д�ļ�
     * 
     * @param fileName
     *            ԭ���ļ�
     * @param writeStr
     *            �ļ�����
     * @return
     * @throws IOException
     */
    public static boolean appendWriteFile(String fileName, byte[] writebytes) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName, true);
            if (fos != null) {
                fos.write(writebytes);
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ����������д���ļ�
     * 
     * @param file
     * @param buf
     *            : ���ݻ���
     * @param offset
     *            ��buf����ʼ��ַ
     * @param length
     *            ����ȡ����
     * @return ��ȡ�����ֽ���
     * @throws IOException
     */
    public static int ReadFile(File file, byte[] buf, int offset, int length) {
        FileInputStream fis;
        int bytes = 0;
        try {
            fis = new FileInputStream(file);
            bytes = fis.read(buf, offset, length);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * ���ļ����ݶ�ȡ������
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readFile(String fileName) {
        String readBuf = "";
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            readBuf = new String(buffer, 0, length, "UTF-8");
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readBuf;
    }

    /**
     * ���ļ����ݶ�ȡ������
     * 
     * @param fileName
     *            �ļ���
     * @param offset
     *            ��ȡ���ݵĿ�ʼλ��
     * @param length
     *            ��ȡ�����ݳ���
     * @return ��ȡ�����ļ�����, ����Ϊ�ַ���
     * @throws IOException
     */
    public static String readFile(String fileName, int offset, int length) {
        int filesize;
        String readBuf = "";
        FileInputStream fis;

        try {
            fis = new FileInputStream(fileName);
            filesize = fis.available();
            if (offset + length > filesize) {
                fis.close();
                return readBuf;
            }
            byte[] buffer = new byte[filesize];
            fis.read(buffer);
            readBuf = new String(buffer, offset, length, "UTF-8");
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readBuf;
    }

    /**
     * �޸��ļ���Ŀ¼��
     * 
     * @param oldFileName
     * @param newFileName
     * @return
     */
    public static boolean renameFile(String oldFileName, String newFileName) {
        File oleFile = new File(oldFileName);
        File newFile = new File(newFileName);
        return oleFile.renameTo(newFile);
    }

    /**
     * �����ļ�
     * 
     * @param srcFile
     *            Դ�ļ�
     * @param destFile
     *            Ŀ���ļ�
     * @throws IOException
     */
    public static boolean copyFile(File srcFile, File destFile) {
        if (srcFile.isDirectory() || destFile.isDirectory()) {
            return false;
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(destFile);
            int readLen = 0;
            byte[] buf = new byte[1024];
            while ((readLen = fis.read(buf)) != -1) {
                fos.write(buf, 0, readLen);
            }
            fos.flush();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * �����ļ�
     * 
     * @param srcFileName
     *            Դ�ļ�ȫ·����
     * @param destFileName
     *            Ŀ���ļ�ȫ·����
     * @throws IOException
     */
    public static boolean copyFile(String srcFileName, String destFileName)
            throws IOException {
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        return copyFile(srcFile, destFile);
    }

    /**
     * �ƶ�һ���ļ�
     * 
     * @param srcFile
     *            Դ�ļ�
     * @param destFile
     *            Ŀ���ļ�
     * @return
     * @throws IOException
     */
    public static boolean moveFile(File srcFile, File destFile) {
        boolean iscopy = copyFile(srcFile, destFile);
        if (!iscopy) {
            return false;
        }
        srcFile.delete();
        return true;
    }

    /**
     * �ƶ������ļ�
     * 
     * @param srcFileName
     *            Դ�ļ�ȫ·����
     * @param destFileName
     *            Ŀ���ļ�ȫ·����
     * @return
     * @throws IOException
     */
    public static boolean moveSDFile(String srcFileName, String destFileName)
            throws IOException {
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        return moveFile(srcFile, destFile);
    }

    /**
     * ����Ŀ¼
     * 
     * @param dirName
     *            : �ļ���
     * @return
     */
    public static boolean createDir(String dirName) {
        return (new File(dirName)).mkdirs();
    }

    /**
     * ɾ����Ŀ¼
     * 
     * @param dirName
     *            : Ŀ¼·��
     * @return
     */
    public static boolean delDir(String dirName) {
        File dir = new File(dirName);
        return dir.delete();
    }

    /**
     * ɾ��Ŀ¼��Ŀ¼�ڵ������ļ�
     * 
     * @param dir
     *            : Ŀ¼
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir == null || !dir.exists() || dir.isFile()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                deleteDir(file);
            }
        }
        dir.delete();
        return true;
    }

    /**
     * ɾ��Ŀ¼��Ŀ¼�ڵ������ļ�
     * 
     * @param dirName
     *            : Ŀ¼ȫ·����
     * @return
     */
    public static boolean deleteDir(String dirName) {
        File dir = new File(dirName);
        return deleteDir(dir);
    }

    /**
     * �޸�Ŀ¼���ļ�Ȩ�޼���
     * 
     * @param cmdStr
     * @param fileName
     */
    public static void changeMod(String cmdStr, String fileName) {
        try {
            Runtime.getRuntime().exec((new StringBuilder()).append("chmod ").append(cmdStr)
                    .append(" ").append(fileName).toString()).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * ����Ψһ�ļ���
     * 
     */
    public static String getUniqueString() {
        if (generateCount > 99999) {
            generateCount = 0;
        }
        String uniqueNumber = Long.toHexString(System.currentTimeMillis()) + Integer.toString(generateCount);
        generateCount++;
        return uniqueNumber;
    }

}
