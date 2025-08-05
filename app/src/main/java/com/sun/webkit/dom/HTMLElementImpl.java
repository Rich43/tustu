package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLElementImpl.class */
public class HTMLElementImpl extends ElementImpl implements HTMLElement {
    static native String getIdImpl(long j2);

    static native void setIdImpl(long j2, String str);

    static native String getTitleImpl(long j2);

    static native void setTitleImpl(long j2, String str);

    static native String getLangImpl(long j2);

    static native void setLangImpl(long j2, String str);

    static native boolean getTranslateImpl(long j2);

    static native void setTranslateImpl(long j2, boolean z2);

    static native String getDirImpl(long j2);

    static native void setDirImpl(long j2, String str);

    static native int getTabIndexImpl(long j2);

    static native void setTabIndexImpl(long j2, int i2);

    static native boolean getDraggableImpl(long j2);

    static native void setDraggableImpl(long j2, boolean z2);

    static native String getWebkitdropzoneImpl(long j2);

    static native void setWebkitdropzoneImpl(long j2, String str);

    static native boolean getHiddenImpl(long j2);

    static native void setHiddenImpl(long j2, boolean z2);

    static native String getAccessKeyImpl(long j2);

    static native void setAccessKeyImpl(long j2, String str);

    static native String getInnerTextImpl(long j2);

    static native void setInnerTextImpl(long j2, String str);

    static native String getOuterTextImpl(long j2);

    static native void setOuterTextImpl(long j2, String str);

    static native long getChildrenImpl(long j2);

    static native String getContentEditableImpl(long j2);

    static native void setContentEditableImpl(long j2, String str);

    static native boolean getIsContentEditableImpl(long j2);

    static native boolean getSpellcheckImpl(long j2);

    static native void setSpellcheckImpl(long j2, boolean z2);

    static native String getTitleDisplayStringImpl(long j2);

    static native long insertAdjacentElementImpl(long j2, String str, long j3);

    static native void insertAdjacentHTMLImpl(long j2, String str, String str2);

    static native void insertAdjacentTextImpl(long j2, String str, String str2);

    static native void clickImpl(long j2);

    HTMLElementImpl(long peer) {
        super(peer);
    }

    static HTMLElement getImpl(long peer) {
        return (HTMLElement) create(peer);
    }

    @Override // com.sun.webkit.dom.ElementImpl, org.w3c.dom.html.HTMLElement
    public String getId() {
        return getIdImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.ElementImpl, org.w3c.dom.html.HTMLElement
    public void setId(String value) {
        setIdImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLElement
    public String getTitle() {
        return getTitleImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLElement
    public void setTitle(String value) {
        setTitleImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLElement
    public String getLang() {
        return getLangImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLElement
    public void setLang(String value) {
        setLangImpl(getPeer(), value);
    }

    public boolean getTranslate() {
        return getTranslateImpl(getPeer());
    }

    public void setTranslate(boolean value) {
        setTranslateImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLElement
    public String getDir() {
        return getDirImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLElement
    public void setDir(String value) {
        setDirImpl(getPeer(), value);
    }

    public int getTabIndex() {
        return getTabIndexImpl(getPeer());
    }

    public void setTabIndex(int value) {
        setTabIndexImpl(getPeer(), value);
    }

    public boolean getDraggable() {
        return getDraggableImpl(getPeer());
    }

    public void setDraggable(boolean value) {
        setDraggableImpl(getPeer(), value);
    }

    public String getWebkitdropzone() {
        return getWebkitdropzoneImpl(getPeer());
    }

    public void setWebkitdropzone(String value) {
        setWebkitdropzoneImpl(getPeer(), value);
    }

    public boolean getHidden() {
        return getHiddenImpl(getPeer());
    }

    public void setHidden(boolean value) {
        setHiddenImpl(getPeer(), value);
    }

    public String getAccessKey() {
        return getAccessKeyImpl(getPeer());
    }

    public void setAccessKey(String value) {
        setAccessKeyImpl(getPeer(), value);
    }

    public String getInnerText() {
        return getInnerTextImpl(getPeer());
    }

    public void setInnerText(String value) throws DOMException {
        setInnerTextImpl(getPeer(), value);
    }

    public String getOuterText() {
        return getOuterTextImpl(getPeer());
    }

    public void setOuterText(String value) throws DOMException {
        setOuterTextImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.ElementImpl
    public HTMLCollection getChildren() {
        return HTMLCollectionImpl.getImpl(getChildrenImpl(getPeer()));
    }

    public String getContentEditable() {
        return getContentEditableImpl(getPeer());
    }

    public void setContentEditable(String value) throws DOMException {
        setContentEditableImpl(getPeer(), value);
    }

    public boolean getIsContentEditable() {
        return getIsContentEditableImpl(getPeer());
    }

    public boolean getSpellcheck() {
        return getSpellcheckImpl(getPeer());
    }

    public void setSpellcheck(boolean value) {
        setSpellcheckImpl(getPeer(), value);
    }

    public String getTitleDisplayString() {
        return getTitleDisplayStringImpl(getPeer());
    }

    public Element insertAdjacentElement(String where, Element element) throws DOMException {
        return ElementImpl.getImpl(insertAdjacentElementImpl(getPeer(), where, ElementImpl.getPeer(element)));
    }

    public void insertAdjacentHTML(String where, String html) throws DOMException {
        insertAdjacentHTMLImpl(getPeer(), where, html);
    }

    public void insertAdjacentText(String where, String text) throws DOMException {
        insertAdjacentTextImpl(getPeer(), where, text);
    }

    public void click() {
        clickImpl(getPeer());
    }
}
