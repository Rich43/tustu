package com.sun.webkit.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/AttrImpl.class */
public class AttrImpl extends NodeImpl implements Attr {
    static native String getNameImpl(long j2);

    static native boolean getSpecifiedImpl(long j2);

    static native String getValueImpl(long j2);

    static native void setValueImpl(long j2, String str);

    static native long getOwnerElementImpl(long j2);

    static native boolean isIdImpl(long j2);

    AttrImpl(long peer) {
        super(peer);
    }

    static Attr getImpl(long peer) {
        return (Attr) create(peer);
    }

    @Override // org.w3c.dom.Attr
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.Attr
    public boolean getSpecified() {
        return getSpecifiedImpl(getPeer());
    }

    @Override // org.w3c.dom.Attr
    public String getValue() {
        return getValueImpl(getPeer());
    }

    @Override // org.w3c.dom.Attr
    public void setValue(String value) throws DOMException {
        setValueImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.Attr
    public Element getOwnerElement() {
        return ElementImpl.getImpl(getOwnerElementImpl(getPeer()));
    }

    @Override // org.w3c.dom.Attr
    public boolean isId() {
        return isIdImpl(getPeer());
    }

    @Override // org.w3c.dom.Attr
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
