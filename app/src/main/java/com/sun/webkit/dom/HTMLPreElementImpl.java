package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLPreElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLPreElementImpl.class */
public class HTMLPreElementImpl extends HTMLElementImpl implements HTMLPreElement {
    static native int getWidthImpl(long j2);

    static native void setWidthImpl(long j2, int i2);

    static native boolean getWrapImpl(long j2);

    static native void setWrapImpl(long j2, boolean z2);

    HTMLPreElementImpl(long peer) {
        super(peer);
    }

    static HTMLPreElement getImpl(long peer) {
        return (HTMLPreElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLPreElement
    public int getWidth() {
        return getWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLPreElement
    public void setWidth(int value) {
        setWidthImpl(getPeer(), value);
    }

    public boolean getWrap() {
        return getWrapImpl(getPeer());
    }

    public void setWrap(boolean value) {
        setWrapImpl(getPeer(), value);
    }
}
