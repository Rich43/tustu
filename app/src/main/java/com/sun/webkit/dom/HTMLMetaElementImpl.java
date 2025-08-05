package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLMetaElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLMetaElementImpl.class */
public class HTMLMetaElementImpl extends HTMLElementImpl implements HTMLMetaElement {
    static native String getContentImpl(long j2);

    static native void setContentImpl(long j2, String str);

    static native String getHttpEquivImpl(long j2);

    static native void setHttpEquivImpl(long j2, String str);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getSchemeImpl(long j2);

    static native void setSchemeImpl(long j2, String str);

    HTMLMetaElementImpl(long peer) {
        super(peer);
    }

    static HTMLMetaElement getImpl(long peer) {
        return (HTMLMetaElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLMetaElement
    public String getContent() {
        return getContentImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLMetaElement
    public void setContent(String value) {
        setContentImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLMetaElement
    public String getHttpEquiv() {
        return getHttpEquivImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLMetaElement
    public void setHttpEquiv(String value) {
        setHttpEquivImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLMetaElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLMetaElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLMetaElement
    public String getScheme() {
        return getSchemeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLMetaElement
    public void setScheme(String value) {
        setSchemeImpl(getPeer(), value);
    }
}
