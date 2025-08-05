package com.sun.org.apache.xml.internal.security.keys.content.x509;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/x509/XMLX509SKI.class */
public class XMLX509SKI extends SignatureElementProxy implements XMLX509DataContent {
    private static final Logger LOG = LoggerFactory.getLogger(XMLX509SKI.class);
    public static final String SKI_OID = "2.5.29.14";

    public XMLX509SKI(Document document, byte[] bArr) {
        super(document);
        addBase64Text(bArr);
    }

    public XMLX509SKI(Document document, X509Certificate x509Certificate) throws XMLSecurityException {
        super(document);
        addBase64Text(getSKIBytesFromCert(x509Certificate));
    }

    public XMLX509SKI(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public byte[] getSKIBytes() throws XMLSecurityException {
        return getBytesFromTextChild();
    }

    public static byte[] getSKIBytesFromCert(X509Certificate x509Certificate) throws XMLSecurityException {
        if (x509Certificate.getVersion() < 3) {
            throw new XMLSecurityException("certificate.noSki.lowVersion", new Object[]{Integer.valueOf(x509Certificate.getVersion())});
        }
        byte[] extensionValue = x509Certificate.getExtensionValue(SKI_OID);
        if (extensionValue == null) {
            throw new XMLSecurityException("certificate.noSki.null");
        }
        byte[] bArr = new byte[extensionValue.length - 4];
        System.arraycopy(extensionValue, 4, bArr, 0, bArr.length);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Base64 of SKI is " + XMLUtils.encodeToString(bArr));
        }
        return bArr;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof XMLX509SKI)) {
            return false;
        }
        try {
            return Arrays.equals(((XMLX509SKI) obj).getSKIBytes(), getSKIBytes());
        } catch (XMLSecurityException e2) {
            return false;
        }
    }

    public int hashCode() {
        int i2 = 17;
        try {
            for (byte b2 : getSKIBytes()) {
                i2 = (31 * i2) + b2;
            }
        } catch (XMLSecurityException e2) {
            LOG.debug(e2.getMessage(), e2);
        }
        return i2;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_X509SKI;
    }
}
