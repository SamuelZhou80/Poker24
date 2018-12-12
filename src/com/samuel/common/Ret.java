/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.samuel.common;

/**
 * ����ֵ����
 * 
 * @author: zzh 2013-3-22 ����<br>
 */
public interface Ret {
    public static final int NULL = 0; // ��Ӧ��
    public static final int SUCCESS = 1; // �ɹ�
    public static final int FAIL = 2; // ʧ��
    public static final int OVERTIME = 3; // ��ʱ
    public static final int UNFINISHED = 4; // �����ɹ�,���������зְ�

    public static final int ERRUNZIP = 100; // ���ܽ�ѹ������
    public static final int ERRCHKSUM = 101; // У�������
    public static final int ERRSEQID = 102; // ��ˮ�Ŵ���
    public static final int ERRDATALEN = 103; // ���ݳ��ȴ���
    public static final int ERRDATA = 104; // ���ݴ����쳣
    public static final int ERRVER = 105; // ����汾�������Ĳ�ƥ��
    public static final int ERRLOGIN = 106; // ��¼��ռ
    public static final int ERRPROTO = 107; // Э�鴦��ȱʧ
    public static final int ERRCACHE = 108; // �����쳣

    public static final int UPDATENOSPACE = 200;// SD���ռ䲻��
    public static final int UPDATECHKSUM = 201;// У��ʹ���
    public static final int UPDATEAPK = 202;// ��������ʽ����
    public static final int UPDATEFAIL = 203;// ����ʧ��
    public static final int UPDATEWRITE = 204;// �ļ�д��ʧ��

    public static final int EXCEPTION = 400;// �����쳣
    public static final int ERRTIMEOUT = 410;// �������ӳ�ʱ
    public static final int ERRUNCONNET = 420;// ����δ����
    public static final int ERRCALLING = 430;// �绰��
    public static final int ERRURL = 440;// URL����
    public static final int ERRUNKNOWN = 490;// δ֪����
    public static final int ERRSERVER = 500;// ����������
    public static final int ERRSERV = 600;// ���ķ���������
}
