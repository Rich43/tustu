package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLHeadElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLHeadElementImpl.class */
public class HTMLHeadElementImpl extends HTMLElementImpl implements HTMLHeadElement {
    static native String getProfileImpl(long j2);

    static native void setProfileImpl(long j2, String str);

    HTMLHeadElementImpl(long peer) {
        super(peer);
    }

    static HTMLHeadElement getImpl(long peer) {
        return (HTMLHeadElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLHeadElement
    public String getProfile() {
        return getProfileImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLHeadElement
    public void setProfile(String value) {
        setProfileImpl(getPeer(), value);
    }
}
