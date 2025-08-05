package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/RetrievalMethod.class */
public class RetrievalMethod extends SignatureElementProxy implements KeyInfoContent {
    public static final String TYPE_DSA = "http://www.w3.org/2000/09/xmldsig#DSAKeyValue";
    public static final String TYPE_RSA = "http://www.w3.org/2000/09/xmldsig#RSAKeyValue";
    public static final String TYPE_PGP = "http://www.w3.org/2000/09/xmldsig#PGPData";
    public static final String TYPE_SPKI = "http://www.w3.org/2000/09/xmldsig#SPKIData";
    public static final String TYPE_MGMT = "http://www.w3.org/2000/09/xmldsig#MgmtData";
    public static final String TYPE_X509 = "http://www.w3.org/2000/09/xmldsig#X509Data";
    public static final String TYPE_RAWX509 = "http://www.w3.org/2000/09/xmldsig#rawX509Certificate";

    public RetrievalMethod(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public RetrievalMethod(Document document, String str, Transforms transforms, String str2) {
        super(document);
        setLocalAttribute(Constants._ATT_URI, str);
        if (str2 != null) {
            setLocalAttribute(Constants._ATT_TYPE, str2);
        }
        if (transforms != null) {
            appendSelf(transforms);
            addReturnToSelf();
        }
    }

    public Attr getURIAttr() {
        return getElement().getAttributeNodeNS(null, Constants._ATT_URI);
    }

    public String getURI() {
        return getLocalAttribute(Constants._ATT_URI);
    }

    public String getType() {
        return getLocalAttribute(Constants._ATT_TYPE);
    }

    public Transforms getTransforms() throws XMLSecurityException {
        try {
            Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_TRANSFORMS, 0);
            if (elementSelectDsNode != null) {
                return new Transforms(elementSelectDsNode, this.baseURI);
            }
            return null;
        } catch (XMLSignatureException e2) {
            throw new XMLSecurityException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_RETRIEVALMETHOD;
    }
}
