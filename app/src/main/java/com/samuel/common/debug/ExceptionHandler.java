/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.samuel.common.debug;

/**
 * �쳣������
 * 
 * @author �´ӻ� V1.00 2011.05.20 ����<br>
 *
 */
public class ExceptionHandler {
    /**
     * ���������쳣
     * 
     * @param e
     *            �쳣
     */
    public static void handle(RuntimeException e) {
        throw e;
    }
}