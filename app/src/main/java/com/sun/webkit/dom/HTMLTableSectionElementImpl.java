package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLTableSectionElementImpl.class */
public class HTMLTableSectionElementImpl extends HTMLElementImpl implements HTMLTableSectionElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getChImpl(long j2);

    static native void setChImpl(long j2, String str);

    static native String getChOffImpl(long j2);

    static native void setChOffImpl(long j2, String str);

    static native String getVAlignImpl(long j2);

    static native void setVAlignImpl(long j2, String str);

    static native long getRowsImpl(long j2);

    static native long insertRowImpl(long j2, int i2);

    static native void deleteRowImpl(long j2, int i2);

    HTMLTableSectionElementImpl(long peer) {
        super(peer);
    }

    static HTMLTableSectionElement getImpl(long peer) {
        return (HTMLTableSectionElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public String getCh() {
        return getChImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public void setCh(String value) {
        setChImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public String getChOff() {
        return getChOffImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public void setChOff(String value) {
        setChOffImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public String getVAlign() {
        return getVAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public void setVAlign(String value) {
        setVAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public HTMLCollection getRows() {
        return HTMLCollectionImpl.getImpl(getRowsImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public HTMLElement insertRow(int index) throws DOMException {
        return HTMLElementImpl.getImpl(insertRowImpl(getPeer(), index));
    }

    @Override // org.w3c.dom.html.HTMLTableSectionElement
    public void deleteRow(int index) throws DOMException {
        deleteRowImpl(getPeer(), index);
    }
}
