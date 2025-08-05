package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLOListElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLOListElementImpl.class */
public class HTMLOListElementImpl extends HTMLElementImpl implements HTMLOListElement {
    static native boolean getCompactImpl(long j2);

    static native void setCompactImpl(long j2, boolean z2);

    static native int getStartImpl(long j2);

    static native void setStartImpl(long j2, int i2);

    static native boolean getReversedImpl(long j2);

    static native void setReversedImpl(long j2, boolean z2);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    HTMLOListElementImpl(long peer) {
        super(peer);
    }

    static HTMLOListElement getImpl(long peer) {
        return (HTMLOListElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLOListElement
    public boolean getCompact() {
        return getCompactImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOListElement
    public void setCompact(boolean value) {
        setCompactImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLOListElement
    public int getStart() {
        return getStartImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOListElement
    public void setStart(int value) {
        setStartImpl(getPeer(), value);
    }

    public boolean getReversed() {
        return getReversedImpl(getPeer());
    }

    public void setReversed(boolean value) {
        setReversedImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLOListElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOListElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }
}
