package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Digest;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import javax.crypto.SecretKey;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/X509DigestResolver.class */
public class X509DigestResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(X509DigestResolver.class);

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public boolean engineCanResolve(Element element, String str, StorageResolver storageResolver) {
        if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_X509DATA)) {
            try {
                return new X509Data(element, str).containsDigest();
            } catch (XMLSecurityException e2) {
                return false;
            }
        }
        return false;
    }

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
        LOG.debug("Can I resolve {}", element.getTagName());
        if (!engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        try {
            return resolveCertificate(element, str, storageResolver);
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    private X509Certificate resolveCertificate(Element element, String str, StorageResolver storageResolver) throws XMLSecurityException {
        Element[] elementArrSelectDs11Nodes = XMLUtils.selectDs11Nodes(element.getFirstChild(), Constants._TAG_X509DIGEST);
        if (elementArrSelectDs11Nodes == null || elementArrSelectDs11Nodes.length <= 0) {
            return null;
        }
        try {
            checkStorage(storageResolver);
            XMLX509Digest[] xMLX509DigestArr = new XMLX509Digest[elementArrSelectDs11Nodes.length];
            for (int i2 = 0; i2 < elementArrSelectDs11Nodes.length; i2++) {
                xMLX509DigestArr[i2] = new XMLX509Digest(elementArrSelectDs11Nodes[i2], str);
            }
            Iterator<Certificate> iterator = storageResolver.getIterator();
            while (iterator.hasNext()) {
                X509Certificate x509Certificate = (X509Certificate) iterator.next();
                for (XMLX509Digest xMLX509Digest : xMLX509DigestArr) {
                    if (Arrays.equals(xMLX509Digest.getDigestBytes(), XMLX509Digest.getDigestBytesFromCert(x509Certificate, xMLX509Digest.getAlgorithm()))) {
                        LOG.debug("Found certificate with: {}", x509Certificate.getSubjectX500Principal().getName());
                        return x509Certificate;
                    }
                }
            }
            return null;
        } catch (XMLSecurityException e2) {
            throw new KeyResolverException(e2);
        }
    }

    private void checkStorage(StorageResolver storageResolver) throws KeyResolverException {
        if (storageResolver == null) {
            KeyResolverException keyResolverException = new KeyResolverException("KeyResolver.needStorageResolver", new Object[]{Constants._TAG_X509DIGEST});
            LOG.debug("", keyResolverException);
            throw keyResolverException;
        }
    }
}
