package com.sun.webkit;

import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/webkit/Timer.class */
public class Timer {
    private static Timer instance;
    private static Mode mode;
    long fireTime;

    /* loaded from: jfxrt.jar:com/sun/webkit/Timer$Mode.class */
    public enum Mode {
        PLATFORM_TICKS,
        SEPARATE_THREAD
    }

    private static native void twkFireTimerEvent();

    Timer() {
    }

    public static synchronized Mode getMode() {
        if (mode == null) {
            mode = Boolean.valueOf((String) AccessController.doPrivileged(() -> {
                return System.getProperty("com.sun.webkit.platformticks", "true");
            })).booleanValue() ? Mode.PLATFORM_TICKS : Mode.SEPARATE_THREAD;
        }
        return mode;
    }

    public static synchronized Timer getTimer() {
        if (instance == null) {
            instance = getMode() == Mode.PLATFORM_TICKS ? new Timer() : new SeparateThreadTimer();
        }
        return instance;
    }

    public synchronized void notifyTick() {
        if (this.fireTime > 0 && this.fireTime <= System.currentTimeMillis()) {
            fireTimerEvent(this.fireTime);
        }
    }

    void fireTimerEvent(long time) {
        boolean needFire = false;
        synchronized (this) {
            if (time == this.fireTime) {
                needFire = true;
                this.fireTime = 0L;
            }
        }
        if (needFire) {
            WebPage.lockPage();
            try {
                twkFireTimerEvent();
            } finally {
                WebPage.unlockPage();
            }
        }
    }

    synchronized void setFireTime(long time) {
        this.fireTime = time;
    }

    private static void fwkSetFireTime(double fireTime) {
        getTimer().setFireTime((long) Math.ceil(fireTime * 1000.0d));
    }

    private static void fwkStopTimer() {
        getTimer().setFireTime(0L);
    }
}
