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
import javafx.scene.input.RotateEvent;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/RotateGestureRecognizer.class */
class RotateGestureRecognizer implements GestureRecognizer {
    private ViewScene scene;
    private static double ROTATATION_THRESHOLD = 5.0d;
    private static boolean ROTATION_INERTIA_ENABLED = true;
    private static double MAX_INITIAL_VELOCITY = 500.0d;
    private static double ROTATION_INERTIA_MILLIS = 1500.0d;
    int modifiers;
    boolean direct;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    int touchPointsInEvent;
    double centerX;
    double centerY;
    double centerAbsX;
    double centerAbsY;
    double currentRotation;
    double angleReference;
    private RotateRecognitionState state = RotateRecognitionState.IDLE;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaRotationVelocity = new SimpleDoubleProperty();
    private double initialInertiaRotationVelocity = 0.0d;
    private double rotationStartTime = 0.0d;
    private double lastTouchEventTime = 0.0d;
    Map<Long, TouchPointTracker> trackers = new HashMap();
    private int currentTouchCount = 0;
    long touchPointID1 = -1;
    long touchPointID2 = -1;
    double totalRotation = 0.0d;
    double inertiaLastTime = 0.0d;

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/RotateGestureRecognizer$RotateRecognitionState.class */
    private enum RotateRecognitionState {
        IDLE,
        TRACKING,
        ACTIVE,
        PRE_INERTIA,
        INERTIA,
        FAILURE
    }

    static {
        AccessController.doPrivileged(() -> {
            String s2 = System.getProperty("com.sun.javafx.gestures.rotate.threshold");
            if (s2 != null) {
                ROTATATION_THRESHOLD = Double.valueOf(s2).doubleValue();
            }
            String s3 = System.getProperty("com.sun.javafx.gestures.rotate.inertia");
            if (s3 != null) {
                ROTATION_INERTIA_ENABLED = Boolean.valueOf(s3).booleanValue();
                return null;
            }
            return null;
        });
    }

    RotateGestureRecognizer(ViewScene scene) {
        this.scene = scene;
        this.inertiaRotationVelocity.addListener(valueModel -> {
            double currentTime = this.inertiaTimeline.getCurrentTime().toSeconds();
            double timePassed = currentTime - this.inertiaLastTime;
            this.inertiaLastTime = currentTime;
            this.currentRotation = timePassed * this.inertiaRotationVelocity.get();
            this.totalRotation += this.currentRotation;
            sendRotateEvent(true);
        });
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyBeginTouchEvent(long time, int modifiers, boolean isDirect, int touchEventCount) {
        params(modifiers, isDirect);
        this.touchPointsSetChanged = false;
        this.touchPointsPressed = false;
        this.touchPointsInEvent = 0;
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyNextTouchEvent(long time, int type, long touchId, int x2, int y2, int xAbs, int yAbs) {
        this.touchPointsInEvent++;
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
                throw new RuntimeException("Error in Rotate gesture recognition: unknown touch state: " + ((Object) this.state));
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Rotate gesture recognition: touch count is zero!");
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

    private double getAngle(TouchPointTracker tp1, TouchPointTracker tp2) {
        double dx = tp2.getAbsX() - tp1.getAbsX();
        double dy = -(tp2.getAbsY() - tp1.getAbsY());
        double newAngle = Math.toDegrees(Math.atan2(dy, dx));
        return newAngle;
    }

    private double getNormalizedDelta(double oldAngle, double newAngle) {
        double delta = -(newAngle - oldAngle);
        if (delta > 180.0d) {
            delta -= 360.0d;
        } else if (delta < -180.0d) {
            delta += 360.0d;
        }
        return delta;
    }

    private void assignActiveTouchpoints() {
        boolean needToReassign = false;
        if (!this.trackers.containsKey(Long.valueOf(this.touchPointID1))) {
            this.touchPointID1 = -1L;
            needToReassign = true;
        }
        if (!this.trackers.containsKey(Long.valueOf(this.touchPointID2))) {
            this.touchPointID2 = -1L;
            needToReassign = true;
        }
        if (needToReassign) {
            for (Long id : this.trackers.keySet()) {
                if (id.longValue() != this.touchPointID1 && id.longValue() != this.touchPointID2) {
                    if (this.touchPointID1 == -1) {
                        this.touchPointID1 = id.longValue();
                    } else if (this.touchPointID2 == -1) {
                        this.touchPointID2 = id.longValue();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyEndTouchEvent(long time) {
        this.lastTouchEventTime = time;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Rotate gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount == 0) {
            if (this.state == RotateRecognitionState.ACTIVE) {
                sendRotateFinishedEvent();
            }
            if (ROTATION_INERTIA_ENABLED) {
                if (this.state == RotateRecognitionState.PRE_INERTIA || this.state == RotateRecognitionState.ACTIVE) {
                    double timeFromLastRotation = (time - this.rotationStartTime) / 1000000.0d;
                    if (timeFromLastRotation < 300.0d) {
                        this.state = RotateRecognitionState.INERTIA;
                        this.inertiaLastTime = 0.0d;
                        if (this.initialInertiaRotationVelocity > MAX_INITIAL_VELOCITY) {
                            this.initialInertiaRotationVelocity = MAX_INITIAL_VELOCITY;
                        } else if (this.initialInertiaRotationVelocity < (-MAX_INITIAL_VELOCITY)) {
                            this.initialInertiaRotationVelocity = -MAX_INITIAL_VELOCITY;
                        }
                        this.inertiaTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(0.0d), new KeyValue(this.inertiaRotationVelocity, Double.valueOf(this.initialInertiaRotationVelocity), Interpolator.LINEAR)), new KeyFrame(Duration.millis((ROTATION_INERTIA_MILLIS * Math.abs(this.initialInertiaRotationVelocity)) / MAX_INITIAL_VELOCITY), (EventHandler<ActionEvent>) event -> {
                            reset();
                        }, new KeyValue(this.inertiaRotationVelocity, 0, Interpolator.LINEAR)));
                        this.inertiaTimeline.playFromStart();
                        return;
                    }
                    reset();
                    return;
                }
                return;
            }
            return;
        }
        if (this.touchPointsPressed && this.state == RotateRecognitionState.INERTIA) {
            this.inertiaTimeline.stop();
            reset();
        }
        if (this.currentTouchCount == 1) {
            if (this.state == RotateRecognitionState.ACTIVE) {
                sendRotateFinishedEvent();
                if (ROTATION_INERTIA_ENABLED) {
                    this.state = RotateRecognitionState.PRE_INERTIA;
                    return;
                } else {
                    reset();
                    return;
                }
            }
            return;
        }
        if (this.state == RotateRecognitionState.IDLE) {
            this.state = RotateRecognitionState.TRACKING;
            assignActiveTouchpoints();
        }
        calculateCenter();
        if (this.touchPointsSetChanged) {
            assignActiveTouchpoints();
        }
        TouchPointTracker tp1 = this.trackers.get(Long.valueOf(this.touchPointID1));
        TouchPointTracker tp2 = this.trackers.get(Long.valueOf(this.touchPointID2));
        double newAngle = getAngle(tp1, tp2);
        if (this.touchPointsSetChanged) {
            this.angleReference = newAngle;
            return;
        }
        this.currentRotation = getNormalizedDelta(this.angleReference, newAngle);
        if (this.state == RotateRecognitionState.TRACKING && Math.abs(this.currentRotation) > ROTATATION_THRESHOLD) {
            this.state = RotateRecognitionState.ACTIVE;
            sendRotateStartedEvent();
        }
        if (this.state == RotateRecognitionState.ACTIVE) {
            this.totalRotation += this.currentRotation;
            sendRotateEvent(false);
            this.angleReference = newAngle;
            double timePassed = (time - this.rotationStartTime) / 1.0E9d;
            if (timePassed > 1.0E-4d) {
                this.initialInertiaRotationVelocity = this.currentRotation / timePassed;
                this.rotationStartTime = time;
            }
        }
    }

    private void sendRotateStartedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent(RotateEvent.ROTATION_STARTED, 0.0d, 0.0d, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendRotateEvent(boolean isInertia) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent(RotateEvent.ROTATE, this.currentRotation, this.totalRotation, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, isInertia);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendRotateFinishedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent(RotateEvent.ROTATION_FINISHED, 0.0d, this.totalRotation, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
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
        if (this.state != RotateRecognitionState.FAILURE) {
            TouchPointTracker tracker = this.trackers.get(Long.valueOf(id));
            if (tracker == null) {
                this.state = RotateRecognitionState.FAILURE;
                throw new RuntimeException("Error in Rotate gesture recognition: released unknown touch point");
            }
            this.trackers.remove(Long.valueOf(id));
        }
        this.currentTouchCount--;
    }

    public void touchMoved(long id, long nanos, int x2, int y2, int xAbs, int yAbs) {
        if (this.state == RotateRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker tracker = this.trackers.get(Long.valueOf(id));
        if (tracker == null) {
            this.state = RotateRecognitionState.FAILURE;
            throw new RuntimeException("Error in rotate gesture recognition: reported unknown touch point");
        }
        tracker.update(nanos, x2, y2, xAbs, yAbs);
    }

    void reset() {
        this.state = RotateRecognitionState.IDLE;
        this.touchPointID1 = -1L;
        this.touchPointID2 = -1L;
        this.currentRotation = 0.0d;
        this.totalRotation = 0.0d;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/RotateGestureRecognizer$TouchPointTracker.class */
    private static class TouchPointTracker {

        /* renamed from: x, reason: collision with root package name */
        double f11969x;

        /* renamed from: y, reason: collision with root package name */
        double f11970y;
        double absX;
        double absY;

        private TouchPointTracker() {
        }

        public void update(long nanos, double x2, double y2, double absX, double absY) {
            this.f11969x = x2;
            this.f11970y = y2;
            this.absX = absX;
            this.absY = absY;
        }

        public double getX() {
            return this.f11969x;
        }

        public double getY() {
            return this.f11970y;
        }

        public double getAbsX() {
            return this.absX;
        }

        public double getAbsY() {
            return this.absY;
        }
    }
}
