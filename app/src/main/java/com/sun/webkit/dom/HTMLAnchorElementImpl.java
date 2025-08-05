package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLAnchorElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLAnchorElementImpl.class */
public class HTMLAnchorElementImpl extends HTMLElementImpl implements HTMLAnchorElement {
    static native String getCharsetImpl(long j2);

    static native void setCharsetImpl(long j2, String str);

    static native String getCoordsImpl(long j2);

    static native void setCoordsImpl(long j2, String str);

    static native String getHreflangImpl(long j2);

    static native void setHreflangImpl(long j2, String str);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getPingImpl(long j2);

    static native void setPingImpl(long j2, String str);

    static native String getRelImpl(long j2);

    static native void setRelImpl(long j2, String str);

    static native String getRevImpl(long j2);

    static native void setRevImpl(long j2, String str);

    static native String getShapeImpl(long j2);

    static native void setShapeImpl(long j2, String str);

    static native String getTargetImpl(long j2);

    static native void setTargetImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native String getTextImpl(long j2);

    static native void setTextImpl(long j2, String str);

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

    HTMLAnchorElementImpl(long peer) {
        super(peer);
    }

    static HTMLAnchorElement getImpl(long peer) {
        return (HTMLAnchorElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getCharset() {
        return getCharsetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setCharset(String value) {
        setCharsetImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getCoords() {
        return getCoordsImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setCoords(String value) {
        setCoordsImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getHreflang() {
        return getHreflangImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setHreflang(String value) {
        setHreflangImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    public String getPing() {
        return getPingImpl(getPeer());
    }

    public void setPing(String value) {
        setPingImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getRel() {
        return getRelImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setRel(String value) {
        setRelImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getRev() {
        return getRevImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setRev(String value) {
        setRevImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getShape() {
        return getShapeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setShape(String value) {
        setShapeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getTarget() {
        return getTargetImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setTarget(String value) {
        setTargetImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    public String getText() {
        return getTextImpl(getPeer());
    }

    public void setText(String value) throws DOMException {
        setTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
    public String getHref() {
        return getHrefImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLAnchorElement
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
