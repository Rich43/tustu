package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLLIElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLLIElementImpl.class */
public class HTMLLIElementImpl extends HTMLElementImpl implements HTMLLIElement {
    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native int getValueImpl(long j2);

    static native void setValueImpl(long j2, int i2);

    HTMLLIElementImpl(long peer) {
        super(peer);
    }

    static HTMLLIElement getImpl(long peer) {
        return (HTMLLIElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLLIElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLIElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLIElement
    public int getValue() {
        return getValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLIElement
    public void setValue(int value) {
        setValueImpl(getPeer(), value);
    }
}
