package com.sun.webkit.dom;

import org.w3c.dom.Entity;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/EntityImpl.class */
public class EntityImpl extends NodeImpl implements Entity {
    static native String getPublicIdImpl(long j2);

    static native String getSystemIdImpl(long j2);

    static native String getNotationNameImpl(long j2);

    EntityImpl(long peer) {
        super(peer);
    }

    static Entity getImpl(long peer) {
        return (Entity) create(peer);
    }

    @Override // org.w3c.dom.Entity
    public String getPublicId() {
        return getPublicIdImpl(getPeer());
    }

    @Override // org.w3c.dom.Entity
    public String getSystemId() {
        return getSystemIdImpl(getPeer());
    }

    @Override // org.w3c.dom.Entity
    public String getNotationName() {
        return getNotationNameImpl(getPeer());
    }

    @Override // org.w3c.dom.Entity
    public String getInputEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Entity
    public String getXmlVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Entity
    public String getXmlEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
