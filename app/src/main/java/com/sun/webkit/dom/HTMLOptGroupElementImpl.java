package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLOptGroupElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLOptGroupElementImpl.class */
public class HTMLOptGroupElementImpl extends HTMLElementImpl implements HTMLOptGroupElement {
    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native String getLabelImpl(long j2);

    static native void setLabelImpl(long j2, String str);

    HTMLOptGroupElementImpl(long peer) {
        super(peer);
    }

    static HTMLOptGroupElement getImpl(long peer) {
        return (HTMLOptGroupElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLOptGroupElement
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOptGroupElement
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLOptGroupElement
    public String getLabel() {
        return getLabelImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOptGroupElement
    public void setLabel(String value) {
        setLabelImpl(getPeer(), value);
    }
}
