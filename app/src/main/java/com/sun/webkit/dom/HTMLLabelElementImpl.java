package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLLabelElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLLabelElementImpl.class */
public class HTMLLabelElementImpl extends HTMLElementImpl implements HTMLLabelElement {
    static native long getFormImpl(long j2);

    static native String getHtmlForImpl(long j2);

    static native void setHtmlForImpl(long j2, String str);

    static native long getControlImpl(long j2);

    static native String getAccessKeyImpl(long j2);

    static native void setAccessKeyImpl(long j2, String str);

    HTMLLabelElementImpl(long peer) {
        super(peer);
    }

    static HTMLLabelElement getImpl(long peer) {
        return (HTMLLabelElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLLabelElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLLabelElement
    public String getHtmlFor() {
        return getHtmlForImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLabelElement
    public void setHtmlFor(String value) {
        setHtmlForImpl(getPeer(), value);
    }

    public HTMLElement getControl() {
        return HTMLElementImpl.getImpl(getControlImpl(getPeer()));
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
