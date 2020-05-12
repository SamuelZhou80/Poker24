package com.samuel.common;

/**
 * 返回值类型
 * 
 * @author zzh 2013-3-22 创建<br>
 */
public interface Ret {
    public static final int NULL = 0; // 无应答
    public static final int SUCCESS = 1; // 成功
    public static final int FAIL = 2; // 失败
    public static final int OVERTIME = 3; // 超时
    public static final int UNFINISHED = 4; // 本包成功,但后续还有分包

    public static final int ERRUNZIP = 100; // 解密解压缩错误
    public static final int ERRCHKSUM = 101; // 校验码错误
    public static final int ERRSEQID = 102; // 流水号错误
    public static final int ERRDATALEN = 103; // 数据长度错误
    public static final int ERRDATA = 104; // 数据处理异常
    public static final int ERRVER = 105; // 程序版本号与中心不匹配
    public static final int ERRLOGIN = 106; // 登录被占
    public static final int ERRPROTO = 107; // 协议处理缺失
    public static final int ERRCACHE = 108; // 缓存异常

    public static final int UPDATENOSPACE = 200;// SD卡空间不足
    public static final int UPDATECHKSUM = 201;// 校验和错误
    public static final int UPDATEAPK = 202;// 升级包格式错误
    public static final int UPDATEFAIL = 203;// 下载失败
    public static final int UPDATEWRITE = 204;// 文件写入失败

    public static final int EXCEPTION = 400;// 网络异常
    public static final int ERRTIMEOUT = 410;// 网络连接超时
    public static final int ERRUNCONNET = 420;// 网络未连接
    public static final int ERRCALLING = 430;// 电话中
    public static final int ERRURL = 440;// URL错误
    public static final int ERRUNKNOWN = 490;// 未知错误
    public static final int ERRSERVER = 500;// 服务器错误
    public static final int ERRSERV = 600;// 中心服务器错误
}
