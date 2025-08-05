package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLStyleElement;
import org.w3c.dom.stylesheets.StyleSheet;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLStyleElementImpl.class */
public class HTMLStyleElementImpl extends HTMLElementImpl implements HTMLStyleElement {
    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native String getMediaImpl(long j2);

    static native void setMediaImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native long getSheetImpl(long j2);

    HTMLStyleElementImpl(long peer) {
        super(peer);
    }

    static HTMLStyleElement getImpl(long peer) {
        return (HTMLStyleElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLStyleElement
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLStyleElement
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLStyleElement
    public String getMedia() {
        return getMediaImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLStyleElement
    public void setMedia(String value) {
        setMediaImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLStyleElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLStyleElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    public StyleSheet getSheet() {
        return StyleSheetImpl.getImpl(getSheetImpl(getPeer()));
    }
}
