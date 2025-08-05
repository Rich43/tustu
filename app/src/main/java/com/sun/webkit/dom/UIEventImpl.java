package com.sun.webkit.dom;

import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/UIEventImpl.class */
public class UIEventImpl extends EventImpl implements UIEvent {
    static native long getViewImpl(long j2);

    static native int getDetailImpl(long j2);

    static native int getKeyCodeImpl(long j2);

    static native int getCharCodeImpl(long j2);

    static native int getLayerXImpl(long j2);

    static native int getLayerYImpl(long j2);

    static native int getPageXImpl(long j2);

    static native int getPageYImpl(long j2);

    static native int getWhichImpl(long j2);

    static native void initUIEventImpl(long j2, String str, boolean z2, boolean z3, long j3, int i2);

    UIEventImpl(long peer) {
        super(peer);
    }

    static UIEvent getImpl(long peer) {
        return (UIEvent) create(peer);
    }

    @Override // org.w3c.dom.events.UIEvent
    public AbstractView getView() {
        return DOMWindowImpl.getImpl(getViewImpl(getPeer()));
    }

    @Override // org.w3c.dom.events.UIEvent
    public int getDetail() {
        return getDetailImpl(getPeer());
    }

    public int getKeyCode() {
        return getKeyCodeImpl(getPeer());
    }

    public int getCharCode() {
        return getCharCodeImpl(getPeer());
    }

    public int getLayerX() {
        return getLayerXImpl(getPeer());
    }

    public int getLayerY() {
        return getLayerYImpl(getPeer());
    }

    public int getPageX() {
        return getPageXImpl(getPeer());
    }

    public int getPageY() {
        return getPageYImpl(getPeer());
    }

    public int getWhich() {
        return getWhichImpl(getPeer());
    }

    @Override // org.w3c.dom.events.UIEvent
    public void initUIEvent(String type, boolean canBubble, boolean cancelable, AbstractView view, int detail) {
        initUIEventImpl(getPeer(), type, canBubble, cancelable, DOMWindowImpl.getPeer(view), detail);
    }
}
