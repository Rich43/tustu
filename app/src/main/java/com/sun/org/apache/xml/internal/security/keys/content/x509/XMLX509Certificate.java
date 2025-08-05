package com.sun.org.apache.xml.internal.security.keys.content.x509;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/x509/XMLX509Certificate.class */
public class XMLX509Certificate extends SignatureElementProxy implements XMLX509DataContent {
    public static final String JCA_CERT_ID = "X.509";

    public XMLX509Certificate(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public XMLX509Certificate(Document document, byte[] bArr) {
        super(document);
        addBase64Text(bArr);
    }

    public XMLX509Certificate(Document document, X509Certificate x509Certificate) throws XMLSecurityException {
        super(document);
        try {
            addBase64Text(x509Certificate.getEncoded());
        } catch (CertificateEncodingException e2) {
            throw new XMLSecurityException(e2);
        }
    }

    public byte[] getCertificateBytes() throws XMLSecurityException {
        return getBytesFromTextChild();
    }

    public X509Certificate getX509Certificate() throws XMLSecurityException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getCertificateBytes());
            Throwable th = null;
            try {
                try {
                    X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance(JCA_CERT_ID).generateCertificate(byteArrayInputStream);
                    if (x509Certificate != null) {
                        if (byteArrayInputStream != null) {
                            if (0 != 0) {
                                try {
                                    byteArrayInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                byteArrayInputStream.close();
                            }
                        }
                        return x509Certificate;
                    }
                    if (byteArrayInputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayInputStream.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            byteArrayInputStream.close();
                        }
                    }
                    return null;
                } finally {
                }
            } finally {
            }
        } catch (IOException | CertificateException e2) {
            throw new XMLSecurityException(e2);
        }
    }

    public PublicKey getPublicKey() throws IOException, XMLSecurityException {
        X509Certificate x509Certificate = getX509Certificate();
        if (x509Certificate != null) {
            return x509Certificate.getPublicKey();
        }
        return null;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof XMLX509Certificate)) {
            return false;
        }
        try {
            return Arrays.equals(((XMLX509Certificate) obj).getCertificateBytes(), getCertificateBytes());
        } catch (XMLSecurityException e2) {
            return false;
        }
    }

    public int hashCode() {
        int i2 = 17;
        try {
            for (byte b2 : getCertificateBytes()) {
                i2 = (31 * i2) + b2;
            }
        } catch (XMLSecurityException e2) {
            LOG.debug(e2.getMessage(), e2);
        }
        return i2;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_X509CERTIFICATE;
    }
}
