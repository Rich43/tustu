package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLHeadingElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLHeadingElementImpl.class */
public class HTMLHeadingElementImpl extends HTMLElementImpl implements HTMLHeadingElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    HTMLHeadingElementImpl(long peer) {
        super(peer);
    }

    static HTMLHeadingElement getImpl(long peer) {
        return (HTMLHeadingElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLHeadingElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLHeadingElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }
}
