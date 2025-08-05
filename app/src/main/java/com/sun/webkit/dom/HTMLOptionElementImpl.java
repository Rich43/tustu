package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLOptionElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLOptionElementImpl.class */
public class HTMLOptionElementImpl extends HTMLElementImpl implements HTMLOptionElement {
    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native long getFormImpl(long j2);

    static native String getLabelImpl(long j2);

    static native void setLabelImpl(long j2, String str);

    static native boolean getDefaultSelectedImpl(long j2);

    static native void setDefaultSelectedImpl(long j2, boolean z2);

    static native boolean getSelectedImpl(long j2);

    static native void setSelectedImpl(long j2, boolean z2);

    static native String getValueImpl(long j2);

    static native void setValueImpl(long j2, String str);

    static native String getTextImpl(long j2);

    static native int getIndexImpl(long j2);

    HTMLOptionElementImpl(long peer) {
        super(peer);
    }

    static HTMLOptionElement getImpl(long peer) {
        return (HTMLOptionElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public String getLabel() {
        return getLabelImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public void setLabel(String value) {
        setLabelImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public boolean getDefaultSelected() {
        return getDefaultSelectedImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public void setDefaultSelected(boolean value) {
        setDefaultSelectedImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public boolean getSelected() {
        return getSelectedImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public void setSelected(boolean value) {
        setSelectedImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public String getValue() {
        return getValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public void setValue(String value) {
        setValueImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public String getText() {
        return getTextImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLOptionElement
    public int getIndex() {
        return getIndexImpl(getPeer());
    }
}
