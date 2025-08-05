package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLMapElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLMapElementImpl.class */
public class HTMLMapElementImpl extends HTMLElementImpl implements HTMLMapElement {
    static native long getAreasImpl(long j2);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    HTMLMapElementImpl(long peer) {
        super(peer);
    }

    static HTMLMapElement getImpl(long peer) {
        return (HTMLMapElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLMapElement
    public HTMLCollection getAreas() {
        return HTMLCollectionImpl.getImpl(getAreasImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLMapElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLMapElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }
}
