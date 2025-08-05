package com.sun.javafx.tk.quantum;

import com.sun.glass.events.TouchEvent;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventType;
import javafx.scene.input.SwipeEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/SwipeGestureRecognizer.class */
class SwipeGestureRecognizer implements GestureRecognizer {
    private static final double TANGENT_30_DEGREES = 0.577d;
    private static final double TANGENT_45_DEGREES = 1.0d;
    private static final boolean VERBOSE = false;
    private static final double DISTANCE_THRESHOLD = 10.0d;
    private static final double BACKWARD_DISTANCE_THRASHOLD = 5.0d;
    private SwipeRecognitionState state = SwipeRecognitionState.IDLE;
    MultiTouchTracker tracker = new MultiTouchTracker();
    private ViewScene scene;

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/SwipeGestureRecognizer$SwipeRecognitionState.class */
    private enum SwipeRecognitionState {
        IDLE,
        ADDING,
        REMOVING,
        FAILURE
    }

    SwipeGestureRecognizer(ViewScene scene) {
        this.scene = scene;
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyBeginTouchEvent(long time, int modifiers, boolean isDirect, int touchEventCount) {
        this.tracker.params(modifiers, isDirect);
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyNextTouchEvent(long time, int type, long touchId, int x2, int y2, int xAbs, int yAbs) {
        switch (type) {
            case TouchEvent.TOUCH_PRESSED /* 811 */:
                this.tracker.pressed(touchId, time, x2, y2, xAbs, yAbs);
                return;
            case TouchEvent.TOUCH_MOVED /* 812 */:
            case TouchEvent.TOUCH_STILL /* 814 */:
                this.tracker.progress(touchId, time, xAbs, yAbs);
                return;
            case TouchEvent.TOUCH_RELEASED /* 813 */:
                this.tracker.released(touchId, time, x2, y2, xAbs, yAbs);
                return;
            default:
                throw new RuntimeException("Error in swipe gesture recognition: unknown touch state: " + ((Object) this.state));
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyEndTouchEvent(long time) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EventType<SwipeEvent> calcSwipeType(TouchPointTracker tracker) {
        double distanceX = tracker.getDistanceX();
        double distanceY = tracker.getDistanceY();
        double absDistanceX = Math.abs(distanceX);
        double absDistanceY = Math.abs(distanceY);
        boolean horizontal = absDistanceX > absDistanceY;
        double primaryDistance = horizontal ? distanceX : distanceY;
        double absPrimaryDistance = horizontal ? absDistanceX : absDistanceY;
        double absSecondaryDistance = horizontal ? absDistanceY : absDistanceX;
        double absPrimaryLength = horizontal ? tracker.lengthX : tracker.lengthY;
        double maxSecondaryDeviation = horizontal ? tracker.maxDeviationY : tracker.maxDeviationX;
        double lastPrimaryMovement = horizontal ? tracker.lastXMovement : tracker.lastYMovement;
        if (absPrimaryDistance <= 10.0d || absSecondaryDistance > absPrimaryDistance * TANGENT_30_DEGREES || maxSecondaryDeviation > absPrimaryDistance * 1.0d) {
            return null;
        }
        int swipeMaxDuration = Integer.getInteger("com.sun.javafx.gestures.swipe.maxduration", 300).intValue();
        if (tracker.getDuration() > swipeMaxDuration || absPrimaryLength > absPrimaryDistance * 1.5d) {
            return null;
        }
        if (Math.signum(primaryDistance) == Math.signum(lastPrimaryMovement) || Math.abs(lastPrimaryMovement) <= BACKWARD_DISTANCE_THRASHOLD) {
            return horizontal ? tracker.getDistanceX() < 0.0d ? SwipeEvent.SWIPE_LEFT : SwipeEvent.SWIPE_RIGHT : tracker.getDistanceY() < 0.0d ? SwipeEvent.SWIPE_UP : SwipeEvent.SWIPE_DOWN;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSwipeType(EventType<SwipeEvent> swipeType, CenterComputer cc, int touchCount, int modifiers, boolean isDirect) {
        if (swipeType == null) {
            return;
        }
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.swipeEvent(swipeType, touchCount, cc.getX(), cc.getY(), cc.getAbsX(), cc.getAbsY(), (modifiers & 1) != 0, (modifiers & 4) != 0, (modifiers & 8) != 0, (modifiers & 16) != 0, isDirect);
                return null;
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/SwipeGestureRecognizer$CenterComputer.class */
    private static class CenterComputer {
        double totalAbsX;
        double totalAbsY;
        double totalX;
        double totalY;
        int count;

        private CenterComputer() {
            this.totalAbsX = 0.0d;
            this.totalAbsY = 0.0d;
            this.totalX = 0.0d;
            this.totalY = 0.0d;
            this.count = 0;
        }

        public void add(double x2, double y2, double xAbs, double yAbs) {
            this.totalAbsX += xAbs;
            this.totalAbsY += yAbs;
            this.totalX += x2;
            this.totalY += y2;
            this.count++;
        }

        public double getX() {
            if (this.count == 0) {
                return 0.0d;
            }
            return this.totalX / this.count;
        }

        public double getY() {
            if (this.count == 0) {
                return 0.0d;
            }
            return this.totalY / this.count;
        }

        public double getAbsX() {
            if (this.count == 0) {
                return 0.0d;
            }
            return this.totalAbsX / this.count;
        }

        public double getAbsY() {
            if (this.count == 0) {
                return 0.0d;
            }
            return this.totalAbsY / this.count;
        }

        public void reset() {
            this.totalX = 0.0d;
            this.totalY = 0.0d;
            this.totalAbsX = 0.0d;
            this.totalAbsY = 0.0d;
            this.count = 0;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/SwipeGestureRecognizer$MultiTouchTracker.class */
    private class MultiTouchTracker {
        SwipeRecognitionState state;
        Map<Long, TouchPointTracker> trackers;
        CenterComputer cc;
        int modifiers;
        boolean direct;
        private int touchCount;
        private int currentTouchCount;
        private EventType<SwipeEvent> type;

        private MultiTouchTracker() {
            this.state = SwipeRecognitionState.IDLE;
            this.trackers = new HashMap();
            this.cc = new CenterComputer();
        }

        public void params(int modifiers, boolean direct) {
            this.modifiers = modifiers;
            this.direct = direct;
        }

        public void pressed(long id, long nanos, int x2, int y2, int xAbs, int yAbs) {
            this.currentTouchCount++;
            switch (this.state) {
                case IDLE:
                    this.currentTouchCount = 1;
                    this.state = SwipeRecognitionState.ADDING;
                    break;
                case ADDING:
                    break;
                case REMOVING:
                    this.state = SwipeRecognitionState.FAILURE;
                    return;
                default:
                    return;
            }
            TouchPointTracker tracker = new TouchPointTracker();
            tracker.start(nanos, x2, y2, xAbs, yAbs);
            this.trackers.put(Long.valueOf(id), tracker);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public void released(long id, long nanos, int x2, int y2, int xAbs, int yAbs) {
            if (this.state != SwipeRecognitionState.FAILURE) {
                TouchPointTracker tracker = this.trackers.get(Long.valueOf(id));
                if (tracker == null) {
                    this.state = SwipeRecognitionState.FAILURE;
                    throw new RuntimeException("Error in swipe gesture recognition: released unknown touch point");
                }
                tracker.end(nanos, x2, y2, xAbs, yAbs);
                this.cc.add(tracker.beginX, tracker.beginY, tracker.beginAbsX, tracker.beginAbsY);
                this.cc.add(tracker.endX, tracker.endY, tracker.endAbsX, tracker.endAbsY);
                EventType<SwipeEvent> swipeType = SwipeGestureRecognizer.this.calcSwipeType(tracker);
                switch (this.state) {
                    case IDLE:
                        reset();
                        throw new RuntimeException("Error in swipe gesture recognition: released touch point outside of gesture");
                    case ADDING:
                        this.state = SwipeRecognitionState.REMOVING;
                        this.touchCount = this.currentTouchCount;
                        this.type = swipeType;
                        this.trackers.remove(Long.valueOf(id));
                        break;
                    case REMOVING:
                        if (this.type != swipeType) {
                            this.state = SwipeRecognitionState.FAILURE;
                        }
                        this.trackers.remove(Long.valueOf(id));
                        break;
                    default:
                        this.trackers.remove(Long.valueOf(id));
                        break;
                }
            }
            this.currentTouchCount--;
            if (this.currentTouchCount == 0) {
                if (this.state == SwipeRecognitionState.REMOVING) {
                    SwipeGestureRecognizer.this.handleSwipeType(this.type, this.cc, this.touchCount, this.modifiers, this.direct);
                }
                this.state = SwipeRecognitionState.IDLE;
                reset();
            }
        }

        public void progress(long id, long nanos, int x2, int y2) {
            if (this.state == SwipeRecognitionState.FAILURE) {
                return;
            }
            TouchPointTracker tracker = this.trackers.get(Long.valueOf(id));
            if (tracker == null) {
                this.state = SwipeRecognitionState.FAILURE;
                throw new RuntimeException("Error in swipe gesture recognition: reported unknown touch point");
            }
            tracker.progress(nanos, x2, y2);
        }

        void reset() {
            this.trackers.clear();
            this.cc.reset();
            this.state = SwipeRecognitionState.IDLE;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/SwipeGestureRecognizer$TouchPointTracker.class */
    private static class TouchPointTracker {
        long beginTime;
        long endTime;
        double beginX;
        double beginY;
        double endX;
        double endY;
        double beginAbsX;
        double beginAbsY;
        double endAbsX;
        double endAbsY;
        double lengthX;
        double lengthY;
        double maxDeviationX;
        double maxDeviationY;
        double lastXMovement;
        double lastYMovement;
        double lastX;
        double lastY;

        private TouchPointTracker() {
        }

        public void start(long nanos, double x2, double y2, double absX, double absY) {
            this.beginX = x2;
            this.beginY = y2;
            this.beginAbsX = absX;
            this.beginAbsY = absY;
            this.lastX = absX;
            this.lastY = absY;
            this.beginTime = nanos / 1000000;
        }

        public void end(long nanos, double x2, double y2, double absX, double absY) {
            progress(nanos, absX, absY);
            this.endX = x2;
            this.endY = y2;
            this.endAbsX = absX;
            this.endAbsY = absY;
            this.endTime = nanos / 1000000;
        }

        public void progress(long nanos, double x2, double y2) {
            double deltaX = x2 - this.lastX;
            double deltaY = y2 - this.lastY;
            this.lengthX += Math.abs(deltaX);
            this.lengthY += Math.abs(deltaY);
            this.lastX = x2;
            this.lastY = y2;
            double devX = Math.abs(x2 - this.beginAbsX);
            if (devX > this.maxDeviationX) {
                this.maxDeviationX = devX;
            }
            double devY = Math.abs(y2 - this.beginAbsY);
            if (devY > this.maxDeviationY) {
                this.maxDeviationY = devY;
            }
            if (Math.signum(deltaX) == Math.signum(this.lastXMovement)) {
                this.lastXMovement += deltaX;
            } else {
                this.lastXMovement = deltaX;
            }
            if (Math.signum(deltaY) == Math.signum(this.lastYMovement)) {
                this.lastYMovement += deltaY;
            } else {
                this.lastYMovement = deltaY;
            }
        }

        public double getDistanceX() {
            return this.endX - this.beginX;
        }

        public double getDistanceY() {
            return this.endY - this.beginY;
        }

        public long getDuration() {
            return this.endTime - this.beginTime;
        }
    }
}
