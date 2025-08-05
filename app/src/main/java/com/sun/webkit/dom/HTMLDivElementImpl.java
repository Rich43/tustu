package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLDivElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLDivElementImpl.class */
public class HTMLDivElementImpl extends HTMLElementImpl implements HTMLDivElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    HTMLDivElementImpl(long peer) {
        super(peer);
    }

    static HTMLDivElement getImpl(long peer) {
        return (HTMLDivElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLDivElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLDivElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }
}
