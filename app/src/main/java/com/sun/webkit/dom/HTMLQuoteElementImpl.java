package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLQuoteElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLQuoteElementImpl.class */
public class HTMLQuoteElementImpl extends HTMLElementImpl implements HTMLQuoteElement {
    static native String getCiteImpl(long j2);

    static native void setCiteImpl(long j2, String str);

    HTMLQuoteElementImpl(long peer) {
        super(peer);
    }

    static HTMLQuoteElement getImpl(long peer) {
        return (HTMLQuoteElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLQuoteElement
    public String getCite() {
        return getCiteImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLQuoteElement
    public void setCite(String value) {
        setCiteImpl(getPeer(), value);
    }
}
