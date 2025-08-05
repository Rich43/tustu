package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLFieldSetElement;
import org.w3c.dom.html.HTMLFormElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLFieldSetElementImpl.class */
public class HTMLFieldSetElementImpl extends HTMLElementImpl implements HTMLFieldSetElement {
    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native long getFormImpl(long j2);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native boolean getWillValidateImpl(long j2);

    static native String getValidationMessageImpl(long j2);

    static native boolean checkValidityImpl(long j2);

    static native void setCustomValidityImpl(long j2, String str);

    HTMLFieldSetElementImpl(long peer) {
        super(peer);
    }

    static HTMLFieldSetElement getImpl(long peer) {
        return (HTMLFieldSetElement) create(peer);
    }

    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFieldSetElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    public String getName() {
        return getNameImpl(getPeer());
    }

    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    public String getType() {
        return getTypeImpl(getPeer());
    }

    public boolean getWillValidate() {
        return getWillValidateImpl(getPeer());
    }

    public String getValidationMessage() {
        return getValidationMessageImpl(getPeer());
    }

    public boolean checkValidity() {
        return checkValidityImpl(getPeer());
    }

    public void setCustomValidity(String error) {
        setCustomValidityImpl(getPeer(), error);
    }
}
