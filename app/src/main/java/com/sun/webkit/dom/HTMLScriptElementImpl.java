package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLScriptElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLScriptElementImpl.class */
public class HTMLScriptElementImpl extends HTMLElementImpl implements HTMLScriptElement {
    static native String getTextImpl(long j2);

    static native void setTextImpl(long j2, String str);

    static native String getHtmlForImpl(long j2);

    static native void setHtmlForImpl(long j2, String str);

    static native String getEventImpl(long j2);

    static native void setEventImpl(long j2, String str);

    static native String getCharsetImpl(long j2);

    static native void setCharsetImpl(long j2, String str);

    static native boolean getAsyncImpl(long j2);

    static native void setAsyncImpl(long j2, boolean z2);

    static native boolean getDeferImpl(long j2);

    static native void setDeferImpl(long j2, boolean z2);

    static native String getSrcImpl(long j2);

    static native void setSrcImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native String getCrossOriginImpl(long j2);

    static native void setCrossOriginImpl(long j2, String str);

    HTMLScriptElementImpl(long peer) {
        super(peer);
    }

    static HTMLScriptElement getImpl(long peer) {
        return (HTMLScriptElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public String getText() {
        return getTextImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public void setText(String value) {
        setTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public String getHtmlFor() {
        return getHtmlForImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public void setHtmlFor(String value) {
        setHtmlForImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public String getEvent() {
        return getEventImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public void setEvent(String value) {
        setEventImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public String getCharset() {
        return getCharsetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public void setCharset(String value) {
        setCharsetImpl(getPeer(), value);
    }

    public boolean getAsync() {
        return getAsyncImpl(getPeer());
    }

    public void setAsync(boolean value) {
        setAsyncImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public boolean getDefer() {
        return getDeferImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public void setDefer(boolean value) {
        setDeferImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public String getSrc() {
        return getSrcImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public void setSrc(String value) {
        setSrcImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLScriptElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    public String getCrossOrigin() {
        return getCrossOriginImpl(getPeer());
    }

    public void setCrossOrigin(String value) {
        setCrossOriginImpl(getPeer(), value);
    }
}
