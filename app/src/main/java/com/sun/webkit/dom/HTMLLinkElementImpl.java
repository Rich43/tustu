package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.stylesheets.StyleSheet;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLLinkElementImpl.class */
public class HTMLLinkElementImpl extends HTMLElementImpl implements HTMLLinkElement {
    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native String getCharsetImpl(long j2);

    static native void setCharsetImpl(long j2, String str);

    static native String getHrefImpl(long j2);

    static native void setHrefImpl(long j2, String str);

    static native String getHreflangImpl(long j2);

    static native void setHreflangImpl(long j2, String str);

    static native String getMediaImpl(long j2);

    static native void setMediaImpl(long j2, String str);

    static native String getRelImpl(long j2);

    static native void setRelImpl(long j2, String str);

    static native String getRevImpl(long j2);

    static native void setRevImpl(long j2, String str);

    static native String getTargetImpl(long j2);

    static native void setTargetImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native long getSheetImpl(long j2);

    HTMLLinkElementImpl(long peer) {
        super(peer);
    }

    static HTMLLinkElement getImpl(long peer) {
        return (HTMLLinkElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public String getCharset() {
        return getCharsetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setCharset(String value) {
        setCharsetImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public String getHref() {
        return getHrefImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setHref(String value) {
        setHrefImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public String getHreflang() {
        return getHreflangImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setHreflang(String value) {
        setHreflangImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public String getMedia() {
        return getMediaImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setMedia(String value) {
        setMediaImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public String getRel() {
        return getRelImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setRel(String value) {
        setRelImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public String getRev() {
        return getRevImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setRev(String value) {
        setRevImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public String getTarget() {
        return getTargetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setTarget(String value) {
        setTargetImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLLinkElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    public StyleSheet getSheet() {
        return StyleSheetImpl.getImpl(getSheetImpl(getPeer()));
    }
}
