package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLUListElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLUListElementImpl.class */
public class HTMLUListElementImpl extends HTMLElementImpl implements HTMLUListElement {
    static native boolean getCompactImpl(long j2);

    static native void setCompactImpl(long j2, boolean z2);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    HTMLUListElementImpl(long peer) {
        super(peer);
    }

    static HTMLUListElement getImpl(long peer) {
        return (HTMLUListElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLUListElement
    public boolean getCompact() {
        return getCompactImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLUListElement
    public void setCompact(boolean value) {
        setCompactImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLUListElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLUListElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }
}
