package com.sun.glass.ui;

import com.sun.glass.events.TouchEvent;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/glass/ui/TouchInputSupport.class */
public class TouchInputSupport {
    private int touchCount = 0;
    private boolean filterTouchCoordinates;
    private Map<Long, TouchCoord> touch;
    private TouchCountListener listener;
    private int curTouchCount;
    private View curView;
    private int curModifiers;
    private boolean curIsDirect;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/TouchInputSupport$TouchCountListener.class */
    public interface TouchCountListener {
        void touchCountChanged(TouchInputSupport touchInputSupport, View view, int i2, boolean z2);
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/TouchInputSupport$TouchCoord.class */
    private static class TouchCoord {

        /* renamed from: x, reason: collision with root package name */
        private final int f11829x;

        /* renamed from: y, reason: collision with root package name */
        private final int f11830y;
        private final int xAbs;
        private final int yAbs;

        private TouchCoord(int x2, int y2, int xAbs, int yAbs) {
            this.f11829x = x2;
            this.f11830y = y2;
            this.xAbs = xAbs;
            this.yAbs = yAbs;
        }
    }

    public TouchInputSupport(TouchCountListener listener, boolean filterTouchCoordinates) {
        Application.checkEventThread();
        this.listener = listener;
        this.filterTouchCoordinates = filterTouchCoordinates;
        if (filterTouchCoordinates) {
            this.touch = new HashMap();
        }
    }

    public int getTouchCount() {
        Application.checkEventThread();
        return this.touchCount;
    }

    public void notifyBeginTouchEvent(View view, int modifiers, boolean isDirect, int touchEventCount) {
        if (this.curView != null && view != this.curView && this.touchCount != 0 && this.touch != null) {
            if (!this.curView.isClosed()) {
                this.curView.notifyBeginTouchEvent(0, true, this.touchCount);
                for (Map.Entry<Long, TouchCoord> e2 : this.touch.entrySet()) {
                    TouchCoord coord = e2.getValue();
                    this.curView.notifyNextTouchEvent(TouchEvent.TOUCH_RELEASED, e2.getKey().longValue(), coord.f11829x, coord.f11830y, coord.xAbs, coord.yAbs);
                }
                this.curView.notifyEndTouchEvent();
            }
            this.touch.clear();
            this.touchCount = 0;
            if (this.listener != null) {
                this.listener.touchCountChanged(this, this.curView, 0, true);
            }
        }
        this.curTouchCount = this.touchCount;
        this.curView = view;
        this.curModifiers = modifiers;
        this.curIsDirect = isDirect;
        if (view != null) {
            view.notifyBeginTouchEvent(modifiers, isDirect, touchEventCount);
        }
    }

    public void notifyEndTouchEvent(View view) {
        if (view == null) {
            return;
        }
        view.notifyEndTouchEvent();
        if (this.curTouchCount != 0 && this.touchCount != 0 && this.curTouchCount != this.touchCount && this.listener != null) {
            this.listener.touchCountChanged(this, this.curView, this.curModifiers, this.curIsDirect);
        }
    }

    public void notifyNextTouchEvent(View view, int state, long id, int x2, int y2, int xAbs, int yAbs) {
        switch (state) {
            case TouchEvent.TOUCH_PRESSED /* 811 */:
                this.touchCount++;
                break;
            case TouchEvent.TOUCH_MOVED /* 812 */:
            case TouchEvent.TOUCH_STILL /* 814 */:
                break;
            case TouchEvent.TOUCH_RELEASED /* 813 */:
                this.touchCount--;
                break;
            default:
                System.err.println("Unknown touch state: " + state);
                return;
        }
        if (this.filterTouchCoordinates) {
            state = filterTouchInputState(state, id, x2, y2, xAbs, yAbs);
        }
        if (view != null) {
            view.notifyNextTouchEvent(state, id, x2, y2, xAbs, yAbs);
        }
    }

    private int filterTouchInputState(int state, long id, int x2, int y2, int xAbs, int yAbs) {
        switch (state) {
            case TouchEvent.TOUCH_MOVED /* 812 */:
                TouchCoord c2 = this.touch.get(Long.valueOf(id));
                if (x2 == c2.f11829x && y2 == c2.f11830y) {
                    state = 814;
                    break;
                }
                break;
            case TouchEvent.TOUCH_PRESSED /* 811 */:
                this.touch.put(Long.valueOf(id), new TouchCoord(x2, y2, xAbs, yAbs));
                break;
            case TouchEvent.TOUCH_RELEASED /* 813 */:
                this.touch.remove(Long.valueOf(id));
                break;
            case TouchEvent.TOUCH_STILL /* 814 */:
                break;
            default:
                System.err.println("Unknown touch state: " + state);
                break;
        }
        return state;
    }
}
