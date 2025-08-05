package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/SignatureElementProxy.class */
public abstract class SignatureElementProxy extends ElementProxy {
    protected SignatureElementProxy() {
    }

    public SignatureElementProxy(Document document) {
        if (document == null) {
            throw new RuntimeException("Document is null");
        }
        setDocument(document);
        setElement(XMLUtils.createElementInSignatureSpace(document, getBaseLocalName()));
    }

    public SignatureElementProxy(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseNamespace() {
        return "http://www.w3.org/2000/09/xmldsig#";
    }
}
