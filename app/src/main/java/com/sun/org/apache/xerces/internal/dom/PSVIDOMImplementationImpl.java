package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.impl.Constants;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/PSVIDOMImplementationImpl.class */
public class PSVIDOMImplementationImpl extends CoreDOMImplementationImpl {
    static PSVIDOMImplementationImpl singleton = new PSVIDOMImplementationImpl();

    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDOMImplementationImpl, org.w3c.dom.DOMImplementation
    public boolean hasFeature(String feature, String version) {
        return super.hasFeature(feature, version) || feature.equalsIgnoreCase(Constants.DOM_PSVI);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDOMImplementationImpl, org.w3c.dom.DOMImplementation
    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException {
        if (doctype != null && doctype.getOwnerDocument() != null) {
            throw new DOMException((short) 4, DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "WRONG_DOCUMENT_ERR", null));
        }
        DocumentImpl doc = new PSVIDocumentImpl(doctype);
        Element e2 = doc.createElementNS(namespaceURI, qualifiedName);
        doc.appendChild(e2);
        return doc;
    }
}
