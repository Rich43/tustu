package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLTableCaptionElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLTableCaptionElementImpl.class */
public class HTMLTableCaptionElementImpl extends HTMLElementImpl implements HTMLTableCaptionElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    HTMLTableCaptionElementImpl(long peer) {
        super(peer);
    }

    static HTMLTableCaptionElement getImpl(long peer) {
        return (HTMLTableCaptionElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLTableCaptionElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCaptionElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }
}
