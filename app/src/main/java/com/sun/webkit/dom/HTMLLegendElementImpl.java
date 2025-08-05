package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLLegendElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLLegendElementImpl.class */
public class HTMLLegendElementImpl extends HTMLElementImpl implements HTMLLegendElement {
    static native long getFormImpl(long j2);

    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getAccessKeyImpl(long j2);

    static native void setAccessKeyImpl(long j2, String str);

    HTMLLegendElementImpl(long peer) {
        super(peer);
    }

    static HTMLLegendElement getImpl(long peer) {
        return (HTMLLegendElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLLegendElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLLegendElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLegendElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public String getAccessKey() {
        return getAccessKeyImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public void setAccessKey(String value) {
        setAccessKeyImpl(getPeer(), value);
    }
}
