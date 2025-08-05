package com.sun.webkit.dom;

import org.w3c.dom.views.AbstractView;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/WheelEventImpl.class */
public class WheelEventImpl extends MouseEventImpl {
    public static final int DOM_DELTA_PIXEL = 0;
    public static final int DOM_DELTA_LINE = 1;
    public static final int DOM_DELTA_PAGE = 2;

    static native double getDeltaXImpl(long j2);

    static native double getDeltaYImpl(long j2);

    static native double getDeltaZImpl(long j2);

    static native int getDeltaModeImpl(long j2);

    static native int getWheelDeltaXImpl(long j2);

    static native int getWheelDeltaYImpl(long j2);

    static native int getWheelDeltaImpl(long j2);

    static native boolean getWebkitDirectionInvertedFromDeviceImpl(long j2);

    static native void initWheelEventImpl(long j2, int i2, int i3, long j3, int i4, int i5, int i6, int i7, boolean z2, boolean z3, boolean z4, boolean z5);

    WheelEventImpl(long peer) {
        super(peer);
    }

    static WheelEventImpl getImpl(long peer) {
        return (WheelEventImpl) create(peer);
    }

    public double getDeltaX() {
        return getDeltaXImpl(getPeer());
    }

    public double getDeltaY() {
        return getDeltaYImpl(getPeer());
    }

    public double getDeltaZ() {
        return getDeltaZImpl(getPeer());
    }

    public int getDeltaMode() {
        return getDeltaModeImpl(getPeer());
    }

    public int getWheelDeltaX() {
        return getWheelDeltaXImpl(getPeer());
    }

    public int getWheelDeltaY() {
        return getWheelDeltaYImpl(getPeer());
    }

    public int getWheelDelta() {
        return getWheelDeltaImpl(getPeer());
    }

    public boolean getWebkitDirectionInvertedFromDevice() {
        return getWebkitDirectionInvertedFromDeviceImpl(getPeer());
    }

    public void initWheelEvent(int wheelDeltaX, int wheelDeltaY, AbstractView view, int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey) {
        initWheelEventImpl(getPeer(), wheelDeltaX, wheelDeltaY, DOMWindowImpl.getPeer(view), screenX, screenY, clientX, clientY, ctrlKey, altKey, shiftKey, metaKey);
    }
}
