package com.sun.webkit.dom;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLFormElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLButtonElementImpl.class */
public class HTMLButtonElementImpl extends HTMLElementImpl implements HTMLButtonElement {
    static native boolean getAutofocusImpl(long j2);

    static native void setAutofocusImpl(long j2, boolean z2);

    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native long getFormImpl(long j2);

    static native String getFormActionImpl(long j2);

    static native void setFormActionImpl(long j2, String str);

    static native String getFormEnctypeImpl(long j2);

    static native void setFormEnctypeImpl(long j2, String str);

    static native String getFormMethodImpl(long j2);

    static native void setFormMethodImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native boolean getFormNoValidateImpl(long j2);

    static native void setFormNoValidateImpl(long j2, boolean z2);

    static native String getFormTargetImpl(long j2);

    static native void setFormTargetImpl(long j2, String str);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getValueImpl(long j2);

    static native void setValueImpl(long j2, String str);

    static native boolean getWillValidateImpl(long j2);

    static native String getValidationMessageImpl(long j2);

    static native long getLabelsImpl(long j2);

    static native String getAccessKeyImpl(long j2);

    static native void setAccessKeyImpl(long j2, String str);

    static native boolean checkValidityImpl(long j2);

    static native void setCustomValidityImpl(long j2, String str);

    static native void clickImpl(long j2);

    HTMLButtonElementImpl(long peer) {
        super(peer);
    }

    static HTMLButtonElement getImpl(long peer) {
        return (HTMLButtonElement) create(peer);
    }

    public boolean getAutofocus() {
        return getAutofocusImpl(getPeer());
    }

    public void setAutofocus(boolean value) {
        setAutofocusImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLButtonElement
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLButtonElement
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLButtonElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    public String getFormAction() {
        return getFormActionImpl(getPeer());
    }

    public void setFormAction(String value) {
        setFormActionImpl(getPeer(), value);
    }

    public String getFormEnctype() {
        return getFormEnctypeImpl(getPeer());
    }

    public void setFormEnctype(String value) {
        setFormEnctypeImpl(getPeer(), value);
    }

    public String getFormMethod() {
        return getFormMethodImpl(getPeer());
    }

    public void setFormMethod(String value) {
        setFormMethodImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLButtonElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    public boolean getFormNoValidate() {
        return getFormNoValidateImpl(getPeer());
    }

    public void setFormNoValidate(boolean value) {
        setFormNoValidateImpl(getPeer(), value);
    }

    public String getFormTarget() {
        return getFormTargetImpl(getPeer());
    }

    public void setFormTarget(String value) {
        setFormTargetImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLButtonElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLButtonElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLButtonElement
    public String getValue() {
        return getValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLButtonElement
    public void setValue(String value) {
        setValueImpl(getPeer(), value);
    }

    public boolean getWillValidate() {
        return getWillValidateImpl(getPeer());
    }

    public String getValidationMessage() {
        return getValidationMessageImpl(getPeer());
    }

    public NodeList getLabels() {
        return NodeListImpl.getImpl(getLabelsImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public String getAccessKey() {
        return getAccessKeyImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public void setAccessKey(String value) {
        setAccessKeyImpl(getPeer(), value);
    }

    public boolean checkValidity() {
        return checkValidityImpl(getPeer());
    }

    public void setCustomValidity(String error) {
        setCustomValidityImpl(getPeer(), error);
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl
    public void click() {
        clickImpl(getPeer());
    }
}
