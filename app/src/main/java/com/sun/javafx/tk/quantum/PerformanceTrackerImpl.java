package com.sun.javafx.tk.quantum;

import com.sun.javafx.perf.PerformanceTracker;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PerformanceTrackerImpl.class */
final class PerformanceTrackerImpl extends PerformanceTracker {
    final PerformanceTrackerHelper helper = PerformanceTrackerHelper.getInstance();

    public PerformanceTrackerImpl() {
        setPerfLoggingEnabled(this.helper.isPerfLoggingEnabled());
    }

    @Override // com.sun.javafx.perf.PerformanceTracker
    public void doLogEvent(String s2) {
        this.helper.logEvent(s2);
    }

    @Override // com.sun.javafx.perf.PerformanceTracker
    public void doOutputLog() {
        this.helper.outputLog();
    }

    @Override // com.sun.javafx.perf.PerformanceTracker
    public long nanoTime() {
        return this.helper.nanoTime();
    }
}
