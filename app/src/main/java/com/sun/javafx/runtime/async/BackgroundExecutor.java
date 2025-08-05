package com.sun.javafx.runtime.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: jfxrt.jar:com/sun/javafx/runtime/async/BackgroundExecutor.class */
public class BackgroundExecutor {
    private static ExecutorService instance;
    private static ScheduledExecutorService timerInstance;

    private BackgroundExecutor() {
    }

    public static synchronized ExecutorService getExecutor() {
        if (instance == null) {
            instance = Executors.newCachedThreadPool(r2 -> {
                Thread t2 = new Thread(r2);
                t2.setPriority(1);
                return t2;
            });
            ((ThreadPoolExecutor) instance).setKeepAliveTime(1L, TimeUnit.SECONDS);
        }
        return instance;
    }

    public static synchronized ScheduledExecutorService getTimer() {
        if (timerInstance == null) {
            timerInstance = new ScheduledThreadPoolExecutor(1, r2 -> {
                Thread t2 = new Thread(r2);
                t2.setDaemon(true);
                return t2;
            });
        }
        return timerInstance;
    }

    private static synchronized void shutdown() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
        if (timerInstance != null) {
            timerInstance.shutdown();
            timerInstance = null;
        }
    }
}
