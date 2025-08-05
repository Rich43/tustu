package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCaptionElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLTableElementImpl.class */
public class HTMLTableElementImpl extends HTMLElementImpl implements HTMLTableElement {
    static native long getCaptionImpl(long j2);

    static native void setCaptionImpl(long j2, long j3);

    static native long getTHeadImpl(long j2);

    static native void setTHeadImpl(long j2, long j3);

    static native long getTFootImpl(long j2);

    static native void setTFootImpl(long j2, long j3);

    static native long getRowsImpl(long j2);

    static native long getTBodiesImpl(long j2);

    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getBgColorImpl(long j2);

    static native void setBgColorImpl(long j2, String str);

    static native String getBorderImpl(long j2);

    static native void setBorderImpl(long j2, String str);

    static native String getCellPaddingImpl(long j2);

    static native void setCellPaddingImpl(long j2, String str);

    static native String getCellSpacingImpl(long j2);

    static native void setCellSpacingImpl(long j2, String str);

    static native String getFrameImpl(long j2);

    static native void setFrameImpl(long j2, String str);

    static native String getRulesImpl(long j2);

    static native void setRulesImpl(long j2, String str);

    static native String getSummaryImpl(long j2);

    static native void setSummaryImpl(long j2, String str);

    static native String getWidthImpl(long j2);

    static native void setWidthImpl(long j2, String str);

    static native long createTHeadImpl(long j2);

    static native void deleteTHeadImpl(long j2);

    static native long createTFootImpl(long j2);

    static native void deleteTFootImpl(long j2);

    static native long createTBodyImpl(long j2);

    static native long createCaptionImpl(long j2);

    static native void deleteCaptionImpl(long j2);

    static native long insertRowImpl(long j2, int i2);

    static native void deleteRowImpl(long j2, int i2);

    HTMLTableElementImpl(long peer) {
        super(peer);
    }

    static HTMLTableElement getImpl(long peer) {
        return (HTMLTableElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLTableCaptionElement getCaption() {
        return HTMLTableCaptionElementImpl.getImpl(getCaptionImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setCaption(HTMLTableCaptionElement value) throws DOMException {
        setCaptionImpl(getPeer(), HTMLTableCaptionElementImpl.getPeer(value));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLTableSectionElement getTHead() {
        return HTMLTableSectionElementImpl.getImpl(getTHeadImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setTHead(HTMLTableSectionElement value) throws DOMException {
        setTHeadImpl(getPeer(), HTMLTableSectionElementImpl.getPeer(value));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLTableSectionElement getTFoot() {
        return HTMLTableSectionElementImpl.getImpl(getTFootImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setTFoot(HTMLTableSectionElement value) throws DOMException {
        setTFootImpl(getPeer(), HTMLTableSectionElementImpl.getPeer(value));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLCollection getRows() {
        return HTMLCollectionImpl.getImpl(getRowsImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLCollection getTBodies() {
        return HTMLCollectionImpl.getImpl(getTBodiesImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getBgColor() {
        return getBgColorImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setBgColor(String value) {
        setBgColorImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getBorder() {
        return getBorderImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setBorder(String value) {
        setBorderImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getCellPadding() {
        return getCellPaddingImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setCellPadding(String value) {
        setCellPaddingImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getCellSpacing() {
        return getCellSpacingImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setCellSpacing(String value) {
        setCellSpacingImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getFrame() {
        return getFrameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setFrame(String value) {
        setFrameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getRules() {
        return getRulesImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setRules(String value) {
        setRulesImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getSummary() {
        return getSummaryImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setSummary(String value) {
        setSummaryImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public String getWidth() {
        return getWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void setWidth(String value) {
        setWidthImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLElement createTHead() {
        return HTMLElementImpl.getImpl(createTHeadImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void deleteTHead() {
        deleteTHeadImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLElement createTFoot() {
        return HTMLElementImpl.getImpl(createTFootImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void deleteTFoot() {
        deleteTFootImpl(getPeer());
    }

    public HTMLElement createTBody() {
        return HTMLElementImpl.getImpl(createTBodyImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLElement createCaption() {
        return HTMLElementImpl.getImpl(createCaptionImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void deleteCaption() {
        deleteCaptionImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public HTMLElement insertRow(int index) throws DOMException {
        return HTMLElementImpl.getImpl(insertRowImpl(getPeer(), index));
    }

    @Override // org.w3c.dom.html.HTMLTableElement
    public void deleteRow(int index) throws DOMException {
        deleteRowImpl(getPeer(), index);
    }
}
