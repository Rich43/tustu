package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/DocumentFragmentImpl.class */
public class DocumentFragmentImpl extends NodeImpl implements DocumentFragment {
    static native long getChildrenImpl(long j2);

    static native long getFirstElementChildImpl(long j2);

    static native long getLastElementChildImpl(long j2);

    static native int getChildElementCountImpl(long j2);

    static native long getElementByIdImpl(long j2, String str);

    static native long querySelectorImpl(long j2, String str);

    static native long querySelectorAllImpl(long j2, String str);

    DocumentFragmentImpl(long peer) {
        super(peer);
    }

    static DocumentFragment getImpl(long peer) {
        return (DocumentFragment) create(peer);
    }

    public HTMLCollection getChildren() {
        return HTMLCollectionImpl.getImpl(getChildrenImpl(getPeer()));
    }

    public Element getFirstElementChild() {
        return ElementImpl.getImpl(getFirstElementChildImpl(getPeer()));
    }

    public Element getLastElementChild() {
        return ElementImpl.getImpl(getLastElementChildImpl(getPeer()));
    }

    public int getChildElementCount() {
        return getChildElementCountImpl(getPeer());
    }

    public Element getElementById(String elementId) {
        return ElementImpl.getImpl(getElementByIdImpl(getPeer(), elementId));
    }

    public Element querySelector(String selectors) throws DOMException {
        return ElementImpl.getImpl(querySelectorImpl(getPeer(), selectors));
    }

    public NodeList querySelectorAll(String selectors) throws DOMException {
        return NodeListImpl.getImpl(querySelectorAllImpl(getPeer(), selectors));
    }
}
