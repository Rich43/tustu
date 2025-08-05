package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/DocumentTypeImpl.class */
public class DocumentTypeImpl extends NodeImpl implements DocumentType {
    static native String getNameImpl(long j2);

    static native long getEntitiesImpl(long j2);

    static native long getNotationsImpl(long j2);

    static native String getPublicIdImpl(long j2);

    static native String getSystemIdImpl(long j2);

    static native String getInternalSubsetImpl(long j2);

    static native void removeImpl(long j2);

    DocumentTypeImpl(long peer) {
        super(peer);
    }

    static DocumentType getImpl(long peer) {
        return (DocumentType) create(peer);
    }

    @Override // org.w3c.dom.DocumentType
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.DocumentType
    public NamedNodeMap getEntities() {
        return NamedNodeMapImpl.getImpl(getEntitiesImpl(getPeer()));
    }

    @Override // org.w3c.dom.DocumentType
    public NamedNodeMap getNotations() {
        return NamedNodeMapImpl.getImpl(getNotationsImpl(getPeer()));
    }

    @Override // org.w3c.dom.DocumentType
    public String getPublicId() {
        return getPublicIdImpl(getPeer());
    }

    @Override // org.w3c.dom.DocumentType
    public String getSystemId() {
        return getSystemIdImpl(getPeer());
    }

    @Override // org.w3c.dom.DocumentType
    public String getInternalSubset() {
        return getInternalSubsetImpl(getPeer());
    }

    public void remove() throws DOMException {
        removeImpl(getPeer());
    }
}
