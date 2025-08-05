package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLBaseElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLBaseElementImpl.class */
public class HTMLBaseElementImpl extends HTMLElementImpl implements HTMLBaseElement {
    static native String getHrefImpl(long j2);

    static native void setHrefImpl(long j2, String str);

    static native String getTargetImpl(long j2);

    static native void setTargetImpl(long j2, String str);

    HTMLBaseElementImpl(long peer) {
        super(peer);
    }

    static HTMLBaseElement getImpl(long peer) {
        return (HTMLBaseElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLBaseElement
    public String getHref() {
        return getHrefImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBaseElement
    public void setHref(String value) {
        setHrefImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLBaseElement
    public String getTarget() {
        return getTargetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBaseElement
    public void setTarget(String value) {
        setTargetImpl(getPeer(), value);
    }
}
