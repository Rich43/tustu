package com.sun.webkit.dom;

import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLObjectElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLObjectElementImpl.class */
public class HTMLObjectElementImpl extends HTMLElementImpl implements HTMLObjectElement {
    static native long getFormImpl(long j2);

    static native String getCodeImpl(long j2);

    static native void setCodeImpl(long j2, String str);

    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getArchiveImpl(long j2);

    static native void setArchiveImpl(long j2, String str);

    static native String getBorderImpl(long j2);

    static native void setBorderImpl(long j2, String str);

    static native String getCodeBaseImpl(long j2);

    static native void setCodeBaseImpl(long j2, String str);

    static native String getCodeTypeImpl(long j2);

    static native void setCodeTypeImpl(long j2, String str);

    static native String getDataImpl(long j2);

    static native void setDataImpl(long j2, String str);

    static native boolean getDeclareImpl(long j2);

    static native void setDeclareImpl(long j2, boolean z2);

    static native String getHeightImpl(long j2);

    static native void setHeightImpl(long j2, String str);

    static native int getHspaceImpl(long j2);

    static native void setHspaceImpl(long j2, int i2);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getStandbyImpl(long j2);

    static native void setStandbyImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native String getUseMapImpl(long j2);

    static native void setUseMapImpl(long j2, String str);

    static native int getVspaceImpl(long j2);

    static native void setVspaceImpl(long j2, int i2);

    static native String getWidthImpl(long j2);

    static native void setWidthImpl(long j2, String str);

    static native boolean getWillValidateImpl(long j2);

    static native String getValidationMessageImpl(long j2);

    static native long getContentDocumentImpl(long j2);

    static native boolean checkValidityImpl(long j2);

    static native void setCustomValidityImpl(long j2, String str);

    HTMLObjectElementImpl(long peer) {
        super(peer);
    }

    static HTMLObjectElement getImpl(long peer) {
        return (HTMLObjectElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getCode() {
        return getCodeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setCode(String value) {
        setCodeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getArchive() {
        return getArchiveImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setArchive(String value) {
        setArchiveImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getBorder() {
        return getBorderImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setBorder(String value) {
        setBorderImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getCodeBase() {
        return getCodeBaseImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setCodeBase(String value) {
        setCodeBaseImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getCodeType() {
        return getCodeTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setCodeType(String value) {
        setCodeTypeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getData() {
        return getDataImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setData(String value) {
        setDataImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public boolean getDeclare() {
        return getDeclareImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setDeclare(boolean value) {
        setDeclareImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getHeight() {
        return getHeightImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setHeight(String value) {
        setHeightImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getHspace() {
        return getHspaceImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setHspace(String value) {
        setHspaceImpl(getPeer(), Integer.parseInt(value));
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getStandby() {
        return getStandbyImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setStandby(String value) {
        setStandbyImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getUseMap() {
        return getUseMapImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setUseMap(String value) {
        setUseMapImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getVspace() {
        return getVspaceImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setVspace(String value) {
        setVspaceImpl(getPeer(), Integer.parseInt(value));
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public String getWidth() {
        return getWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public void setWidth(String value) {
        setWidthImpl(getPeer(), value);
    }

    public boolean getWillValidate() {
        return getWillValidateImpl(getPeer());
    }

    public String getValidationMessage() {
        return getValidationMessageImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLObjectElement
    public Document getContentDocument() {
        return DocumentImpl.getImpl(getContentDocumentImpl(getPeer()));
    }

    public boolean checkValidity() {
        return checkValidityImpl(getPeer());
    }

    public void setCustomValidity(String error) {
        setCustomValidityImpl(getPeer(), error);
    }
}
