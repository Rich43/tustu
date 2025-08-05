package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLAppletElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLAppletElementImpl.class */
public class HTMLAppletElementImpl extends HTMLElementImpl implements HTMLAppletElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getAltImpl(long j2);

    static native void setAltImpl(long j2, String str);

    static native String getArchiveImpl(long j2);

    static native void setArchiveImpl(long j2, String str);

    static native String getCodeImpl(long j2);

    static native void setCodeImpl(long j2, String str);

    static native String getCodeBaseImpl(long j2);

    static native void setCodeBaseImpl(long j2, String str);

    static native String getHeightImpl(long j2);

    static native void setHeightImpl(long j2, String str);

    static native int getHspaceImpl(long j2);

    static native void setHspaceImpl(long j2, int i2);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getObjectImpl(long j2);

    static native void setObjectImpl(long j2, String str);

    static native int getVspaceImpl(long j2);

    static native void setVspaceImpl(long j2, int i2);

    static native String getWidthImpl(long j2);

    static native void setWidthImpl(long j2, String str);

    HTMLAppletElementImpl(long peer) {
        super(peer);
    }

    static HTMLAppletElement getImpl(long peer) {
        return (HTMLAppletElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getAlt() {
        return getAltImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setAlt(String value) {
        setAltImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getArchive() {
        return getArchiveImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setArchive(String value) {
        setArchiveImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getCode() {
        return getCodeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setCode(String value) {
        setCodeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getCodeBase() {
        return getCodeBaseImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setCodeBase(String value) {
        setCodeBaseImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getHeight() {
        return getHeightImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setHeight(String value) {
        setHeightImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getHspace() {
        return getHspaceImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setHspace(String value) {
        setHspaceImpl(getPeer(), Integer.parseInt(value));
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getObject() {
        return getObjectImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setObject(String value) {
        setObjectImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getVspace() {
        return getVspaceImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setVspace(String value) {
        setVspaceImpl(getPeer(), Integer.parseInt(value));
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public String getWidth() {
        return getWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAppletElement
    public void setWidth(String value) {
        setWidthImpl(getPeer(), value);
    }
}
