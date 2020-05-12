package com.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/**
 * 文件管理类
 * 
 * @author zzh 2012.11.15 创建<br>
 * 
 */
public class FileManager {
    // private static final String TAG = "FileManager";
    private static FileManager instance;
    private static Context mContext;

    private static int generateCount = 0;

    /**
     * 构造函数初始化
     */
    private FileManager(Context context) {
        mContext = context;
    }

    /**
     * 获取文件管理实例
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
     * 创建新文件
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
     * 删除文件
     * 
     * @param fileName
     *            : 文件名
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
     * 文件或目录是否存在
     * 
     * @param fileName
     *            : 文件名
     * @return
     */
    public static Boolean fileIsExist(String fileName) {
        return (new File(fileName)).exists();
    }

    /**
     * 获取文件大小
     * 
     * @param filename
     *            : 文件名
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
     * 将缓存数据写入文件
     * 
     * @param file
     * @param buf
     *            : 数据缓存
     * @param offset
     *            ：数据缓存的偏移
     * @param length
     *            ：写入数据长度
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
     * 将缓存数据写入文件
     * 
     * @param fileName
     *            写入文件名
     * @param write_str
     *            : 写入数据
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
     * 将缓存数据写入到/data/data/<应用程序名>目录上的文件
     * 
     * @param fileName
     *            写入文件名
     * @param write_str
     *            : 写入数据
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
     * 在原有文件上继续写文件
     * 
     * @param fileName
     *            原有文件
     * @param writeStr
     *            文件内容
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
     * 在原有文件上继续写文件
     * 
     * @param fileName
     *            原有文件
     * @param writeStr
     *            文件内容
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
     * 将缓存数据写入文件
     * 
     * @param file
     * @param buf
     *            : 数据缓存
     * @param offset
     *            ：buf的起始地址
     * @param length
     *            ：读取长度
     * @return 读取数据字节数
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
     * 将文件数据读取到缓存
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
     * 将文件数据读取到缓存
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFile(File file, String charset) {
        String readBuf = "";
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            readBuf = new String(buffer, 0, length, charset);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readBuf;
    }

    /**
     * 将文件数据读取到缓存
     * 
     * @param fileName
     *            文件名
     * @param offset
     *            读取数据的开始位置
     * @param length
     *            读取的数据长度
     * @return 获取到的文件数据, 保存为字符串
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
     * 读取文件中的二进制数据
     * 
     * @param fileName 文件名全路径
     * @return
     */
    public static byte[] readFileByte(String fileName) {
        int fileSize = (int) getFileSize(fileName);
        if (fileSize == 0) {
            return null;
        }
        byte fileData[] = new byte[fileSize];
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
            fis.read(fileData, 0, fileSize);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    /**
     * 修改文件或目录名
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
     * 拷贝文件
     * 
     * @param srcFile
     *            源文件
     * @param destFile
     *            目标文件
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
     * 拷贝文件
     * 
     * @param srcFileName
     *            源文件全路径名
     * @param destFileName
     *            目标文件全路径名
     * @throws IOException
     */
    public static boolean copyFile(String srcFileName, String destFileName)
            throws IOException {
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        return copyFile(srcFile, destFile);
    }

    /**
     * 移动一个文件
     * 
     * @param srcFile
     *            源文件
     * @param destFile
     *            目标文件
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
     * 移动单个文件
     * 
     * @param srcFileName
     *            源文件全路径名
     * @param destFileName
     *            目标文件全路径名
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
     * 创建目录
     * 
     * @param dirName
     *            : 文件名
     * @return
     */
    public static boolean createDir(String dirName) {
        return (new File(dirName)).mkdirs();
    }

    /**
     * 删除空目录
     * 
     * @param dirName
     *            : 目录路径
     * @return
     */
    public static boolean delDir(String dirName) {
        File dir = new File(dirName);
        return dir.delete();
    }

    /**
     * 删除目录及目录内的所有文件
     * 
     * @param dir
     *            : 目录
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
     * 删除目录及目录内的所有文件
     * 
     * @param dirName
     *            : 目录全路径名
     * @return
     */
    public static boolean deleteDir(String dirName) {
        File dir = new File(dirName);
        return deleteDir(dir);
    }

    /**
     * 修改目录或文件权限级别
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
     * 生成唯一文件名
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
