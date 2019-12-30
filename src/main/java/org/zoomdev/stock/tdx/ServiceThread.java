package org.zoomdev.stock.tdx;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 服务线程
 *
 * @author Randy
 */
public abstract class ServiceThread extends Thread{


    private static final Log log = LogFactory.getLog(ServiceThread.class);

    /**
     * 是否正在运行
     */
    protected volatile boolean running;


    public ServiceThread() {
        this.setDaemon(true);
    }



    public boolean isRunning() {
        return running;
    }

    /**
     * 启动
     */
    public synchronized void start() {
        if (running) return;
        running = true;
        super.start();
    }


    /**
     * 停止
     */
    public synchronized void shutdown() {
        if (!running) return;
        running = false;
        interrupt();

    }

    protected abstract boolean repetitionRun();


    protected void init(){

    }

    @Override
    public void run() {
        while (running) {
            try {
                if (!repetitionRun()) break;
            } catch (Throwable e) {
                log.warn("Service exception:", e);
            }
        }

        log.info("ServiceThread has exit!");
    }
}
