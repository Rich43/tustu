package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLOptionElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLOptionsCollectionImpl.class */
public class HTMLOptionsCollectionImpl extends HTMLCollectionImpl {
    static native int getSelectedIndexImpl(long j2);

    static native void setSelectedIndexImpl(long j2, int i2);

    static native int getLengthImpl(long j2);

    static native void setLengthImpl(long j2, int i2);

    static native long namedItemImpl(long j2, String str);

    static native void addImpl(long j2, long j3, int i2);

    static native long itemImpl(long j2, int i2);

    HTMLOptionsCollectionImpl(long peer) {
        super(peer);
    }

    static HTMLOptionsCollectionImpl getImpl(long peer) {
        return (HTMLOptionsCollectionImpl) create(peer);
    }

    public int getSelectedIndex() {
        return getSelectedIndexImpl(getPeer());
    }

    public void setSelectedIndex(int value) {
        setSelectedIndexImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.HTMLCollectionImpl, org.w3c.dom.html.HTMLCollection
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    public void setLength(int value) throws DOMException {
        setLengthImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.HTMLCollectionImpl, org.w3c.dom.html.HTMLCollection
    public Node namedItem(String name) {
        return NodeImpl.getImpl(namedItemImpl(getPeer(), name));
    }

    public void add(HTMLOptionElement option, int index) throws DOMException {
        addImpl(getPeer(), HTMLOptionElementImpl.getPeer(option), index);
    }

    @Override // com.sun.webkit.dom.HTMLCollectionImpl, org.w3c.dom.html.HTMLCollection
    public Node item(int index) {
        return NodeImpl.getImpl(itemImpl(getPeer(), index));
    }
}
