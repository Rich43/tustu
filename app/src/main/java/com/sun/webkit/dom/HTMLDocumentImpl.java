package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLDocumentImpl.class */
public class HTMLDocumentImpl extends DocumentImpl implements HTMLDocument {
    static native long getEmbedsImpl(long j2);

    static native long getPluginsImpl(long j2);

    static native long getScriptsImpl(long j2);

    static native int getWidthImpl(long j2);

    static native int getHeightImpl(long j2);

    static native String getDirImpl(long j2);

    static native void setDirImpl(long j2, String str);

    static native String getDesignModeImpl(long j2);

    static native void setDesignModeImpl(long j2, String str);

    static native String getCompatModeImpl(long j2);

    static native String getBgColorImpl(long j2);

    static native void setBgColorImpl(long j2, String str);

    static native String getFgColorImpl(long j2);

    static native void setFgColorImpl(long j2, String str);

    static native String getAlinkColorImpl(long j2);

    static native void setAlinkColorImpl(long j2, String str);

    static native String getLinkColorImpl(long j2);

    static native void setLinkColorImpl(long j2, String str);

    static native String getVlinkColorImpl(long j2);

    static native void setVlinkColorImpl(long j2, String str);

    static native void openImpl(long j2);

    static native void closeImpl(long j2);

    static native void writeImpl(long j2, String str);

    static native void writelnImpl(long j2, String str);

    static native void clearImpl(long j2);

    static native void captureEventsImpl(long j2);

    static native void releaseEventsImpl(long j2);

    HTMLDocumentImpl(long peer) {
        super(peer);
    }

    static HTMLDocument getImpl(long peer) {
        return (HTMLDocument) create(peer);
    }

    public HTMLCollection getEmbeds() {
        return HTMLCollectionImpl.getImpl(getEmbedsImpl(getPeer()));
    }

    public HTMLCollection getPlugins() {
        return HTMLCollectionImpl.getImpl(getPluginsImpl(getPeer()));
    }

    public HTMLCollection getScripts() {
        return HTMLCollectionImpl.getImpl(getScriptsImpl(getPeer()));
    }

    public int getWidth() {
        return getWidthImpl(getPeer());
    }

    public int getHeight() {
        return getHeightImpl(getPeer());
    }

    public String getDir() {
        return getDirImpl(getPeer());
    }

    public void setDir(String value) {
        setDirImpl(getPeer(), value);
    }

    public String getDesignMode() {
        return getDesignModeImpl(getPeer());
    }

    public void setDesignMode(String value) {
        setDesignModeImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.DocumentImpl
    public String getCompatMode() {
        return getCompatModeImpl(getPeer());
    }

    public String getBgColor() {
        return getBgColorImpl(getPeer());
    }

    public void setBgColor(String value) {
        setBgColorImpl(getPeer(), value);
    }

    public String getFgColor() {
        return getFgColorImpl(getPeer());
    }

    public void setFgColor(String value) {
        setFgColorImpl(getPeer(), value);
    }

    public String getAlinkColor() {
        return getAlinkColorImpl(getPeer());
    }

    public void setAlinkColor(String value) {
        setAlinkColorImpl(getPeer(), value);
    }

    public String getLinkColor() {
        return getLinkColorImpl(getPeer());
    }

    public void setLinkColor(String value) {
        setLinkColorImpl(getPeer(), value);
    }

    public String getVlinkColor() {
        return getVlinkColorImpl(getPeer());
    }

    public void setVlinkColor(String value) {
        setVlinkColorImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLDocument
    public void open() {
        openImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLDocument
    public void close() {
        closeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLDocument
    public void write(String text) {
        writeImpl(getPeer(), text);
    }

    @Override // org.w3c.dom.html.HTMLDocument
    public void writeln(String text) {
        writelnImpl(getPeer(), text);
    }

    public void clear() {
        clearImpl(getPeer());
    }

    public void captureEvents() {
        captureEventsImpl(getPeer());
    }

    public void releaseEvents() {
        releaseEventsImpl(getPeer());
    }
}
