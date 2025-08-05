package com.sun.javafx.tk.quantum;

import com.sun.glass.events.TouchEvent;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/ScrollGestureRecognizer.class */
class ScrollGestureRecognizer implements GestureRecognizer {
    private static double SCROLL_THRESHOLD = 10.0d;
    private static boolean SCROLL_INERTIA_ENABLED = true;
    private static double MAX_INITIAL_VELOCITY = 1000.0d;
    private static double SCROLL_INERTIA_MILLIS = 1500.0d;
    private ViewScene scene;
    private int modifiers;
    private boolean direct;
    private int lastTouchCount;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    private double centerX;
    private double centerY;
    private double centerAbsX;
    private double centerAbsY;
    private double lastCenterAbsX;
    private double lastCenterAbsY;
    private double deltaX;
    private double deltaY;
    private double totalDeltaX;
    private double totalDeltaY;
    private double factorX;
    private double factorY;
    private ScrollRecognitionState state = ScrollRecognitionState.IDLE;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaScrollVelocity = new SimpleDoubleProperty();
    private double initialInertiaScrollVelocity = 0.0d;
    private double scrollStartTime = 0.0d;
    private double lastTouchEventTime = 0.0d;
    private Map<Long, TouchPointTracker> trackers = new HashMap();
    private int currentTouchCount = 0;
    double inertiaLastTime = 0.0d;

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/ScrollGestureRecognizer$ScrollRecognitionState.class */
    private enum ScrollRecognitionState {
        IDLE,
        TRACKING,
        ACTIVE,
        INERTIA,
        FAILURE
    }

    static {
        AccessController.doPrivileged(() -> {
            String s2 = System.getProperty("com.sun.javafx.gestures.scroll.threshold");
            if (s2 != null) {
                SCROLL_THRESHOLD = Double.valueOf(s2).doubleValue();
            }
            String s3 = System.getProperty("com.sun.javafx.gestures.scroll.inertia");
            if (s3 != null) {
                SCROLL_INERTIA_ENABLED = Boolean.valueOf(s3).booleanValue();
                return null;
            }
            return null;
        });
    }

    ScrollGestureRecognizer(ViewScene scene) {
        this.scene = scene;
        this.inertiaScrollVelocity.addListener(valueModel -> {
            double currentTime = this.inertiaTimeline.getCurrentTime().toSeconds();
            double timePassed = currentTime - this.inertiaLastTime;
            this.inertiaLastTime = currentTime;
            double scrollVectorSize = timePassed * this.inertiaScrollVelocity.get();
            this.deltaX = scrollVectorSize * this.factorX;
            this.totalDeltaX += this.deltaX;
            this.deltaY = scrollVectorSize * this.factorY;
            this.totalDeltaY += this.deltaY;
            sendScrollEvent(true, this.centerAbsX, this.centerAbsY, this.currentTouchCount);
        });
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyBeginTouchEvent(long time, int modifiers, boolean isDirect, int touchEventCount) {
        params(modifiers, isDirect);
        this.touchPointsSetChanged = false;
        this.touchPointsPressed = false;
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyNextTouchEvent(long time, int type, long touchId, int x2, int y2, int xAbs, int yAbs) {
        switch (type) {
            case TouchEvent.TOUCH_PRESSED /* 811 */:
                this.touchPointsSetChanged = true;
                this.touchPointsPressed = true;
                touchPressed(touchId, time, x2, y2, xAbs, yAbs);
                return;
            case TouchEvent.TOUCH_MOVED /* 812 */:
                touchMoved(touchId, time, x2, y2, xAbs, yAbs);
                return;
            case TouchEvent.TOUCH_RELEASED /* 813 */:
                this.touchPointsSetChanged = true;
                touchReleased(touchId, time, x2, y2, xAbs, yAbs);
                return;
            case TouchEvent.TOUCH_STILL /* 814 */:
                return;
            default:
                throw new RuntimeException("Error in Scroll gesture recognition: unknown touch state: " + ((Object) this.state));
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Scroll gesture recognition: touch count is zero!");
        }
        double totalX = 0.0d;
        double totalY = 0.0d;
        double totalAbsX = 0.0d;
        double totalAbsY = 0.0d;
        for (TouchPointTracker tracker : this.trackers.values()) {
            totalX += tracker.getX();
            totalY += tracker.getY();
            totalAbsX += tracker.getAbsX();
            totalAbsY += tracker.getAbsY();
        }
        this.centerX = totalX / this.currentTouchCount;
        this.centerY = totalY / this.currentTouchCount;
        this.centerAbsX = totalAbsX / this.currentTouchCount;
        this.centerAbsY = totalAbsY / this.currentTouchCount;
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyEndTouchEvent(long time) {
        this.lastTouchEventTime = time;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Scroll gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount < 1) {
            if (this.state == ScrollRecognitionState.ACTIVE) {
                sendScrollFinishedEvent(this.lastCenterAbsX, this.lastCenterAbsY, this.lastTouchCount);
                if (SCROLL_INERTIA_ENABLED) {
                    double timeFromLastScroll = (time - this.scrollStartTime) / 1000000.0d;
                    if (timeFromLastScroll < 300.0d) {
                        this.state = ScrollRecognitionState.INERTIA;
                        this.inertiaLastTime = 0.0d;
                        if (this.initialInertiaScrollVelocity > MAX_INITIAL_VELOCITY) {
                            this.initialInertiaScrollVelocity = MAX_INITIAL_VELOCITY;
                        }
                        this.inertiaTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(0.0d), new KeyValue(this.inertiaScrollVelocity, Double.valueOf(this.initialInertiaScrollVelocity), Interpolator.LINEAR)), new KeyFrame(Duration.millis((SCROLL_INERTIA_MILLIS * Math.abs(this.initialInertiaScrollVelocity)) / MAX_INITIAL_VELOCITY), (EventHandler<ActionEvent>) event -> {
                            reset();
                        }, new KeyValue(this.inertiaScrollVelocity, 0, Interpolator.LINEAR)));
                        this.inertiaTimeline.playFromStart();
                        return;
                    }
                    reset();
                    return;
                }
                reset();
                return;
            }
            return;
        }
        calculateCenter();
        if (this.touchPointsPressed && this.state == ScrollRecognitionState.INERTIA) {
            this.inertiaTimeline.stop();
            reset();
        }
        if (this.touchPointsSetChanged) {
            if (this.state == ScrollRecognitionState.IDLE) {
                this.state = ScrollRecognitionState.TRACKING;
            }
            if (this.state == ScrollRecognitionState.ACTIVE) {
                sendScrollFinishedEvent(this.lastCenterAbsX, this.lastCenterAbsY, this.lastTouchCount);
                this.totalDeltaX = 0.0d;
                this.totalDeltaY = 0.0d;
                sendScrollStartedEvent(this.centerAbsX, this.centerAbsY, this.currentTouchCount);
            }
            this.lastTouchCount = this.currentTouchCount;
            this.lastCenterAbsX = this.centerAbsX;
            this.lastCenterAbsY = this.centerAbsY;
            return;
        }
        this.deltaX = this.centerAbsX - this.lastCenterAbsX;
        this.deltaY = this.centerAbsY - this.lastCenterAbsY;
        if (this.state == ScrollRecognitionState.TRACKING && (Math.abs(this.deltaX) > SCROLL_THRESHOLD || Math.abs(this.deltaY) > SCROLL_THRESHOLD)) {
            this.state = ScrollRecognitionState.ACTIVE;
            sendScrollStartedEvent(this.centerAbsX, this.centerAbsY, this.currentTouchCount);
        }
        if (this.state == ScrollRecognitionState.ACTIVE) {
            this.totalDeltaX += this.deltaX;
            this.totalDeltaY += this.deltaY;
            sendScrollEvent(false, this.centerAbsX, this.centerAbsY, this.currentTouchCount);
            double timePassed = (time - this.scrollStartTime) / 1.0E9d;
            if (timePassed > 1.0E-4d) {
                double scrollMagnitude = Math.sqrt((this.deltaX * this.deltaX) + (this.deltaY * this.deltaY));
                this.factorX = this.deltaX / scrollMagnitude;
                this.factorY = this.deltaY / scrollMagnitude;
                this.initialInertiaScrollVelocity = scrollMagnitude / timePassed;
                this.scrollStartTime = time;
            }
            this.lastCenterAbsX = this.centerAbsX;
            this.lastCenterAbsY = this.centerAbsY;
        }
    }

    private void sendScrollStartedEvent(double centerX, double centerY, int touchCount) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL_STARTED, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 1.0d, touchCount, 0, 0, 0, 0, centerX, centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendScrollEvent(boolean isInertia, double centerX, double centerY, int touchCount) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL, this.deltaX, this.deltaY, this.totalDeltaX, this.totalDeltaY, 1.0d, 1.0d, touchCount, 0, 0, 0, 0, centerX, centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, isInertia);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendScrollFinishedEvent(double centerX, double centerY, int touchCount) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL_FINISHED, 0.0d, 0.0d, this.totalDeltaX, this.totalDeltaY, 1.0d, 1.0d, touchCount, 0, 0, 0, 0, centerX, centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public void params(int modifiers, boolean direct) {
        this.modifiers = modifiers;
        this.direct = direct;
    }

    public void touchPressed(long id, long nanos, int x2, int y2, int xAbs, int yAbs) {
        this.currentTouchCount++;
        TouchPointTracker tracker = new TouchPointTracker();
        tracker.update(nanos, x2, y2, xAbs, yAbs);
        this.trackers.put(Long.valueOf(id), tracker);
    }

    public void touchReleased(long id, long nanos, int x2, int y2, int xAbs, int yAbs) {
        if (this.state != ScrollRecognitionState.FAILURE) {
            TouchPointTracker tracker = this.trackers.get(Long.valueOf(id));
            if (tracker == null) {
                this.state = ScrollRecognitionState.FAILURE;
                throw new RuntimeException("Error in Scroll gesture recognition: released unknown touch point");
            }
            this.trackers.remove(Long.valueOf(id));
        }
        this.currentTouchCount--;
    }

    public void touchMoved(long id, long nanos, int x2, int y2, int xAbs, int yAbs) {
        if (this.state == ScrollRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker tracker = this.trackers.get(Long.valueOf(id));
        if (tracker == null) {
            this.state = ScrollRecognitionState.FAILURE;
            throw new RuntimeException("Error in scroll gesture recognition: reported unknown touch point");
        }
        tracker.update(nanos, x2, y2, xAbs, yAbs);
    }

    void reset() {
        this.state = ScrollRecognitionState.IDLE;
        this.totalDeltaX = 0.0d;
        this.totalDeltaY = 0.0d;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/ScrollGestureRecognizer$TouchPointTracker.class */
    private static class TouchPointTracker {

        /* renamed from: x, reason: collision with root package name */
        double f11971x;

        /* renamed from: y, reason: collision with root package name */
        double f11972y;
        double absX;
        double absY;

        private TouchPointTracker() {
        }

        public void update(long nanos, double x2, double y2, double absX, double absY) {
            this.f11971x = x2;
            this.f11972y = y2;
            this.absX = absX;
            this.absY = absY;
        }

        public double getX() {
            return this.f11971x;
        }

        public double getY() {
            return this.f11972y;
        }

        public double getAbsX() {
            return this.absX;
        }

        public double getAbsY() {
            return this.absY;
        }
    }
}
