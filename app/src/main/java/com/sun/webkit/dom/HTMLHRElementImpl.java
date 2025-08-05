package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLHRElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLHRElementImpl.class */
public class HTMLHRElementImpl extends HTMLElementImpl implements HTMLHRElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native boolean getNoShadeImpl(long j2);

    static native void setNoShadeImpl(long j2, boolean z2);

    static native String getSizeImpl(long j2);

    static native void setSizeImpl(long j2, String str);

    static native String getWidthImpl(long j2);

    static native void setWidthImpl(long j2, String str);

    HTMLHRElementImpl(long peer) {
        super(peer);
    }

    static HTMLHRElement getImpl(long peer) {
        return (HTMLHRElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLHRElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLHRElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLHRElement
    public boolean getNoShade() {
        return getNoShadeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLHRElement
    public void setNoShade(boolean value) {
        setNoShadeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLHRElement
    public String getSize() {
        return getSizeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLHRElement
    public void setSize(String value) {
        setSizeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLHRElement
    public String getWidth() {
        return getWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLHRElement
    public void setWidth(String value) {
        setWidthImpl(getPeer(), value);
    }
}
