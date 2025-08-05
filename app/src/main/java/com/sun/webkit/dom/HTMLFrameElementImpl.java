package com.sun.webkit.dom;

import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFrameElement;
import org.w3c.dom.views.AbstractView;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLFrameElementImpl.class */
public class HTMLFrameElementImpl extends HTMLElementImpl implements HTMLFrameElement {
    static native String getFrameBorderImpl(long j2);

    static native void setFrameBorderImpl(long j2, String str);

    static native String getLongDescImpl(long j2);

    static native void setLongDescImpl(long j2, String str);

    static native String getMarginHeightImpl(long j2);

    static native void setMarginHeightImpl(long j2, String str);

    static native String getMarginWidthImpl(long j2);

    static native void setMarginWidthImpl(long j2, String str);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native boolean getNoResizeImpl(long j2);

    static native void setNoResizeImpl(long j2, boolean z2);

    static native String getScrollingImpl(long j2);

    static native void setScrollingImpl(long j2, String str);

    static native String getSrcImpl(long j2);

    static native void setSrcImpl(long j2, String str);

    static native long getContentDocumentImpl(long j2);

    static native long getContentWindowImpl(long j2);

    static native String getLocationImpl(long j2);

    static native void setLocationImpl(long j2, String str);

    static native int getWidthImpl(long j2);

    static native int getHeightImpl(long j2);

    HTMLFrameElementImpl(long peer) {
        super(peer);
    }

    static HTMLFrameElement getImpl(long peer) {
        return (HTMLFrameElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public String getFrameBorder() {
        return getFrameBorderImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public void setFrameBorder(String value) {
        setFrameBorderImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public String getLongDesc() {
        return getLongDescImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public void setLongDesc(String value) {
        setLongDescImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public String getMarginHeight() {
        return getMarginHeightImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public void setMarginHeight(String value) {
        setMarginHeightImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public String getMarginWidth() {
        return getMarginWidthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public void setMarginWidth(String value) {
        setMarginWidthImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public boolean getNoResize() {
        return getNoResizeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public void setNoResize(boolean value) {
        setNoResizeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public String getScrolling() {
        return getScrollingImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public void setScrolling(String value) {
        setScrollingImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public String getSrc() {
        return getSrcImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public void setSrc(String value) {
        setSrcImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLFrameElement
    public Document getContentDocument() {
        return DocumentImpl.getImpl(getContentDocumentImpl(getPeer()));
    }

    public AbstractView getContentWindow() {
        return DOMWindowImpl.getImpl(getContentWindowImpl(getPeer()));
    }

    public String getLocation() {
        return getLocationImpl(getPeer());
    }

    public void setLocation(String value) {
        setLocationImpl(getPeer(), value);
    }

    public int getWidth() {
        return getWidthImpl(getPeer());
    }

    public int getHeight() {
        return getHeightImpl(getPeer());
    }
}
