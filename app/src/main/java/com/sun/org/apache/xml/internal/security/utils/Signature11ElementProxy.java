package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/Signature11ElementProxy.class */
public abstract class Signature11ElementProxy extends ElementProxy {
    protected Signature11ElementProxy() {
    }

    public Signature11ElementProxy(Document document) throws DOMException {
        if (document == null) {
            throw new RuntimeException("Document is null");
        }
        setDocument(document);
        setElement(XMLUtils.createElementInSignature11Space(document, getBaseLocalName()));
        String defaultPrefix = ElementProxy.getDefaultPrefix(getBaseNamespace());
        if (defaultPrefix == null || defaultPrefix.length() == 0) {
            getElement().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", getBaseNamespace());
        } else {
            getElement().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + defaultPrefix, getBaseNamespace());
        }
    }

    public Signature11ElementProxy(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseNamespace() {
        return Constants.SignatureSpec11NS;
    }
}
