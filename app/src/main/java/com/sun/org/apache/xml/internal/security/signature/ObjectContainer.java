package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/ObjectContainer.class */
public class ObjectContainer extends SignatureElementProxy {
    public ObjectContainer(Document document) {
        super(document);
    }

    public ObjectContainer(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public void setId(String str) {
        if (str != null) {
            setLocalIdAttribute(Constants._ATT_ID, str);
        }
    }

    public String getId() {
        return getLocalAttribute(Constants._ATT_ID);
    }

    public void setMimeType(String str) {
        if (str != null) {
            setLocalAttribute(Constants._ATT_MIMETYPE, str);
        }
    }

    public String getMimeType() {
        return getLocalAttribute(Constants._ATT_MIMETYPE);
    }

    public void setEncoding(String str) {
        if (str != null) {
            setLocalAttribute(Constants._ATT_ENCODING, str);
        }
    }

    public String getEncoding() {
        return getLocalAttribute(Constants._ATT_ENCODING);
    }

    public Node appendChild(Node node) {
        appendSelf(node);
        return node;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return "Object";
    }
}
