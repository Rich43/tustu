package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.Signature11ElementProxy;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/KeyInfoReference.class */
public class KeyInfoReference extends Signature11ElementProxy implements KeyInfoContent {
    public KeyInfoReference(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public KeyInfoReference(Document document, String str) {
        super(document);
        setLocalAttribute(Constants._ATT_URI, str);
    }

    public Attr getURIAttr() {
        return getElement().getAttributeNodeNS(null, Constants._ATT_URI);
    }

    public String getURI() {
        return getURIAttr().getNodeValue();
    }

    public void setId(String str) {
        setLocalIdAttribute(Constants._ATT_ID, str);
    }

    public String getId() {
        return getLocalAttribute(Constants._ATT_ID);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_KEYINFOREFERENCE;
    }
}
