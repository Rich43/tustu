package com.sun.webkit.dom;

import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/MouseEventImpl.class */
public class MouseEventImpl extends UIEventImpl implements MouseEvent {
    static native int getScreenXImpl(long j2);

    static native int getScreenYImpl(long j2);

    static native int getClientXImpl(long j2);

    static native int getClientYImpl(long j2);

    static native boolean getCtrlKeyImpl(long j2);

    static native boolean getShiftKeyImpl(long j2);

    static native boolean getAltKeyImpl(long j2);

    static native boolean getMetaKeyImpl(long j2);

    static native short getButtonImpl(long j2);

    static native long getRelatedTargetImpl(long j2);

    static native int getOffsetXImpl(long j2);

    static native int getOffsetYImpl(long j2);

    static native int getXImpl(long j2);

    static native int getYImpl(long j2);

    static native long getFromElementImpl(long j2);

    static native long getToElementImpl(long j2);

    static native void initMouseEventImpl(long j2, String str, boolean z2, boolean z3, long j3, int i2, int i3, int i4, int i5, int i6, boolean z4, boolean z5, boolean z6, boolean z7, short s2, long j4);

    MouseEventImpl(long peer) {
        super(peer);
    }

    static MouseEvent getImpl(long peer) {
        return (MouseEvent) create(peer);
    }

    @Override // org.w3c.dom.events.MouseEvent
    public int getScreenX() {
        return getScreenXImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public int getScreenY() {
        return getScreenYImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public int getClientX() {
        return getClientXImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public int getClientY() {
        return getClientYImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public boolean getCtrlKey() {
        return getCtrlKeyImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public boolean getShiftKey() {
        return getShiftKeyImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public boolean getAltKey() {
        return getAltKeyImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public boolean getMetaKey() {
        return getMetaKeyImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public short getButton() {
        return getButtonImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MouseEvent
    public EventTarget getRelatedTarget() {
        return (EventTarget) NodeImpl.getImpl(getRelatedTargetImpl(getPeer()));
    }

    public int getOffsetX() {
        return getOffsetXImpl(getPeer());
    }

    public int getOffsetY() {
        return getOffsetYImpl(getPeer());
    }

    public int getX() {
        return getXImpl(getPeer());
    }

    public int getY() {
        return getYImpl(getPeer());
    }

    public Node getFromElement() {
        return NodeImpl.getImpl(getFromElementImpl(getPeer()));
    }

    public Node getToElement() {
        return NodeImpl.getImpl(getToElementImpl(getPeer()));
    }

    @Override // org.w3c.dom.events.MouseEvent
    public void initMouseEvent(String type, boolean canBubble, boolean cancelable, AbstractView view, int detail, int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey, short button, EventTarget relatedTarget) {
        initMouseEventImpl(getPeer(), type, canBubble, cancelable, DOMWindowImpl.getPeer(view), detail, screenX, screenY, clientX, clientY, ctrlKey, altKey, shiftKey, metaKey, button, NodeImpl.getPeer((NodeImpl) relatedTarget));
    }
}
