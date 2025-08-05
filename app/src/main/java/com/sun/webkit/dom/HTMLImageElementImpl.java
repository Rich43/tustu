package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLImageElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLImageElementImpl.class */
public class HTMLImageElementImpl extends HTMLElementImpl implements HTMLImageElement {
    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getAltImpl(long j2);

    static native void setAltImpl(long j2, String str);

    static native String getBorderImpl(long j2);

    static native void setBorderImpl(long j2, String str);

    static native String getCrossOriginImpl(long j2);

    static native void setCrossOriginImpl(long j2, String str);

    static native int getHeightImpl(long j2);

    static native void setHeightImpl(long j2, int i2);

    static native int getHspaceImpl(long j2);

    static native void setHspaceImpl(long j2, int i2);

    static native boolean getIsMapImpl(long j2);

    static native void setIsMapImpl(long j2, boolean z2);

    static native String getLongDescImpl(long j2);

    static native void setLongDescImpl(long j2, String str);

    static native String getSrcImpl(long j2);

    static native void setSrcImpl(long j2, String str);

    static native String getSrcsetImpl(long j2);

    static native void setSrcsetImpl(long j2, String str);

    static native String getSizesImpl(long j2);

    static native void setSizesImpl(long j2, String str);

    static native String getCurrentSrcImpl(long j2);

    static native String getUseMapImpl(long j2);

    static native void setUseMapImpl(long j2, String str);

    static native int getVspaceImpl(long j2);

    static native void setVspaceImpl(long j2, int i2);

    static native int getWidthImpl(long j2);

    static native void setWidthImpl(long j2, int i2);

    static native boolean getCompleteImpl(long j2);

    static native String getLowsrcImpl(long j2);

    static native void setLowsrcImpl(long j2, String str);

    static native int getNaturalHeightImpl(long j2);

    static native int getNaturalWidthImpl(long j2);

    static native int getXImpl(long j2);

    static native int getYImpl(long j2);

    HTMLImageElementImpl(long peer) {
        super(peer);
    }

    static HTMLImageElement getImpl(long peer) {
        return (HTMLImageElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getAlt() {
        return getAltImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setAlt(String value) {
        setAltImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getBorder() {
        return getBorderImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setBorder(String value) {
        setBorderImpl(getPeer(), value);
    }

    public String getCrossOrigin() {
        return getCrossOriginImpl(getPeer());
    }

    public void setCrossOrigin(String value) {
        setCrossOriginImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getHeight() {
        return getHeightImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setHeight(String value) {
        setHeightImpl(getPeer(), Integer.parseInt(value));
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getHspace() {
        return getHspaceImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setHspace(String value) {
        setHspaceImpl(getPeer(), Integer.parseInt(value));
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public boolean getIsMap() {
        return getIsMapImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setIsMap(boolean value) {
        setIsMapImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getLongDesc() {
        return getLongDescImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setLongDesc(String value) {
        setLongDescImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getSrc() {
        return getSrcImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setSrc(String value) {
        setSrcImpl(getPeer(), value);
    }

    public String getSrcset() {
        return getSrcsetImpl(getPeer());
    }

    public void setSrcset(String value) {
        setSrcsetImpl(getPeer(), value);
    }

    public String getSizes() {
        return getSizesImpl(getPeer());
    }

    public void setSizes(String value) {
        setSizesImpl(getPeer(), value);
    }

    public String getCurrentSrc() {
        return getCurrentSrcImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getUseMap() {
        return getUseMapImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setUseMap(String value) {
        setUseMapImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getVspace() {
        return getVspaceImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setVspace(String value) {
        setVspaceImpl(getPeer(), Integer.parseInt(value));
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getWidth() {
        return getWidthImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setWidth(String value) {
        setWidthImpl(getPeer(), Integer.parseInt(value));
    }

    public boolean getComplete() {
        return getCompleteImpl(getPeer());
    }

    public String getLowsrc() {
        return getLowsrcImpl(getPeer());
    }

    public void setLowsrc(String value) {
        setLowsrcImpl(getPeer(), value);
    }

    public int getNaturalHeight() {
        return getNaturalHeightImpl(getPeer());
    }

    public int getNaturalWidth() {
        return getNaturalWidthImpl(getPeer());
    }

    public int getX() {
        return getXImpl(getPeer());
    }

    public int getY() {
        return getYImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public void setLowSrc(String lowSrc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.html.HTMLImageElement
    public String getLowSrc() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
