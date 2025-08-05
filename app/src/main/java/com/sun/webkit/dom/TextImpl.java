package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/TextImpl.class */
public class TextImpl extends CharacterDataImpl implements Text {
    static native String getWholeTextImpl(long j2);

    static native long splitTextImpl(long j2, int i2);

    static native long replaceWholeTextImpl(long j2, String str);

    TextImpl(long peer) {
        super(peer);
    }

    static Text getImpl(long peer) {
        return (Text) create(peer);
    }

    @Override // org.w3c.dom.Text
    public String getWholeText() {
        return getWholeTextImpl(getPeer());
    }

    @Override // org.w3c.dom.Text
    public Text splitText(int offset) throws DOMException {
        return getImpl(splitTextImpl(getPeer(), offset));
    }

    @Override // org.w3c.dom.Text
    public Text replaceWholeText(String content) throws DOMException {
        return getImpl(replaceWholeTextImpl(getPeer(), content));
    }

    @Override // org.w3c.dom.Text
    public boolean isElementContentWhitespace() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
