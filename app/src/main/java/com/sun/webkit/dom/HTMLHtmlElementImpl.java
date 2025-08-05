package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLHtmlElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLHtmlElementImpl.class */
public class HTMLHtmlElementImpl extends HTMLElementImpl implements HTMLHtmlElement {
    static native String getVersionImpl(long j2);

    static native void setVersionImpl(long j2, String str);

    static native String getManifestImpl(long j2);

    static native void setManifestImpl(long j2, String str);

    HTMLHtmlElementImpl(long peer) {
        super(peer);
    }

    static HTMLHtmlElement getImpl(long peer) {
        return (HTMLHtmlElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLHtmlElement
    public String getVersion() {
        return getVersionImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLHtmlElement
    public void setVersion(String value) {
        setVersionImpl(getPeer(), value);
    }

    public String getManifest() {
        return getManifestImpl(getPeer());
    }

    public void setManifest(String value) {
        setManifestImpl(getPeer(), value);
    }
}
