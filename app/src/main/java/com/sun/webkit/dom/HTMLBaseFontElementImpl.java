package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLBaseFontElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLBaseFontElementImpl.class */
public class HTMLBaseFontElementImpl extends HTMLElementImpl implements HTMLBaseFontElement {
    static native String getColorImpl(long j2);

    static native void setColorImpl(long j2, String str);

    static native String getFaceImpl(long j2);

    static native void setFaceImpl(long j2, String str);

    static native String getSizeImpl(long j2);

    static native void setSizeImpl(long j2, String str);

    HTMLBaseFontElementImpl(long peer) {
        super(peer);
    }

    static HTMLBaseFontElement getImpl(long peer) {
        return (HTMLBaseFontElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLBaseFontElement
    public String getColor() {
        return getColorImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBaseFontElement
    public void setColor(String value) {
        setColorImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLBaseFontElement
    public String getFace() {
        return getFaceImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBaseFontElement
    public void setFace(String value) {
        setFaceImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLBaseFontElement
    public String getSize() {
        return getSizeImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLBaseFontElement
    public void setSize(String value) {
        setSizeImpl(getPeer(), value);
    }
}
