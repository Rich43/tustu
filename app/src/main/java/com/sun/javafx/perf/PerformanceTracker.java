package com.sun.javafx.perf;

import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import javafx.scene.Scene;

/* loaded from: jfxrt.jar:com/sun/javafx/perf/PerformanceTracker.class */
public abstract class PerformanceTracker {
    private static SceneAccessor sceneAccessor;
    private boolean perfLoggingEnabled;
    private boolean firstPulse = true;
    private float instantFPS;
    private int instantFPSFrames;
    private long instantFPSStartTime;
    private long avgStartTime;
    private int avgFramesTotal;
    private float instantPulses;
    private int instantPulsesFrames;
    private long instantPulsesStartTime;
    private long avgPulsesStartTime;
    private int avgPulsesTotal;
    private Runnable onPulse;
    private Runnable onFirstPulse;
    private Runnable onRenderedFrameTask;

    /* loaded from: jfxrt.jar:com/sun/javafx/perf/PerformanceTracker$SceneAccessor.class */
    public static abstract class SceneAccessor {
        public abstract void setPerfTracker(Scene scene, PerformanceTracker performanceTracker);

        public abstract PerformanceTracker getPerfTracker(Scene scene);
    }

    protected abstract long nanoTime();

    public abstract void doOutputLog();

    public abstract void doLogEvent(String str);

    public static boolean isLoggingEnabled() {
        return Toolkit.getToolkit().getPerformanceTracker().perfLoggingEnabled;
    }

    public static PerformanceTracker getSceneTracker(Scene scene) {
        PerformanceTracker tracker = null;
        if (sceneAccessor != null) {
            tracker = sceneAccessor.getPerfTracker(scene);
            if (tracker == null) {
                tracker = Toolkit.getToolkit().createPerformanceTracker();
            }
            sceneAccessor.setPerfTracker(scene, tracker);
        }
        return tracker;
    }

    public static void releaseSceneTracker(Scene scene) {
        if (sceneAccessor != null) {
            sceneAccessor.setPerfTracker(scene, null);
        }
    }

    public static void setSceneAccessor(SceneAccessor accessor) {
        sceneAccessor = accessor;
    }

    public static void logEvent(String desc) {
        Toolkit.getToolkit().getPerformanceTracker().doLogEvent(desc);
    }

    public static void outputLog() {
        Toolkit.getToolkit().getPerformanceTracker().doOutputLog();
    }

    protected boolean isPerfLoggingEnabled() {
        return this.perfLoggingEnabled;
    }

    protected void setPerfLoggingEnabled(boolean value) {
        this.perfLoggingEnabled = value;
    }

    public synchronized float getInstantFPS() {
        return this.instantFPS;
    }

    public synchronized float getAverageFPS() {
        long nsseconds = nanoTime() - this.avgStartTime;
        if (nsseconds > 0) {
            return (this.avgFramesTotal * 1.0E9f) / nsseconds;
        }
        return getInstantFPS();
    }

    public synchronized void resetAverageFPS() {
        this.avgStartTime = nanoTime();
        this.avgFramesTotal = 0;
    }

    public float getInstantPulses() {
        return this.instantPulses;
    }

    public float getAveragePulses() {
        long nsseconds = nanoTime() - this.avgPulsesStartTime;
        if (nsseconds > 0) {
            return (this.avgPulsesTotal * 1.0E9f) / nsseconds;
        }
        return getInstantPulses();
    }

    public void resetAveragePulses() {
        this.avgPulsesStartTime = nanoTime();
        this.avgPulsesTotal = 0;
    }

    public void pulse() {
        calcPulses();
        updateInstantFps();
        if (this.firstPulse) {
            doLogEvent("first repaint");
            this.firstPulse = false;
            resetAverageFPS();
            resetAveragePulses();
            if (this.onFirstPulse != null) {
                this.onFirstPulse.run();
            }
        }
        if (this.onPulse != null) {
            this.onPulse.run();
        }
    }

    public void frameRendered() {
        calcFPS();
        if (this.onRenderedFrameTask != null) {
            this.onRenderedFrameTask.run();
        }
    }

    private void calcPulses() {
        this.avgPulsesTotal++;
        this.instantPulsesFrames++;
        updateInstantPulses();
    }

    private synchronized void calcFPS() {
        this.avgFramesTotal++;
        this.instantFPSFrames++;
        updateInstantFps();
    }

    private synchronized void updateInstantFps() {
        long timeSince = nanoTime() - this.instantFPSStartTime;
        if (timeSince > NativeMediaPlayer.ONE_SECOND) {
            this.instantFPS = (1.0E9f * this.instantFPSFrames) / timeSince;
            this.instantFPSFrames = 0;
            this.instantFPSStartTime = nanoTime();
        }
    }

    private void updateInstantPulses() {
        long timeSince = nanoTime() - this.instantPulsesStartTime;
        if (timeSince > NativeMediaPlayer.ONE_SECOND) {
            this.instantPulses = (1.0E9f * this.instantPulsesFrames) / timeSince;
            this.instantPulsesFrames = 0;
            this.instantPulsesStartTime = nanoTime();
        }
    }

    public void setOnPulse(Runnable value) {
        this.onPulse = value;
    }

    public Runnable getOnPulse() {
        return this.onPulse;
    }

    public void setOnFirstPulse(Runnable value) {
        this.onFirstPulse = value;
    }

    public Runnable getOnFirstPulse() {
        return this.onFirstPulse;
    }

    public void setOnRenderedFrameTask(Runnable value) {
        this.onRenderedFrameTask = value;
    }

    public Runnable getOnRenderedFrameTask() {
        return this.onRenderedFrameTask;
    }
}
