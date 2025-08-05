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
import javafx.scene.input.ZoomEvent;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/ZoomGestureRecognizer.class */
class ZoomGestureRecognizer implements GestureRecognizer {
    private static double ZOOM_FACTOR_THRESHOLD = 0.1d;
    private static boolean ZOOM_INERTIA_ENABLED = true;
    private static double MAX_ZOOMIN_VELOCITY = 3.0d;
    private static double MAX_ZOOMOUT_VELOCITY = 0.3333d;
    private static double ZOOM_INERTIA_MILLIS = 500.0d;
    private static double MAX_ZOOM_IN_FACTOR = 10.0d;
    private static double MAX_ZOOM_OUT_FACTOR = 0.1d;
    private ViewScene scene;
    private int modifiers;
    private boolean direct;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    private double centerX;
    private double centerY;
    private double centerAbsX;
    private double centerAbsY;
    private double currentDistance;
    private double distanceReference;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaZoomVelocity = new SimpleDoubleProperty();
    private double initialInertiaZoomVelocity = 0.0d;
    private double zoomStartTime = 0.0d;
    private double lastTouchEventTime = 0.0d;
    private ZoomRecognitionState state = ZoomRecognitionState.IDLE;
    private Map<Long, TouchPointTracker> trackers = new HashMap();
    private int currentTouchCount = 0;
    private double zoomFactor = 1.0d;
    private double totalZoomFactor = 1.0d;
    double inertiaLastTime = 0.0d;

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/ZoomGestureRecognizer$ZoomRecognitionState.class */
    private enum ZoomRecognitionState {
        IDLE,
        TRACKING,
        ACTIVE,
        PRE_INERTIA,
        INERTIA,
        FAILURE
    }

    static {
        AccessController.doPrivileged(() -> {
            String s2 = System.getProperty("com.sun.javafx.gestures.zoom.threshold");
            if (s2 != null) {
                ZOOM_FACTOR_THRESHOLD = Double.valueOf(s2).doubleValue();
            }
            String s3 = System.getProperty("com.sun.javafx.gestures.zoom.inertia");
            if (s3 != null) {
                ZOOM_INERTIA_ENABLED = Boolean.valueOf(s3).booleanValue();
                return null;
            }
            return null;
        });
    }

    ZoomGestureRecognizer(ViewScene scene) {
        this.scene = scene;
        this.inertiaZoomVelocity.addListener(valueModel -> {
            double currentTime = this.inertiaTimeline.getCurrentTime().toSeconds();
            double timePassed = currentTime - this.inertiaLastTime;
            this.inertiaLastTime = currentTime;
            double prevTotalZoomFactor = this.totalZoomFactor;
            this.totalZoomFactor += timePassed * this.inertiaZoomVelocity.get();
            this.zoomFactor = this.totalZoomFactor / prevTotalZoomFactor;
            sendZoomEvent(true);
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
                throw new RuntimeException("Error in Zoom gesture recognition: unknown touch state: " + ((Object) this.state));
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Zoom gesture recognition: touch count is zero!");
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

    private double calculateMaxDistance() {
        double maxSquareDist = 0.0d;
        for (TouchPointTracker tracker : this.trackers.values()) {
            double deltaX = tracker.getAbsX() - this.centerAbsX;
            double deltaY = tracker.getAbsY() - this.centerAbsY;
            double squareDist = (deltaX * deltaX) + (deltaY * deltaY);
            if (squareDist > maxSquareDist) {
                maxSquareDist = squareDist;
            }
        }
        return Math.sqrt(maxSquareDist);
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyEndTouchEvent(long time) {
        this.lastTouchEventTime = time;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Zoom gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount == 0) {
            if (this.state == ZoomRecognitionState.ACTIVE) {
                sendZoomFinishedEvent();
            }
            if (ZOOM_INERTIA_ENABLED && (this.state == ZoomRecognitionState.PRE_INERTIA || this.state == ZoomRecognitionState.ACTIVE)) {
                double timeFromLastZoom = (time - this.zoomStartTime) / 1000000.0d;
                if (this.initialInertiaZoomVelocity != 0.0d && timeFromLastZoom < 200.0d) {
                    this.state = ZoomRecognitionState.INERTIA;
                    this.inertiaLastTime = 0.0d;
                    double duration = ZOOM_INERTIA_MILLIS / 1000.0d;
                    double newZoom = this.totalZoomFactor + (this.initialInertiaZoomVelocity * duration);
                    if (this.initialInertiaZoomVelocity > 0.0d) {
                        if (newZoom / this.totalZoomFactor > MAX_ZOOM_IN_FACTOR) {
                            duration = ((this.totalZoomFactor * MAX_ZOOM_IN_FACTOR) - this.totalZoomFactor) / this.initialInertiaZoomVelocity;
                        }
                    } else if (newZoom / this.totalZoomFactor < MAX_ZOOM_OUT_FACTOR) {
                        duration = ((this.totalZoomFactor * MAX_ZOOM_OUT_FACTOR) - this.totalZoomFactor) / this.initialInertiaZoomVelocity;
                    }
                    this.inertiaTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(0.0d), new KeyValue(this.inertiaZoomVelocity, Double.valueOf(this.initialInertiaZoomVelocity), Interpolator.LINEAR)), new KeyFrame(Duration.seconds(duration), (EventHandler<ActionEvent>) event -> {
                        reset();
                    }, new KeyValue(this.inertiaZoomVelocity, 0, Interpolator.LINEAR)));
                    this.inertiaTimeline.playFromStart();
                    return;
                }
                reset();
                return;
            }
            reset();
            return;
        }
        if (this.touchPointsPressed && this.state == ZoomRecognitionState.INERTIA) {
            this.inertiaTimeline.stop();
            reset();
        }
        if (this.currentTouchCount == 1) {
            if (this.state == ZoomRecognitionState.ACTIVE) {
                sendZoomFinishedEvent();
                if (ZOOM_INERTIA_ENABLED) {
                    this.state = ZoomRecognitionState.PRE_INERTIA;
                    return;
                } else {
                    reset();
                    return;
                }
            }
            return;
        }
        if (this.state == ZoomRecognitionState.IDLE) {
            this.state = ZoomRecognitionState.TRACKING;
            this.zoomStartTime = time;
        }
        calculateCenter();
        double currentDistance = calculateMaxDistance();
        if (this.touchPointsSetChanged) {
            this.distanceReference = currentDistance;
            return;
        }
        this.zoomFactor = currentDistance / this.distanceReference;
        if (this.state == ZoomRecognitionState.TRACKING && Math.abs(this.zoomFactor - 1.0d) > ZOOM_FACTOR_THRESHOLD) {
            this.state = ZoomRecognitionState.ACTIVE;
            sendZoomStartedEvent();
        }
        if (this.state == ZoomRecognitionState.ACTIVE) {
            double prevTotalZoomFactor = this.totalZoomFactor;
            this.totalZoomFactor *= this.zoomFactor;
            sendZoomEvent(false);
            this.distanceReference = currentDistance;
            double timePassed = (time - this.zoomStartTime) / 1.0E9d;
            if (timePassed > 1.0E-4d) {
                this.initialInertiaZoomVelocity = (this.totalZoomFactor - prevTotalZoomFactor) / timePassed;
                this.zoomStartTime = time;
            } else {
                this.initialInertiaZoomVelocity = 0.0d;
            }
        }
    }

    private void sendZoomStartedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM_STARTED, 1.0d, 1.0d, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendZoomEvent(boolean isInertia) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM, this.zoomFactor, this.totalZoomFactor, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, isInertia);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendZoomFinishedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM_FINISHED, 1.0d, this.totalZoomFactor, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
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
        if (this.state != ZoomRecognitionState.FAILURE) {
            TouchPointTracker tracker = this.trackers.get(Long.valueOf(id));
            if (tracker == null) {
                this.state = ZoomRecognitionState.FAILURE;
                throw new RuntimeException("Error in Zoom gesture recognition: released unknown touch point");
            }
            this.trackers.remove(Long.valueOf(id));
        }
        this.currentTouchCount--;
    }

    public void touchMoved(long id, long nanos, int x2, int y2, int xAbs, int yAbs) {
        if (this.state == ZoomRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker tracker = this.trackers.get(Long.valueOf(id));
        if (tracker == null) {
            this.state = ZoomRecognitionState.FAILURE;
            throw new RuntimeException("Error in zoom gesture recognition: reported unknown touch point");
        }
        tracker.update(nanos, x2, y2, xAbs, yAbs);
    }

    void reset() {
        this.state = ZoomRecognitionState.IDLE;
        this.zoomFactor = 1.0d;
        this.totalZoomFactor = 1.0d;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/ZoomGestureRecognizer$TouchPointTracker.class */
    private static class TouchPointTracker {

        /* renamed from: x, reason: collision with root package name */
        double f11973x;

        /* renamed from: y, reason: collision with root package name */
        double f11974y;
        double absX;
        double absY;

        private TouchPointTracker() {
        }

        public void update(long nanos, double x2, double y2, double absX, double absY) {
            this.f11973x = x2;
            this.f11974y = y2;
            this.absX = absX;
            this.absY = absY;
        }

        public double getX() {
            return this.f11973x;
        }

        public double getY() {
            return this.f11974y;
        }

        public double getAbsX() {
            return this.absX;
        }

        public double getAbsY() {
            return this.absY;
        }
    }
}
