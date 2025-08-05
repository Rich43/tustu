package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509IssuerSerial;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import javax.crypto.SecretKey;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/X509IssuerSerialResolver.class */
public class X509IssuerSerialResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(X509IssuerSerialResolver.class);

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        X509Certificate x509CertificateEngineLookupResolveX509Certificate = engineLookupResolveX509Certificate(element, str, storageResolver);
        if (x509CertificateEngineLookupResolveX509Certificate != null) {
            return x509CertificateEngineLookupResolveX509Certificate.getPublicKey();
        }
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}?", element.getTagName());
        try {
            X509Data x509Data = new X509Data(element, str);
            if (!x509Data.containsIssuerSerial()) {
                return null;
            }
            try {
                if (storageResolver == null) {
                    KeyResolverException keyResolverException = new KeyResolverException("KeyResolver.needStorageResolver", new Object[]{Constants._TAG_X509ISSUERSERIAL});
                    LOG.debug("", keyResolverException);
                    throw keyResolverException;
                }
                int iLengthIssuerSerial = x509Data.lengthIssuerSerial();
                Iterator<Certificate> iterator = storageResolver.getIterator();
                while (iterator.hasNext()) {
                    X509Certificate x509Certificate = (X509Certificate) iterator.next();
                    XMLX509IssuerSerial xMLX509IssuerSerial = new XMLX509IssuerSerial(element.getOwnerDocument(), x509Certificate);
                    LOG.debug("Found Certificate Issuer: {}", xMLX509IssuerSerial.getIssuerName());
                    LOG.debug("Found Certificate Serial: {}", xMLX509IssuerSerial.getSerialNumber().toString());
                    for (int i2 = 0; i2 < iLengthIssuerSerial; i2++) {
                        XMLX509IssuerSerial xMLX509IssuerSerialItemIssuerSerial = x509Data.itemIssuerSerial(i2);
                        LOG.debug("Found Element Issuer:     {}", xMLX509IssuerSerialItemIssuerSerial.getIssuerName());
                        LOG.debug("Found Element Serial:     {}", xMLX509IssuerSerialItemIssuerSerial.getSerialNumber().toString());
                        if (xMLX509IssuerSerial.equals(xMLX509IssuerSerialItemIssuerSerial)) {
                            LOG.debug("match !!! ");
                            return x509Certificate;
                        }
                        LOG.debug("no match...");
                    }
                }
                return null;
            } catch (XMLSecurityException e2) {
                LOG.debug("XMLSecurityException", e2);
                throw new KeyResolverException(e2);
            }
        } catch (XMLSignatureException e3) {
            LOG.debug("I can't");
            return null;
        } catch (XMLSecurityException e4) {
            LOG.debug("I can't");
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) {
        return null;
    }
}
