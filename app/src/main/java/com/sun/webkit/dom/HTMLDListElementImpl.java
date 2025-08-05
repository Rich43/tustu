package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLDListElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLDListElementImpl.class */
public class HTMLDListElementImpl extends HTMLElementImpl implements HTMLDListElement {
    static native boolean getCompactImpl(long j2);

    static native void setCompactImpl(long j2, boolean z2);

    HTMLDListElementImpl(long peer) {
        super(peer);
    }

    static HTMLDListElement getImpl(long peer) {
        return (HTMLDListElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLDListElement
    public boolean getCompact() {
        return getCompactImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLDListElement
    public void setCompact(boolean value) {
        setCompactImpl(getPeer(), value);
    }
}
