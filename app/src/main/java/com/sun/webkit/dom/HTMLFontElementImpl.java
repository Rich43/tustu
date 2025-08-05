package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLFontElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLFontElementImpl.class */
public class HTMLFontElementImpl extends HTMLElementImpl implements HTMLFontElement {
    static native String getColorImpl(long j2);

    static native void setColorImpl(long j2, String str);

    static native String getFaceImpl(long j2);

    static native void setFaceImpl(long j2, String str);

    static native String getSizeImpl(long j2);

    static native void setSizeImpl(long j2, String str);

    HTMLFontElementImpl(long peer) {
        super(peer);
    }

    static HTMLFontElement getImpl(long peer) {
        return (HTMLFontElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLFontElement
    public String getColor() {
        return getColorImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFontElement
    public void setColor(String value) {
        setColorImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFontElement
    public String getFace() {
        return getFaceImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFontElement
    public void setFace(String value) {
        setFaceImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFontElement
    public String getSize() {
        return getSizeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFontElement
    public void setSize(String value) {
        setSizeImpl(getPeer(), value);
    }
}
