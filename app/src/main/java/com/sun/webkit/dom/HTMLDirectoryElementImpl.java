package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLDirectoryElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLDirectoryElementImpl.class */
public class HTMLDirectoryElementImpl extends HTMLElementImpl implements HTMLDirectoryElement {
    static native boolean getCompactImpl(long j2);

    static native void setCompactImpl(long j2, boolean z2);

    HTMLDirectoryElementImpl(long peer) {
        super(peer);
    }

    static HTMLDirectoryElement getImpl(long peer) {
        return (HTMLDirectoryElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLDirectoryElement
    public boolean getCompact() {
        return getCompactImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLDirectoryElement
    public void setCompact(boolean value) {
        setCompactImpl(getPeer(), value);
    }
}
