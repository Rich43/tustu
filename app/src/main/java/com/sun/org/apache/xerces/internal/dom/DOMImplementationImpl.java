package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.utils.ConfigurationError;
import java.util.MissingResourceException;
import org.slf4j.Marker;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMImplementationImpl.class */
public class DOMImplementationImpl extends CoreDOMImplementationImpl implements DOMImplementation {
    static DOMImplementationImpl singleton = new DOMImplementationImpl();

    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDOMImplementationImpl, org.w3c.dom.DOMImplementation
    public boolean hasFeature(String feature, String version) throws ConfigurationError {
        boolean result = super.hasFeature(feature, version);
        if (!result) {
            boolean anyVersion = version == null || version.length() == 0;
            if (feature.startsWith(Marker.ANY_NON_NULL_MARKER)) {
                feature = feature.substring(1);
            }
            return (feature.equalsIgnoreCase("Events") && (anyVersion || version.equals("2.0"))) || (feature.equalsIgnoreCase("MutationEvents") && (anyVersion || version.equals("2.0"))) || ((feature.equalsIgnoreCase("Traversal") && (anyVersion || version.equals("2.0"))) || ((feature.equalsIgnoreCase("Range") && (anyVersion || version.equals("2.0"))) || (feature.equalsIgnoreCase("MutationEvents") && (anyVersion || version.equals("2.0")))));
        }
        return result;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDOMImplementationImpl, org.w3c.dom.DOMImplementation
    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException, MissingResourceException {
        if (namespaceURI == null && qualifiedName == null && doctype == null) {
            return new DocumentImpl();
        }
        if (doctype != null && doctype.getOwnerDocument() != null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
            throw new DOMException((short) 4, msg);
        }
        DocumentImpl doc = new DocumentImpl(doctype);
        Element e2 = doc.createElementNS(namespaceURI, qualifiedName);
        doc.appendChild(e2);
        return doc;
    }
}
