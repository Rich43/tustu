package com.sun.org.apache.xml.internal.security.keys.content.x509;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/x509/XMLX509CRL.class */
public class XMLX509CRL extends SignatureElementProxy implements XMLX509DataContent {
    public XMLX509CRL(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public XMLX509CRL(Document document, byte[] bArr) {
        super(document);
        addBase64Text(bArr);
    }

    public byte[] getCRLBytes() throws XMLSecurityException {
        return getBytesFromTextChild();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_X509CRL;
    }
}
