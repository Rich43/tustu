package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLAreaElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLAreaElementImpl.class */
public class HTMLAreaElementImpl extends HTMLElementImpl implements HTMLAreaElement {
    static native String getAltImpl(long j2);

    static native void setAltImpl(long j2, String str);

    static native String getCoordsImpl(long j2);

    static native void setCoordsImpl(long j2, String str);

    static native boolean getNoHrefImpl(long j2);

    static native void setNoHrefImpl(long j2, boolean z2);

    static native String getPingImpl(long j2);

    static native void setPingImpl(long j2, String str);

    static native String getRelImpl(long j2);

    static native void setRelImpl(long j2, String str);

    static native String getShapeImpl(long j2);

    static native void setShapeImpl(long j2, String str);

    static native String getTargetImpl(long j2);

    static native void setTargetImpl(long j2, String str);

    static native String getAccessKeyImpl(long j2);

    static native void setAccessKeyImpl(long j2, String str);

    static native String getHrefImpl(long j2);

    static native void setHrefImpl(long j2, String str);

    static native String getOriginImpl(long j2);

    static native String getProtocolImpl(long j2);

    static native void setProtocolImpl(long j2, String str);

    static native String getUsernameImpl(long j2);

    static native void setUsernameImpl(long j2, String str);

    static native String getPasswordImpl(long j2);

    static native void setPasswordImpl(long j2, String str);

    static native String getHostImpl(long j2);

    static native void setHostImpl(long j2, String str);

    static native String getHostnameImpl(long j2);

    static native void setHostnameImpl(long j2, String str);

    static native String getPortImpl(long j2);

    static native void setPortImpl(long j2, String str);

    static native String getPathnameImpl(long j2);

    static native void setPathnameImpl(long j2, String str);

    static native String getSearchImpl(long j2);

    static native void setSearchImpl(long j2, String str);

    static native String getHashImpl(long j2);

    static native void setHashImpl(long j2, String str);

    HTMLAreaElementImpl(long peer) {
        super(peer);
    }

    static HTMLAreaElement getImpl(long peer) {
        return (HTMLAreaElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public String getAlt() {
        return getAltImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public void setAlt(String value) {
        setAltImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public String getCoords() {
        return getCoordsImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public void setCoords(String value) {
        setCoordsImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public boolean getNoHref() {
        return getNoHrefImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public void setNoHref(boolean value) {
        setNoHrefImpl(getPeer(), value);
    }

    public String getPing() {
        return getPingImpl(getPeer());
    }

    public void setPing(String value) {
        setPingImpl(getPeer(), value);
    }

    public String getRel() {
        return getRelImpl(getPeer());
    }

    public void setRel(String value) {
        setRelImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public String getShape() {
        return getShapeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public void setShape(String value) {
        setShapeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public String getTarget() {
        return getTargetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public void setTarget(String value) {
        setTargetImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public String getAccessKey() {
        return getAccessKeyImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public void setAccessKey(String value) {
        setAccessKeyImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public String getHref() {
        return getHrefImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAreaElement
    public void setHref(String value) {
        setHrefImpl(getPeer(), value);
    }

    public String getOrigin() {
        return getOriginImpl(getPeer());
    }

    public String getProtocol() {
        return getProtocolImpl(getPeer());
    }

    public void setProtocol(String value) {
        setProtocolImpl(getPeer(), value);
    }

    public String getUsername() {
        return getUsernameImpl(getPeer());
    }

    public void setUsername(String value) {
        setUsernameImpl(getPeer(), value);
    }

    public String getPassword() {
        return getPasswordImpl(getPeer());
    }

    public void setPassword(String value) {
        setPasswordImpl(getPeer(), value);
    }

    public String getHost() {
        return getHostImpl(getPeer());
    }

    public void setHost(String value) {
        setHostImpl(getPeer(), value);
    }

    public String getHostname() {
        return getHostnameImpl(getPeer());
    }

    public void setHostname(String value) {
        setHostnameImpl(getPeer(), value);
    }

    public String getPort() {
        return getPortImpl(getPeer());
    }

    public void setPort(String value) {
        setPortImpl(getPeer(), value);
    }

    public String getPathname() {
        return getPathnameImpl(getPeer());
    }

    public void setPathname(String value) {
        setPathnameImpl(getPeer(), value);
    }

    public String getSearch() {
        return getSearchImpl(getPeer());
    }

    public void setSearch(String value) {
        setSearchImpl(getPeer(), value);
    }

    public String getHash() {
        return getHashImpl(getPeer());
    }

    public void setHash(String value) {
        setHashImpl(getPeer(), value);
    }
}
