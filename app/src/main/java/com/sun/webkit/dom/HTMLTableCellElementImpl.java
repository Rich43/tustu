package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLTableCellElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLTableCellElementImpl.class */
public class HTMLTableCellElementImpl extends HTMLElementImpl implements HTMLTableCellElement {
    static native int getCellIndexImpl(long j2);

    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getAxisImpl(long j2);

    static native void setAxisImpl(long j2, String str);

    static native String getBgColorImpl(long j2);

    static native void setBgColorImpl(long j2, String str);

    static native String getChImpl(long j2);

    static native void setChImpl(long j2, String str);

    static native String getChOffImpl(long j2);

    static native void setChOffImpl(long j2, String str);

    static native int getColSpanImpl(long j2);

    static native void setColSpanImpl(long j2, int i2);

    static native int getRowSpanImpl(long j2);

    static native void setRowSpanImpl(long j2, int i2);

    static native String getHeadersImpl(long j2);

    static native void setHeadersImpl(long j2, String str);

    static native String getHeightImpl(long j2);

    static native void setHeightImpl(long j2, String str);

    static native boolean getNoWrapImpl(long j2);

    static native void setNoWrapImpl(long j2, boolean z2);

    static native String getVAlignImpl(long j2);

    static native void setVAlignImpl(long j2, String str);

    static native String getWidthImpl(long j2);

    static native void setWidthImpl(long j2, String str);

    static native String getAbbrImpl(long j2);

    static native void setAbbrImpl(long j2, String str);

    static native String getScopeImpl(long j2);

    static native void setScopeImpl(long j2, String str);

    HTMLTableCellElementImpl(long peer) {
        super(peer);
    }

    static HTMLTableCellElement getImpl(long peer) {
        return (HTMLTableCellElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public int getCellIndex() {
        return getCellIndexImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getAxis() {
        return getAxisImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setAxis(String value) {
        setAxisImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getBgColor() {
        return getBgColorImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setBgColor(String value) {
        setBgColorImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getCh() {
        return getChImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setCh(String value) {
        setChImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getChOff() {
        return getChOffImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setChOff(String value) {
        setChOffImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public int getColSpan() {
        return getColSpanImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setColSpan(int value) {
        setColSpanImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public int getRowSpan() {
        return getRowSpanImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setRowSpan(int value) {
        setRowSpanImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getHeaders() {
        return getHeadersImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setHeaders(String value) {
        setHeadersImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getHeight() {
        return getHeightImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setHeight(String value) {
        setHeightImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public boolean getNoWrap() {
        return getNoWrapImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setNoWrap(boolean value) {
        setNoWrapImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getVAlign() {
        return getVAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setVAlign(String value) {
        setVAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getWidth() {
        return getWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setWidth(String value) {
        setWidthImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getAbbr() {
        return getAbbrImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setAbbr(String value) {
        setAbbrImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public String getScope() {
        return getScopeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableCellElement
    public void setScope(String value) {
        setScopeImpl(getPeer(), value);
    }
}
