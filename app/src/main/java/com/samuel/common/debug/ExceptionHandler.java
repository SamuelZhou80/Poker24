/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.samuel.common.debug;

/**
 * 异常处理器
 * 
 * @author 陈从华 V1.00 2011.05.20 创建<br>
 *
 */
public class ExceptionHandler {
    /**
     * 处理运行异常
     * 
     * @param e
     *            异常
     */
    public static void handle(RuntimeException e) {
        throw e;
    }
}