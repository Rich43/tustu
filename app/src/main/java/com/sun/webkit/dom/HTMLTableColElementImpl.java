package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLTableColElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLTableColElementImpl.class */
public class HTMLTableColElementImpl extends HTMLElementImpl implements HTMLTableColElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getChImpl(long j2);

    static native void setChImpl(long j2, String str);

    static native String getChOffImpl(long j2);

    static native void setChOffImpl(long j2, String str);

    static native int getSpanImpl(long j2);

    static native void setSpanImpl(long j2, int i2);

    static native String getVAlignImpl(long j2);

    static native void setVAlignImpl(long j2, String str);

    static native String getWidthImpl(long j2);

    static native void setWidthImpl(long j2, String str);

    HTMLTableColElementImpl(long peer) {
        super(peer);
    }

    static HTMLTableColElement getImpl(long peer) {
        return (HTMLTableColElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public String getCh() {
        return getChImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public void setCh(String value) {
        setChImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public String getChOff() {
        return getChOffImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public void setChOff(String value) {
        setChOffImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public int getSpan() {
        return getSpanImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public void setSpan(int value) {
        setSpanImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public String getVAlign() {
        return getVAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public void setVAlign(String value) {
        setVAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public String getWidth() {
        return getWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableColElement
    public void setWidth(String value) {
        setWidthImpl(getPeer(), value);
    }
}
