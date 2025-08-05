package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/SignatureProperty.class */
public class SignatureProperty extends SignatureElementProxy {
    public SignatureProperty(Document document, String str) {
        this(document, str, null);
    }

    public SignatureProperty(Document document, String str, String str2) {
        super(document);
        setTarget(str);
        setId(str2);
    }

    public SignatureProperty(Element element, String str) throws XMLSecurityException {
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

    public void setTarget(String str) {
        if (str != null) {
            setLocalAttribute(Constants._ATT_TARGET, str);
        }
    }

    public String getTarget() {
        return getLocalAttribute(Constants._ATT_TARGET);
    }

    public Node appendChild(Node node) {
        appendSelf(node);
        return node;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_SIGNATUREPROPERTY;
    }
}
