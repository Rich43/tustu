package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.html.HTMLDocument;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/DOMImplementationImpl.class */
public class DOMImplementationImpl implements DOMImplementation {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native boolean hasFeatureImpl(long j2, String str, String str2);

    static native long createDocumentTypeImpl(long j2, String str, String str2, String str3);

    static native long createDocumentImpl(long j2, String str, String str2, long j3);

    static native long createCSSStyleSheetImpl(long j2, String str, String str2);

    static native long createHTMLDocumentImpl(long j2, String str);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/DOMImplementationImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            DOMImplementationImpl.dispose(this.peer);
        }
    }

    DOMImplementationImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static DOMImplementation create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new DOMImplementationImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof DOMImplementationImpl) && this.peer == ((DOMImplementationImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(DOMImplementation arg) {
        if (arg == null) {
            return 0L;
        }
        return ((DOMImplementationImpl) arg).getPeer();
    }

    static DOMImplementation getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.DOMImplementation
    public boolean hasFeature(String feature, String version) {
        return hasFeatureImpl(getPeer(), feature, version);
    }

    @Override // org.w3c.dom.DOMImplementation
    public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) throws DOMException {
        return DocumentTypeImpl.getImpl(createDocumentTypeImpl(getPeer(), qualifiedName, publicId, systemId));
    }

    @Override // org.w3c.dom.DOMImplementation
    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException {
        return DocumentImpl.getImpl(createDocumentImpl(getPeer(), namespaceURI, qualifiedName, DocumentTypeImpl.getPeer(doctype)));
    }

    public CSSStyleSheet createCSSStyleSheet(String title, String media) throws DOMException {
        return CSSStyleSheetImpl.getImpl(createCSSStyleSheetImpl(getPeer(), title, media));
    }

    public HTMLDocument createHTMLDocument(String title) {
        return HTMLDocumentImpl.getImpl(createHTMLDocumentImpl(getPeer(), title));
    }

    @Override // org.w3c.dom.DOMImplementation
    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
