package com.sun.glass.ui.win;

import com.sun.glass.ui.GestureSupport;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.glass.ui.View;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinGestureSupport.class */
final class WinGestureSupport {
    private static final double multiplier = 1.0d;
    private static final GestureSupport gestures;
    private static final TouchInputSupport touches;
    private static int modifiers;
    private static boolean isDirect;

    private static native void _initIDs();

    WinGestureSupport() {
    }

    static {
        _initIDs();
        gestures = new GestureSupport(true);
        touches = new TouchInputSupport(gestures.createTouchCountListener(), true);
    }

    public static void notifyBeginTouchEvent(View view, int modifiers2, boolean isDirect2, int touchEventCount) {
        touches.notifyBeginTouchEvent(view, modifiers2, isDirect2, touchEventCount);
    }

    public static void notifyNextTouchEvent(View view, int state, long id, int x2, int y2, int xAbs, int yAbs) {
        touches.notifyNextTouchEvent(view, state, id, x2, y2, xAbs, yAbs);
    }

    public static void notifyEndTouchEvent(View view) {
        touches.notifyEndTouchEvent(view);
        gestureFinished(view, touches.getTouchCount(), false);
    }

    private static void gestureFinished(View view, int touchCount, boolean isInertia) {
        if (gestures.isScrolling() && touchCount == 0) {
            gestures.handleScrollingEnd(view, modifiers, touchCount, isDirect, isInertia, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        if (gestures.isRotating() && touchCount < 2) {
            gestures.handleRotationEnd(view, modifiers, isDirect, isInertia, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        if (gestures.isZooming() && touchCount < 2) {
            gestures.handleZoomingEnd(view, modifiers, isDirect, isInertia, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
    }

    public static void inertiaGestureFinished(View view) {
        gestureFinished(view, 0, true);
    }

    public static void gesturePerformed(View view, int modifiers2, boolean isDirect2, boolean isInertia, int x2, int y2, int xAbs, int yAbs, float dx, float dy, float totaldx, float totaldy, float totalscale, float totalexpansion, float totalrotation) {
        modifiers = modifiers2;
        isDirect = isDirect2;
        int touchCount = touches.getTouchCount();
        if (touchCount >= 2) {
            gestures.handleTotalZooming(view, modifiers2, isDirect2, isInertia, x2, y2, xAbs, yAbs, totalscale, totalexpansion);
            gestures.handleTotalRotation(view, modifiers2, isDirect2, isInertia, x2, y2, xAbs, yAbs, Math.toDegrees(totalrotation));
        }
        gestures.handleTotalScrolling(view, modifiers2, isDirect2, isInertia, touchCount, x2, y2, xAbs, yAbs, totaldx, totaldy, 1.0d, 1.0d);
    }
}
