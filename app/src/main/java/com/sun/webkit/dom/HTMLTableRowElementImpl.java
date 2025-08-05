package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableRowElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLTableRowElementImpl.class */
public class HTMLTableRowElementImpl extends HTMLElementImpl implements HTMLTableRowElement {
    static native int getRowIndexImpl(long j2);

    static native int getSectionRowIndexImpl(long j2);

    static native long getCellsImpl(long j2);

    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getBgColorImpl(long j2);

    static native void setBgColorImpl(long j2, String str);

    static native String getChImpl(long j2);

    static native void setChImpl(long j2, String str);

    static native String getChOffImpl(long j2);

    static native void setChOffImpl(long j2, String str);

    static native String getVAlignImpl(long j2);

    static native void setVAlignImpl(long j2, String str);

    static native long insertCellImpl(long j2, int i2);

    static native void deleteCellImpl(long j2, int i2);

    HTMLTableRowElementImpl(long peer) {
        super(peer);
    }

    static HTMLTableRowElement getImpl(long peer) {
        return (HTMLTableRowElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public int getRowIndex() {
        return getRowIndexImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public int getSectionRowIndex() {
        return getSectionRowIndexImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public HTMLCollection getCells() {
        return HTMLCollectionImpl.getImpl(getCellsImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public String getBgColor() {
        return getBgColorImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public void setBgColor(String value) {
        setBgColorImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public String getCh() {
        return getChImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public void setCh(String value) {
        setChImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public String getChOff() {
        return getChOffImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public void setChOff(String value) {
        setChOffImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public String getVAlign() {
        return getVAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public void setVAlign(String value) {
        setVAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public HTMLElement insertCell(int index) throws DOMException {
        return HTMLElementImpl.getImpl(insertCellImpl(getPeer(), index));
    }

    @Override // org.w3c.dom.html.HTMLTableRowElement
    public void deleteCell(int index) throws DOMException {
        deleteCellImpl(getPeer(), index);
    }
}
