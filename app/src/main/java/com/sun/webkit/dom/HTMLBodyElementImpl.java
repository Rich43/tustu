package com.sun.webkit.dom;

import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLBodyElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLBodyElementImpl.class */
public class HTMLBodyElementImpl extends HTMLElementImpl implements HTMLBodyElement {
    static native String getALinkImpl(long j2);

    static native void setALinkImpl(long j2, String str);

    static native String getBackgroundImpl(long j2);

    static native void setBackgroundImpl(long j2, String str);

    static native String getBgColorImpl(long j2);

    static native void setBgColorImpl(long j2, String str);

    static native String getLinkImpl(long j2);

    static native void setLinkImpl(long j2, String str);

    static native String getTextImpl(long j2);

    static native void setTextImpl(long j2, String str);

    static native String getVLinkImpl(long j2);

    static native void setVLinkImpl(long j2, String str);

    static native long getOnblurImpl(long j2);

    static native void setOnblurImpl(long j2, long j3);

    static native long getOnerrorImpl(long j2);

    static native void setOnerrorImpl(long j2, long j3);

    static native long getOnfocusImpl(long j2);

    static native void setOnfocusImpl(long j2, long j3);

    static native long getOnfocusinImpl(long j2);

    static native void setOnfocusinImpl(long j2, long j3);

    static native long getOnfocusoutImpl(long j2);

    static native void setOnfocusoutImpl(long j2, long j3);

    static native long getOnloadImpl(long j2);

    static native void setOnloadImpl(long j2, long j3);

    static native long getOnresizeImpl(long j2);

    static native void setOnresizeImpl(long j2, long j3);

    static native long getOnscrollImpl(long j2);

    static native void setOnscrollImpl(long j2, long j3);

    static native long getOnselectionchangeImpl(long j2);

    static native void setOnselectionchangeImpl(long j2, long j3);

    static native long getOnbeforeunloadImpl(long j2);

    static native void setOnbeforeunloadImpl(long j2, long j3);

    static native long getOnhashchangeImpl(long j2);

    static native void setOnhashchangeImpl(long j2, long j3);

    static native long getOnmessageImpl(long j2);

    static native void setOnmessageImpl(long j2, long j3);

    static native long getOnofflineImpl(long j2);

    static native void setOnofflineImpl(long j2, long j3);

    static native long getOnonlineImpl(long j2);

    static native void setOnonlineImpl(long j2, long j3);

    static native long getOnpagehideImpl(long j2);

    static native void setOnpagehideImpl(long j2, long j3);

    static native long getOnpageshowImpl(long j2);

    static native void setOnpageshowImpl(long j2, long j3);

    static native long getOnpopstateImpl(long j2);

    static native void setOnpopstateImpl(long j2, long j3);

    static native long getOnstorageImpl(long j2);

    static native void setOnstorageImpl(long j2, long j3);

    static native long getOnunloadImpl(long j2);

    static native void setOnunloadImpl(long j2, long j3);

    HTMLBodyElementImpl(long peer) {
        super(peer);
    }

    static HTMLBodyElement getImpl(long peer) {
        return (HTMLBodyElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public String getALink() {
        return getALinkImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public void setALink(String value) {
        setALinkImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public String getBackground() {
        return getBackgroundImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public void setBackground(String value) {
        setBackgroundImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public String getBgColor() {
        return getBgColorImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public void setBgColor(String value) {
        setBgColorImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public String getLink() {
        return getLinkImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public void setLink(String value) {
        setLinkImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public String getText() {
        return getTextImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public void setText(String value) {
        setTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public String getVLink() {
        return getVLinkImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLBodyElement
    public void setVLink(String value) {
        setVLinkImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public EventListener getOnblur() {
        return EventListenerImpl.getImpl(getOnblurImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public void setOnblur(EventListener value) {
        setOnblurImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public EventListener getOnerror() {
        return EventListenerImpl.getImpl(getOnerrorImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public void setOnerror(EventListener value) {
        setOnerrorImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public EventListener getOnfocus() {
        return EventListenerImpl.getImpl(getOnfocusImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public void setOnfocus(EventListener value) {
        setOnfocusImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public EventListener getOnfocusin() {
        return EventListenerImpl.getImpl(getOnfocusinImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public void setOnfocusin(EventListener value) {
        setOnfocusinImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public EventListener getOnfocusout() {
        return EventListenerImpl.getImpl(getOnfocusoutImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public void setOnfocusout(EventListener value) {
        setOnfocusoutImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public EventListener getOnload() {
        return EventListenerImpl.getImpl(getOnloadImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public void setOnload(EventListener value) {
        setOnloadImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public EventListener getOnresize() {
        return EventListenerImpl.getImpl(getOnresizeImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public void setOnresize(EventListener value) {
        setOnresizeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public EventListener getOnscroll() {
        return EventListenerImpl.getImpl(getOnscrollImpl(getPeer()));
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public void setOnscroll(EventListener value) {
        setOnscrollImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnselectionchange() {
        return EventListenerImpl.getImpl(getOnselectionchangeImpl(getPeer()));
    }

    public void setOnselectionchange(EventListener value) {
        setOnselectionchangeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnbeforeunload() {
        return EventListenerImpl.getImpl(getOnbeforeunloadImpl(getPeer()));
    }

    public void setOnbeforeunload(EventListener value) {
        setOnbeforeunloadImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnhashchange() {
        return EventListenerImpl.getImpl(getOnhashchangeImpl(getPeer()));
    }

    public void setOnhashchange(EventListener value) {
        setOnhashchangeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmessage() {
        return EventListenerImpl.getImpl(getOnmessageImpl(getPeer()));
    }

    public void setOnmessage(EventListener value) {
        setOnmessageImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnoffline() {
        return EventListenerImpl.getImpl(getOnofflineImpl(getPeer()));
    }

    public void setOnoffline(EventListener value) {
        setOnofflineImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnonline() {
        return EventListenerImpl.getImpl(getOnonlineImpl(getPeer()));
    }

    public void setOnonline(EventListener value) {
        setOnonlineImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnpagehide() {
        return EventListenerImpl.getImpl(getOnpagehideImpl(getPeer()));
    }

    public void setOnpagehide(EventListener value) {
        setOnpagehideImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnpageshow() {
        return EventListenerImpl.getImpl(getOnpageshowImpl(getPeer()));
    }

    public void setOnpageshow(EventListener value) {
        setOnpageshowImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnpopstate() {
        return EventListenerImpl.getImpl(getOnpopstateImpl(getPeer()));
    }

    public void setOnpopstate(EventListener value) {
        setOnpopstateImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnstorage() {
        return EventListenerImpl.getImpl(getOnstorageImpl(getPeer()));
    }

    public void setOnstorage(EventListener value) {
        setOnstorageImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnunload() {
        return EventListenerImpl.getImpl(getOnunloadImpl(getPeer()));
    }

    public void setOnunload(EventListener value) {
        setOnunloadImpl(getPeer(), EventListenerImpl.getPeer(value));
    }
}
