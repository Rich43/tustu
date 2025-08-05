package com.sun.org.apache.xml.internal.security.keys.content.x509;

import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.Signature11ElementProxy;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/x509/XMLX509Digest.class */
public class XMLX509Digest extends Signature11ElementProxy implements XMLX509DataContent {
    public XMLX509Digest(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public XMLX509Digest(Document document, byte[] bArr, String str) {
        super(document);
        addBase64Text(bArr);
        setLocalAttribute(Constants._ATT_ALGORITHM, str);
    }

    public XMLX509Digest(Document document, X509Certificate x509Certificate, String str) throws XMLSecurityException {
        super(document);
        addBase64Text(getDigestBytesFromCert(x509Certificate, str));
        setLocalAttribute(Constants._ATT_ALGORITHM, str);
    }

    public Attr getAlgorithmAttr() {
        return getElement().getAttributeNodeNS(null, Constants._ATT_ALGORITHM);
    }

    public String getAlgorithm() {
        return getAlgorithmAttr().getNodeValue();
    }

    public byte[] getDigestBytes() throws XMLSecurityException {
        return getBytesFromTextChild();
    }

    public static byte[] getDigestBytesFromCert(X509Certificate x509Certificate, String str) throws XMLSecurityException {
        String strTranslateURItoJCEID = JCEMapper.translateURItoJCEID(str);
        if (strTranslateURItoJCEID == null) {
            throw new XMLSecurityException("XMLX509Digest.UnknownDigestAlgorithm", new Object[]{str});
        }
        try {
            return MessageDigest.getInstance(strTranslateURItoJCEID).digest(x509Certificate.getEncoded());
        } catch (Exception e2) {
            throw new XMLSecurityException("XMLX509Digest.FailedDigest", new Object[]{strTranslateURItoJCEID});
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_X509DIGEST;
    }
}
