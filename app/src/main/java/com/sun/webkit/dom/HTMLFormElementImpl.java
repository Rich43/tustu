package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFormElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLFormElementImpl.class */
public class HTMLFormElementImpl extends HTMLElementImpl implements HTMLFormElement {
    static native String getAcceptCharsetImpl(long j2);

    static native void setAcceptCharsetImpl(long j2, String str);

    static native String getActionImpl(long j2);

    static native void setActionImpl(long j2, String str);

    static native String getAutocompleteImpl(long j2);

    static native void setAutocompleteImpl(long j2, String str);

    static native String getEnctypeImpl(long j2);

    static native void setEnctypeImpl(long j2, String str);

    static native String getEncodingImpl(long j2);

    static native void setEncodingImpl(long j2, String str);

    static native String getMethodImpl(long j2);

    static native void setMethodImpl(long j2, String str);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native boolean getNoValidateImpl(long j2);

    static native void setNoValidateImpl(long j2, boolean z2);

    static native String getTargetImpl(long j2);

    static native void setTargetImpl(long j2, String str);

    static native long getElementsImpl(long j2);

    static native int getLengthImpl(long j2);

    static native void submitImpl(long j2);

    static native void resetImpl(long j2);

    static native boolean checkValidityImpl(long j2);

    HTMLFormElementImpl(long peer) {
        super(peer);
    }

    static HTMLFormElement getImpl(long peer) {
        return (HTMLFormElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public String getAcceptCharset() {
        return getAcceptCharsetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public void setAcceptCharset(String value) {
        setAcceptCharsetImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public String getAction() {
        return getActionImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public void setAction(String value) {
        setActionImpl(getPeer(), value);
    }

    public String getAutocomplete() {
        return getAutocompleteImpl(getPeer());
    }

    public void setAutocomplete(String value) {
        setAutocompleteImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public String getEnctype() {
        return getEnctypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public void setEnctype(String value) {
        setEnctypeImpl(getPeer(), value);
    }

    public String getEncoding() {
        return getEncodingImpl(getPeer());
    }

    public void setEncoding(String value) {
        setEncodingImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public String getMethod() {
        return getMethodImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public void setMethod(String value) {
        setMethodImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    public boolean getNoValidate() {
        return getNoValidateImpl(getPeer());
    }

    public void setNoValidate(boolean value) {
        setNoValidateImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public String getTarget() {
        return getTargetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public void setTarget(String value) {
        setTargetImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public HTMLCollection getElements() {
        return HTMLCollectionImpl.getImpl(getElementsImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public void submit() {
        submitImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFormElement
    public void reset() {
        resetImpl(getPeer());
    }

    public boolean checkValidity() {
        return checkValidityImpl(getPeer());
    }
}
