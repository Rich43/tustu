package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLParagraphElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLParagraphElementImpl.class */
public class HTMLParagraphElementImpl extends HTMLElementImpl implements HTMLParagraphElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    HTMLParagraphElementImpl(long peer) {
        super(peer);
    }

    static HTMLParagraphElement getImpl(long peer) {
        return (HTMLParagraphElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLParagraphElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLParagraphElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }
}
