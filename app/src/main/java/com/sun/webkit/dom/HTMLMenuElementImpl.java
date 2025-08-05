package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLMenuElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLMenuElementImpl.class */
public class HTMLMenuElementImpl extends HTMLElementImpl implements HTMLMenuElement {
    static native boolean getCompactImpl(long j2);

    static native void setCompactImpl(long j2, boolean z2);

    HTMLMenuElementImpl(long peer) {
        super(peer);
    }

    static HTMLMenuElement getImpl(long peer) {
        return (HTMLMenuElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLMenuElement
    public boolean getCompact() {
        return getCompactImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLMenuElement
    public void setCompact(boolean value) {
        setCompactImpl(getPeer(), value);
    }
}
