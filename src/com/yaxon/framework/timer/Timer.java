package com.yaxon.framework.timer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;

/**
 * ��ʱ������
 * 
 * @author �´ӻ� V1.00 2011.05.18 ����<br>
 * 
 */
public class Timer {
    private static final int PERIOD_MS = 200; // ��ʱ�̶߳�ʱ����, ��λ: ����
    private static TimerServer msServer = new TimerServer(PERIOD_MS);
    private int period; // ��ʱ����
    private int left; // ʣ�µļ�ʱʱ��
    private boolean running; // ��ʶ�Ƿ�������״̬
    private Object obj; // �û�����
    private TimerListener listener; // ��ʱ��������

    /**
     * ȱʡ������
     */
    public Timer() {
        period = 0;
        left = 0;
        running = false;
        obj = null;
        listener = null;
    }

    /**
     * ������
     * 
     * @param listener
     *            ��ʱ��������
     */
    public Timer(TimerListener listener) {
        this(listener, null);
    }

    /**
     * ������
     * 
     * @param listener
     *            ��ʱ��������
     * @param obj
     *            �û����ݶ���
     */
    public Timer(TimerListener listener, Object obj) {
        this();
        this.obj = obj;
        this.listener = listener;
    }

    /**
     * ������ʱ��
     * 
     * @param period
     *            ��ʱ����, ��λ: ms
     * @return true: �����ɹ�, false: ����ʧ��
     */
    public boolean start(int period) {
        return start(period, this.listener, this.obj);
    }

    /**
     * ������ʱ��
     * 
     * @param period
     *            ��ʱ����, ��λ: ����
     * @param listener
     *            ��ʱ������
     * @return true: �����ɹ�, false: ����ʧ��
     */
    public boolean start(int period, TimerListener listener) {
        return start(period, listener, this.obj);
    }

    /**
     * ������ʱ��
     * 
     * @param period
     *            ��ʱ������
     * @param listener
     *            ��ʱ������
     * @param obj
     *            �û����ݶ���
     * @return true: �����ɹ�, false: ����ʧ��
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
     * ֹͣ��ʱ��
     */
    public void stop() {
        if (running) {
            running = false;
            msServer.removeTimer(this);
        }
    }

    /**
     * ���Զ�ʱ���Ƿ�������״̬
     * 
     * @return true: ������, false: ��ֹͣ
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * ��ȡ��ʱʣ��ʱ��
     * 
     * @return ʣ��ʱ��, ��λ: ����
     */
    public int leftTime() {
        if (running) {
            return left * PERIOD_MS;
        } else {
            return 0;
        }
    }

    /**
     * ��ʱ������
     * 
     * @author �´ӻ� V1.00 2011.05.18 ����<br>
     * 
     */
    private static class TimerServer {
        private TimerThread mThread = null;
        private LinkedList<Timer> runList = new LinkedList<Timer>();
        private LinkedList<Timer> addList = new LinkedList<Timer>();
        private boolean handling = false;
        private boolean needClear = false;

        /**
         * ������
         * 
         * @param period
         *            ��ʱ����, ��λ: ����
         */
        private TimerServer(int period) {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message ms) {
                    handling = true; // ���������еĶ�ʱ��, ���Զ�ʱʱ���Ƿ��ѵ�
                    for (Timer tm : runList) {
                        if (tm.running && --tm.left == 0) {
                            tm.left = tm.period;
                            if (tm.listener != null) {
                                tm.listener.onPeriod(tm.obj);
                            }
                        }
                    }
                    handling = false;

                    for (Timer tm : addList) { // ��׷�������еĶ�ʱ����ӵ�����������
                        runList.add(tm);
                    }
                    addList.clear();

                    if (needClear) { // �Ƴ������д�ɾ���Ķ�ʱ��
                        Iterator<Timer> it = runList.iterator();
                        while (it.hasNext()) {
                            Timer timer = it.next();
                            if (!timer.running) {
                                it.remove();
                            }
                        }
                        needClear = false;
                    }

                    if (runList.size() == 0) { // ���������޶�ʱ��, ��ʹ�ö�ʱ�߳̽���˯��
                        mThread.setStop();
                    }
                }
            };

            mThread = new TimerThread(period);
            mThread.start();
            mThread.setServerHandler(handler);
        }

        /**
         * ����ʱ����������׷��һ����ʱ��
         * 
         * @param timer
         *            ��׷�ӵĶ�ʱ��
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
                    mThread.setRun(); // ��������׷��ǰ�޶�ʱ��, ��������ʱ�߳�
                }
            }
        }

        /**
         * �Ӷ�ʱ�����������Ƴ�һ����ʱ��
         * 
         * @param timer
         *            ���Ƴ��Ķ�ʱ��
         */
        private void removeTimer(Timer timer) {
            if (addList.remove(timer)) {
                return;
            }
            if (!handling) { // �Ȳ����Ƿ��ڴ���ʱ����׶�
                runList.remove(timer);
            } else {
                needClear = true;
            }
        }
    }

    /**
     * ��ʱ�߳�
     * 
     * @author �´ӻ� V1.00 2011.05.18 ����<br>
     * 
     */
    private static class TimerThread extends Thread {
        private Handler serHandler; // server��Ϣ������
        private boolean running = false;
        private int period = 0;

        /**
         * ������
         * 
         * @param period
         *            ��ʱ����, ��λ: ����
         */
        TimerThread(int period) {
            super();
            this.period = period;
        }

        /**
         * ����server����Ϣ������
         * 
         * @param serHandler
         *            ��Ϣ������
         */
        void setServerHandler(Handler serHandler) {
            synchronized (this) {
                this.serHandler = serHandler;
            }
        }

        /**
         * �߳�ִ�к���
         */
        @Override
        public void run() {
            setName("timer thread"); // �����߳�����
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
         * ���ó�����״̬
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
         * ���ó�ֹͣ״̬
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
