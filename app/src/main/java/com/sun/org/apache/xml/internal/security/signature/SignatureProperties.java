package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/SignatureProperties.class */
public class SignatureProperties extends SignatureElementProxy {
    public SignatureProperties(Document document) {
        super(document);
        addReturnToSelf();
    }

    public SignatureProperties(Element element, String str) throws DOMException, XMLSecurityException {
        super(element, str);
        Attr attributeNodeNS = element.getAttributeNodeNS(null, Constants._ATT_ID);
        if (attributeNodeNS != null) {
            element.setIdAttributeNode(attributeNodeNS, true);
        }
        int length = getLength();
        for (int i2 = 0; i2 < length; i2++) {
            Element elementSelectDsNode = XMLUtils.selectDsNode(getElement(), Constants._TAG_SIGNATUREPROPERTY, i2);
            Attr attributeNodeNS2 = elementSelectDsNode.getAttributeNodeNS(null, Constants._ATT_ID);
            if (attributeNodeNS2 != null) {
                elementSelectDsNode.setIdAttributeNode(attributeNodeNS2, true);
            }
        }
    }

    public int getLength() {
        return XMLUtils.selectDsNodes(getElement(), Constants._TAG_SIGNATUREPROPERTY).length;
    }

    public SignatureProperty item(int i2) throws XMLSignatureException {
        try {
            Element elementSelectDsNode = XMLUtils.selectDsNode(getElement(), Constants._TAG_SIGNATUREPROPERTY, i2);
            if (elementSelectDsNode == null) {
                return null;
            }
            return new SignatureProperty(elementSelectDsNode, this.baseURI);
        } catch (XMLSecurityException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    public void setId(String str) {
        if (str != null) {
            setLocalIdAttribute(Constants._ATT_ID, str);
        }
    }

    public String getId() {
        return getLocalAttribute(Constants._ATT_ID);
    }

    public void addSignatureProperty(SignatureProperty signatureProperty) {
        appendSelf(signatureProperty);
        addReturnToSelf();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_SIGNATUREPROPERTIES;
    }
}
