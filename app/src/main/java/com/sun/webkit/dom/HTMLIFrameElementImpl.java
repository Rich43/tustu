package com.sun.webkit.dom;

import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.views.AbstractView;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLIFrameElementImpl.class */
public class HTMLIFrameElementImpl extends HTMLElementImpl implements HTMLIFrameElement {
    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getFrameBorderImpl(long j2);

    static native void setFrameBorderImpl(long j2, String str);

    static native String getHeightImpl(long j2);

    static native void setHeightImpl(long j2, String str);

    static native String getLongDescImpl(long j2);

    static native void setLongDescImpl(long j2, String str);

    static native String getMarginHeightImpl(long j2);

    static native void setMarginHeightImpl(long j2, String str);

    static native String getMarginWidthImpl(long j2);

    static native void setMarginWidthImpl(long j2, String str);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getScrollingImpl(long j2);

    static native void setScrollingImpl(long j2, String str);

    static native String getSrcImpl(long j2);

    static native void setSrcImpl(long j2, String str);

    static native String getSrcdocImpl(long j2);

    static native void setSrcdocImpl(long j2, String str);

    static native String getWidthImpl(long j2);

    static native void setWidthImpl(long j2, String str);

    static native long getContentDocumentImpl(long j2);

    static native long getContentWindowImpl(long j2);

    HTMLIFrameElementImpl(long peer) {
        super(peer);
    }

    static HTMLIFrameElement getImpl(long peer) {
        return (HTMLIFrameElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getFrameBorder() {
        return getFrameBorderImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setFrameBorder(String value) {
        setFrameBorderImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getHeight() {
        return getHeightImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setHeight(String value) {
        setHeightImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getLongDesc() {
        return getLongDescImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setLongDesc(String value) {
        setLongDescImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getMarginHeight() {
        return getMarginHeightImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setMarginHeight(String value) {
        setMarginHeightImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getMarginWidth() {
        return getMarginWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setMarginWidth(String value) {
        setMarginWidthImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getScrolling() {
        return getScrollingImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setScrolling(String value) {
        setScrollingImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getSrc() {
        return getSrcImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setSrc(String value) {
        setSrcImpl(getPeer(), value);
    }

    public String getSrcdoc() {
        return getSrcdocImpl(getPeer());
    }

    public void setSrcdoc(String value) {
        setSrcdocImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public String getWidth() {
        return getWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public void setWidth(String value) {
        setWidthImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLIFrameElement
    public Document getContentDocument() {
        return DocumentImpl.getImpl(getContentDocumentImpl(getPeer()));
    }

    public AbstractView getContentWindow() {
        return DOMWindowImpl.getImpl(getContentWindowImpl(getPeer()));
    }
}
