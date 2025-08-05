package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLParamElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLParamElementImpl.class */
public class HTMLParamElementImpl extends HTMLElementImpl implements HTMLParamElement {
    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native String getValueImpl(long j2);

    static native void setValueImpl(long j2, String str);

    static native String getValueTypeImpl(long j2);

    static native void setValueTypeImpl(long j2, String str);

    HTMLParamElementImpl(long peer) {
        super(peer);
    }

    static HTMLParamElement getImpl(long peer) {
        return (HTMLParamElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLParamElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLParamElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLParamElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLParamElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLParamElement
    public String getValue() {
        return getValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLParamElement
    public void setValue(String value) {
        setValueImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLParamElement
    public String getValueType() {
        return getValueTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLParamElement
    public void setValueType(String value) {
        setValueTypeImpl(getPeer(), value);
    }
}
