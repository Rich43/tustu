package com.sun.webkit.dom;

import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSValueListImpl.class */
public class CSSValueListImpl extends CSSValueImpl implements CSSValueList {
    static native int getLengthImpl(long j2);

    static native long itemImpl(long j2, int i2);

    CSSValueListImpl(long peer) {
        super(peer);
    }

    static CSSValueList getImpl(long peer) {
        return (CSSValueList) create(peer);
    }

    @Override // org.w3c.dom.css.CSSValueList
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSValueList
    public CSSValue item(int index) {
        return CSSValueImpl.getImpl(itemImpl(getPeer(), index));
    }
}
