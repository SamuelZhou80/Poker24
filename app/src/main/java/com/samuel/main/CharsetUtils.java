package com.samuel.main;

import java.io.UnsupportedEncodingException;

/**
 * 字符集转换工具接口
 * 
 * @author 周思政
 *
 */
public class CharsetUtils {

    /**
     * GBK采用双字节表示，总体编码范围为 8140-FEFE，首字节在 81-FE 之间，尾字节在 40-FE 之间，剔除 xx7F 一条线
     */

    /**
     * 获取一段GBK编码范围内的字符串, 总体编码范围为 8140-FEFE
     * 
     * @param start
     *            编码起始值, 大于等于0x8140
     * @param end
     *            编码结束值, 小于等于0xFEFE
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
                    // 跳过0xXX7F这个空白的码位
                    if (low == 0x7F) {
                        continue;
                    }

                    // 跳过空白码位, 用户自定义区: F8A1-FEFE
                    if (high >= 0xF8 && low > 0xA0) {
                        continue;
                    }
                    // 跳过用户自定义区: A140-A7A0
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
