package com.sun.glass.ui;

import com.sun.glass.ui.TouchInputSupport;

/* loaded from: jfxrt.jar:com/sun/glass/ui/GestureSupport.class */
public final class GestureSupport {
    private static final double THRESHOLD_SCROLL = 1.0d;
    private static final double THRESHOLD_SCALE = 0.01d;
    private static final double THRESHOLD_EXPANSION = 0.01d;
    private static final double THRESHOLD_ROTATE = Math.toDegrees(0.017453292519943295d);
    private final GestureState scrolling = new GestureState();
    private final GestureState rotating = new GestureState();
    private final GestureState zooming = new GestureState();
    private final GestureState swiping = new GestureState();
    private double totalScrollX = Double.NaN;
    private double totalScrollY = Double.NaN;
    private double totalScale = 1.0d;
    private double totalExpansion = Double.NaN;
    private double totalRotation = 0.0d;
    private double multiplierX = 1.0d;
    private double multiplierY = 1.0d;
    private boolean zoomWithExpansion;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/GestureSupport$GestureState.class */
    private static class GestureState {
        private StateId id;

        /* loaded from: jfxrt.jar:com/sun/glass/ui/GestureSupport$GestureState$StateId.class */
        enum StateId {
            Idle,
            Running,
            Inertia
        }

        private GestureState() {
            this.id = StateId.Idle;
        }

        void setIdle() {
            this.id = StateId.Idle;
        }

        boolean isIdle() {
            return this.id == StateId.Idle;
        }

        int updateProgress(boolean isInertia) {
            int eventID = 2;
            if (doesGestureStart(isInertia) && !isInertia) {
                eventID = 1;
            }
            this.id = isInertia ? StateId.Inertia : StateId.Running;
            return eventID;
        }

        boolean doesGestureStart(boolean isInertia) {
            switch (this.id) {
                case Running:
                    return isInertia;
                case Inertia:
                    return !isInertia;
                default:
                    return true;
            }
        }
    }

    public GestureSupport(boolean zoomWithExpansion) {
        this.zoomWithExpansion = zoomWithExpansion;
    }

    private static double multiplicativeDelta(double from, double to) {
        if (from == 0.0d) {
            return Double.NaN;
        }
        return to / from;
    }

    private int setScrolling(boolean isInertia) {
        return this.scrolling.updateProgress(isInertia);
    }

    private int setRotating(boolean isInertia) {
        return this.rotating.updateProgress(isInertia);
    }

    private int setZooming(boolean isInertia) {
        return this.zooming.updateProgress(isInertia);
    }

    private int setSwiping(boolean isInertia) {
        return this.swiping.updateProgress(isInertia);
    }

    public boolean isScrolling() {
        return !this.scrolling.isIdle();
    }

    public boolean isRotating() {
        return !this.rotating.isIdle();
    }

    public boolean isZooming() {
        return !this.zooming.isIdle();
    }

    public boolean isSwiping() {
        return !this.swiping.isIdle();
    }

    public void handleScrollingEnd(View view, int modifiers, int touchCount, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs) {
        this.scrolling.setIdle();
        if (isInertia) {
            return;
        }
        view.notifyScrollGestureEvent(3, modifiers, isDirect, isInertia, touchCount, x2, y2, xAbs, yAbs, 0.0d, 0.0d, this.totalScrollX, this.totalScrollY, this.multiplierX, this.multiplierY);
    }

    public void handleRotationEnd(View view, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs) {
        this.rotating.setIdle();
        if (isInertia) {
            return;
        }
        view.notifyRotateGestureEvent(3, modifiers, isDirect, isInertia, x2, y2, xAbs, yAbs, 0.0d, this.totalRotation);
    }

    public void handleZoomingEnd(View view, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs) {
        this.zooming.setIdle();
        if (isInertia) {
            return;
        }
        view.notifyZoomGestureEvent(3, modifiers, isDirect, isInertia, x2, y2, xAbs, yAbs, Double.NaN, 0.0d, this.totalScale, this.totalExpansion);
    }

    public void handleSwipeEnd(View view, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs) {
        this.swiping.setIdle();
        if (isInertia) {
            return;
        }
        view.notifySwipeGestureEvent(3, modifiers, isDirect, isInertia, Integer.MAX_VALUE, Integer.MAX_VALUE, x2, y2, xAbs, yAbs);
    }

    public void handleTotalZooming(View view, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs, double scale, double expansion) {
        double baseScale = this.totalScale;
        double baseExpansion = this.totalExpansion;
        if (this.zooming.doesGestureStart(isInertia)) {
            baseScale = 1.0d;
            baseExpansion = 0.0d;
        }
        if (Math.abs(scale - baseScale) < 0.01d && (!this.zoomWithExpansion || Math.abs(expansion - baseExpansion) < 0.01d)) {
            return;
        }
        double deltaExpansion = Double.NaN;
        if (this.zoomWithExpansion) {
            deltaExpansion = expansion - baseExpansion;
        } else {
            expansion = Double.NaN;
        }
        this.totalScale = scale;
        this.totalExpansion = expansion;
        int eventID = setZooming(isInertia);
        view.notifyZoomGestureEvent(eventID, modifiers, isDirect, isInertia, x2, y2, xAbs, yAbs, multiplicativeDelta(baseScale, this.totalScale), deltaExpansion, scale, expansion);
    }

    public void handleTotalRotation(View view, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs, double rotation) {
        double baseRotation = this.totalRotation;
        if (this.rotating.doesGestureStart(isInertia)) {
            baseRotation = 0.0d;
        }
        if (Math.abs(rotation - baseRotation) < THRESHOLD_ROTATE) {
            return;
        }
        this.totalRotation = rotation;
        int eventID = setRotating(isInertia);
        view.notifyRotateGestureEvent(eventID, modifiers, isDirect, isInertia, x2, y2, xAbs, yAbs, rotation - baseRotation, rotation);
    }

    public void handleTotalScrolling(View view, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int x2, int y2, int xAbs, int yAbs, double dx, double dy, double multiplierX, double multiplierY) {
        this.multiplierX = multiplierX;
        this.multiplierY = multiplierY;
        double baseScrollX = this.totalScrollX;
        double baseScrollY = this.totalScrollY;
        if (this.scrolling.doesGestureStart(isInertia)) {
            baseScrollX = 0.0d;
            baseScrollY = 0.0d;
        }
        if (Math.abs(dx - this.totalScrollX) < 1.0d && Math.abs(dy - this.totalScrollY) < 1.0d) {
            return;
        }
        this.totalScrollX = dx;
        this.totalScrollY = dy;
        int eventID = setScrolling(isInertia);
        view.notifyScrollGestureEvent(eventID, modifiers, isDirect, isInertia, touchCount, x2, y2, xAbs, yAbs, dx - baseScrollX, dy - baseScrollY, dx, dy, multiplierX, multiplierY);
    }

    public void handleDeltaZooming(View view, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs, double scale, double expansion) {
        double baseScale = this.totalScale;
        double baseExpansion = this.totalExpansion;
        if (this.zooming.doesGestureStart(isInertia)) {
            baseScale = 1.0d;
            baseExpansion = 0.0d;
        }
        this.totalScale = baseScale * (1.0d + scale);
        if (this.zoomWithExpansion) {
            this.totalExpansion = baseExpansion + expansion;
        } else {
            this.totalExpansion = Double.NaN;
        }
        int eventID = setZooming(isInertia);
        view.notifyZoomGestureEvent(eventID, modifiers, isDirect, isInertia, x2, y2, xAbs, yAbs, multiplicativeDelta(baseScale, this.totalScale), expansion, this.totalScale, this.totalExpansion);
    }

    public void handleDeltaRotation(View view, int modifiers, boolean isDirect, boolean isInertia, int x2, int y2, int xAbs, int yAbs, double rotation) {
        double baseRotation = this.totalRotation;
        if (this.rotating.doesGestureStart(isInertia)) {
            baseRotation = 0.0d;
        }
        this.totalRotation = baseRotation + rotation;
        int eventID = setRotating(isInertia);
        view.notifyRotateGestureEvent(eventID, modifiers, isDirect, isInertia, x2, y2, xAbs, yAbs, rotation, this.totalRotation);
    }

    public void handleDeltaScrolling(View view, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int x2, int y2, int xAbs, int yAbs, double dx, double dy, double multiplierX, double multiplierY) {
        this.multiplierX = multiplierX;
        this.multiplierY = multiplierY;
        double baseScrollX = this.totalScrollX;
        double baseScrollY = this.totalScrollY;
        if (this.scrolling.doesGestureStart(isInertia)) {
            baseScrollX = 0.0d;
            baseScrollY = 0.0d;
        }
        this.totalScrollX = baseScrollX + dx;
        this.totalScrollY = baseScrollY + dy;
        int eventID = setScrolling(isInertia);
        view.notifyScrollGestureEvent(eventID, modifiers, isDirect, isInertia, touchCount, x2, y2, xAbs, yAbs, dx, dy, this.totalScrollX, this.totalScrollY, multiplierX, multiplierY);
    }

    public void handleSwipe(View view, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int dir, int x2, int y2, int xAbs, int yAbs) {
        int eventID = setSwiping(isInertia);
        view.notifySwipeGestureEvent(eventID, modifiers, isDirect, isInertia, touchCount, dir, x2, y2, xAbs, yAbs);
    }

    public static void handleSwipePerformed(View view, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int dir, int x2, int y2, int xAbs, int yAbs) {
        view.notifySwipeGestureEvent(2, modifiers, isDirect, isInertia, touchCount, dir, x2, y2, xAbs, yAbs);
    }

    public static void handleScrollingPerformed(View view, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int x2, int y2, int xAbs, int yAbs, double dx, double dy, double multiplierX, double multiplierY) {
        view.notifyScrollGestureEvent(2, modifiers, isDirect, isInertia, touchCount, x2, y2, xAbs, yAbs, dx, dy, dx, dy, multiplierX, multiplierY);
    }

    public TouchInputSupport.TouchCountListener createTouchCountListener() {
        Application.checkEventThread();
        return (sender, view, modifiers, isDirect) -> {
            if (isScrolling()) {
                handleScrollingEnd(view, modifiers, sender.getTouchCount(), isDirect, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            if (isRotating()) {
                handleRotationEnd(view, modifiers, isDirect, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            if (isZooming()) {
                handleZoomingEnd(view, modifiers, isDirect, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        };
    }
}
