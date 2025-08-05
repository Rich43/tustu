package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLModElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLModElementImpl.class */
public class HTMLModElementImpl extends HTMLElementImpl implements HTMLModElement {
    static native String getCiteImpl(long j2);

    static native void setCiteImpl(long j2, String str);

    static native String getDateTimeImpl(long j2);

    static native void setDateTimeImpl(long j2, String str);

    HTMLModElementImpl(long peer) {
        super(peer);
    }

    static HTMLModElement getImpl(long peer) {
        return (HTMLModElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLModElement
    public String getCite() {
        return getCiteImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLModElement
    public void setCite(String value) {
        setCiteImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLModElement
    public String getDateTime() {
        return getDateTimeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLModElement
    public void setDateTime(String value) {
        setDateTimeImpl(getPeer(), value);
    }
}
