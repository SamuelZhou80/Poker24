package com.samuel.twentyfour;

import java.io.UnsupportedEncodingException;

/**
 * �ַ���ת�����߽ӿ�
 * 
 * @author ��˼��
 *
 */
public class CharsetUtils {

    /**
     * GBK����˫�ֽڱ�ʾ��������뷶ΧΪ 8140-FEFE�����ֽ��� 81-FE ֮�䣬β�ֽ��� 40-FE ֮�䣬�޳� xx7F һ����
     */

    /**
     * ��ȡһ��GBK���뷶Χ�ڵ��ַ���, ������뷶ΧΪ 8140-FEFE
     * 
     * @param start
     *            ������ʼֵ, ���ڵ���0x8140
     * @param end
     *            �������ֵ, С�ڵ���0xFEFE
     * @return
     */
    public static String getGBKTable(int start, int end) {
        StringBuilder sb = new StringBuilder(10000);
        if (start < 0x8140) {
            start = 0x8140;
        }
        if (end < start || end > 0xFEFF) {
            end = 0xFEFF;
        }
        int highStart = (start >> 8) & 0xff;
        int lowStart = start & 0xff;
        int highEnd = (end >> 8) & 0xff;
        int lowEnd = end & 0xff;
        byte[] data = new byte[2];
        try {
            for (int high = highStart; high <= highEnd; high++) {
                for (int low = lowStart; low < lowEnd; low++) {
                    // ����0xXX7F����հ׵���λ
                    if (low == 0x7F) {
                        continue;
                    }

                    // �����հ���λ, �û��Զ�����: F8A1-FEFE
                    if (high >= 0xF8 && low > 0xA0) {
                        continue;
                    }
                    // �����û��Զ�����: A140-A7A0
                    if (high >= 0xA1 && high <= 0xA7) {
                        if (low <= 0xA0) {
                            continue;
                        }
                    }
                    data[0] = (byte) (high);
                    data[1] = (byte) (low);
                    sb.append(new String(data, "GBK"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
