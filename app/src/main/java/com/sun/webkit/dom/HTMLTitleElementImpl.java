package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLTitleElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLTitleElementImpl.class */
public class HTMLTitleElementImpl extends HTMLElementImpl implements HTMLTitleElement {
    static native String getTextImpl(long j2);

    static native void setTextImpl(long j2, String str);

    HTMLTitleElementImpl(long peer) {
        super(peer);
    }

    static HTMLTitleElement getImpl(long peer) {
        return (HTMLTitleElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLTitleElement
    public String getText() {
        return getTextImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTitleElement
    public void setText(String value) {
        setTextImpl(getPeer(), value);
    }
}
