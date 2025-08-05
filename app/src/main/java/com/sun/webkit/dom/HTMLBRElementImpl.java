package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLBRElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLBRElementImpl.class */
public class HTMLBRElementImpl extends HTMLElementImpl implements HTMLBRElement {
    static native String getClearImpl(long j2);

    static native void setClearImpl(long j2, String str);

    HTMLBRElementImpl(long peer) {
        super(peer);
    }

    static HTMLBRElement getImpl(long peer) {
        return (HTMLBRElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLBRElement
    public String getClear() {
        return getClearImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBRElement
    public void setClear(String value) {
        setClearImpl(getPeer(), value);
    }
}
