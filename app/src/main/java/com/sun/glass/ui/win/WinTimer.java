package com.sun.glass.ui.win;

import com.sun.glass.ui.Timer;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinTimer.class */
final class WinTimer extends Timer {
    private static final int minPeriod = _getMinPeriod();
    private static final int maxPeriod = _getMaxPeriod();

    private static native int _getMinPeriod();

    private static native int _getMaxPeriod();

    @Override // com.sun.glass.ui.Timer
    protected native long _start(Runnable runnable, int i2);

    @Override // com.sun.glass.ui.Timer
    protected native void _stop(long j2);

    protected WinTimer(Runnable runnable) {
        super(runnable);
    }

    static int getMinPeriod_impl() {
        return minPeriod;
    }

    static int getMaxPeriod_impl() {
        return maxPeriod;
    }

    @Override // com.sun.glass.ui.Timer
    protected long _start(Runnable runnable) {
        throw new RuntimeException("vsync timer not supported");
    }
}
