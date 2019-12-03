package com.yaxon.framework.timer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;

/**
 * 定时器驱动
 * 
 * @author 陈从华 V1.00 2011.05.18 创建<br>
 * 
 */
public class Timer {
    private static final int PERIOD_MS = 200; // 定时线程定时周期, 单位: 毫秒
    private static TimerServer msServer = new TimerServer(PERIOD_MS);
    private int period; // 定时周期
    private int left; // 剩下的计时时间
    private boolean running; // 标识是否处于运行状态
    private Object obj; // 用户对象
    private TimerListener listener; // 定时器监听器

    /**
     * 缺省构造器
     */
    public Timer() {
        period = 0;
        left = 0;
        running = false;
        obj = null;
        listener = null;
    }

    /**
     * 构造器
     * 
     * @param listener
     *            定时器监听器
     */
    public Timer(TimerListener listener) {
        this(listener, null);
    }

    /**
     * 构造器
     * 
     * @param listener
     *            定时器监听器
     * @param obj
     *            用户数据对象
     */
    public Timer(TimerListener listener, Object obj) {
        this();
        this.obj = obj;
        this.listener = listener;
    }

    /**
     * 启动定时器
     * 
     * @param period
     *            定时周期, 单位: ms
     * @return true: 启动成功, false: 启动失败
     */
    public boolean start(int period) {
        return start(period, this.listener, this.obj);
    }

    /**
     * 启动定时器
     * 
     * @param period
     *            定时周期, 单位: 毫秒
     * @param listener
     *            定时监听器
     * @return true: 启动成功, false: 启动失败
     */
    public boolean start(int period, TimerListener listener) {
        return start(period, listener, this.obj);
    }

    /**
     * 启动定时器
     * 
     * @param period
     *            定时器周期
     * @param listener
     *            定时监听器
     * @param obj
     *            用户数据对象
     * @return true: 启动成功, false: 启动失败
     */
    public boolean start(int period, TimerListener listener, Object obj) {
        if (listener == null) {
            return false;
        }
        this.listener = listener;
        this.obj = obj;
        this.period = period / PERIOD_MS;
        if (this.period == 0) {
            this.period = 1;
        }
        left = this.period;
        if (!running) {
            running = true;
            msServer.addTimer(this);
        }
        return true;
    }

    /**
     * 停止定时器
     */
    public void stop() {
        if (running) {
            running = false;
            msServer.removeTimer(this);
        }
    }

    /**
     * 测试定时器是否处于运行状态
     * 
     * @return true: 运行中, false: 已停止
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * 获取定时剩余时间
     * 
     * @return 剩余时间, 单位: 毫秒
     */
    public int leftTime() {
        if (running) {
            return left * PERIOD_MS;
        } else {
            return 0;
        }
    }

    /**
     * 定时服务器
     * 
     * @author 陈从华 V1.00 2011.05.18 创建<br>
     * 
     */
    private static class TimerServer {
        private TimerThread mThread = null;
        private LinkedList<Timer> runList = new LinkedList<Timer>();
        private LinkedList<Timer> addList = new LinkedList<Timer>();
        private boolean handling = false;
        private boolean needClear = false;

        /**
         * 构造器
         * 
         * @param period
         *            定时周期, 单位: 毫秒
         */
        private TimerServer(int period) {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message ms) {
                    handling = true; // 遍历链表中的定时器, 测试定时时间是否已到
                    for (Timer tm : runList) {
                        if (tm.running && --tm.left == 0) {
                            tm.left = tm.period;
                            if (tm.listener != null) {
                                tm.listener.onPeriod(tm.obj);
                            }
                        }
                    }
                    handling = false;

                    for (Timer tm : addList) { // 将追加链表中的定时器添加到运行链表中
                        runList.add(tm);
                    }
                    addList.clear();

                    if (needClear) { // 移除链表中待删除的定时器
                        Iterator<Timer> it = runList.iterator();
                        while (it.hasNext()) {
                            Timer timer = it.next();
                            if (!timer.running) {
                                it.remove();
                            }
                        }
                        needClear = false;
                    }

                    if (runList.size() == 0) { // 如链表中无定时器, 则使得定时线程进入睡眠
                        mThread.setStop();
                    }
                }
            };

            mThread = new TimerThread(period);
            mThread.start();
            mThread.setServerHandler(handler);
        }

        /**
         * 往定时器服务器中追加一个定时器
         * 
         * @param timer
         *            待追加的定时器
         */
        private void addTimer(Timer timer) {
            if (runList.indexOf(timer) >= 0) {
                return;
            }
            if (addList.indexOf(timer) >= 0) {
                return;
            }
            if (handling) {
                addList.add(timer);
            } else {
                runList.add(timer);
                if (runList.size() == 1) {
                    mThread.setRun(); // 如链表中追加前无定时器, 则启动定时线程
                }
            }
        }

        /**
         * 从定时器服务器中移除一个定时器
         * 
         * @param timer
         *            待移除的定时器
         */
        private void removeTimer(Timer timer) {
            if (addList.remove(timer)) {
                return;
            }
            if (!handling) { // 先测试是否处于处理定时溢出阶段
                runList.remove(timer);
            } else {
                needClear = true;
            }
        }
    }

    /**
     * 定时线程
     * 
     * @author 陈从华 V1.00 2011.05.18 创建<br>
     * 
     */
    private static class TimerThread extends Thread {
        private Handler serHandler; // server消息处理句柄
        private boolean running = false;
        private int period = 0;

        /**
         * 构造器
         * 
         * @param period
         *            定时周期, 单位: 毫秒
         */
        TimerThread(int period) {
            super();
            this.period = period;
        }

        /**
         * 设置server的消息处理句柄
         * 
         * @param serHandler
         *            消息处理句柄
         */
        void setServerHandler(Handler serHandler) {
            synchronized (this) {
                this.serHandler = serHandler;
            }
        }

        /**
         * 线程执行函数
         */
        @Override
        public void run() {
            setName("timer thread"); // 设置线程名称
            while (true) {
                synchronized (this) {
                    while (!running || period == 0) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            ;
                        }
                    }
                }
                if (period > 0) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(period);
                    } catch (InterruptedException e) {
                        ;
                    }
                    synchronized (this) {
                        if (serHandler != null) {
                            serHandler.sendEmptyMessage(0);
                        }
                    }
                }
            }
        }

        /**
         * 设置成运行状态
         */
        void setRun() {
            synchronized (this) {
                if (!running) {
                    running = true;
                    this.notify();
                }
            }
        }

        /**
         * 设置成停止状态
         */
        void setStop() {
            synchronized (this) {
                if (running) {
                    running = false;
                }
            }
        }
    }
}
