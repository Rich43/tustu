package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/X509CertificateResolver.class */
public class X509CertificateResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(X509CertificateResolver.class);

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
        try {
            Element[] elementArrSelectDsNodes = XMLUtils.selectDsNodes(element.getFirstChild(), Constants._TAG_X509CERTIFICATE);
            if (elementArrSelectDsNodes == null || elementArrSelectDsNodes.length == 0) {
                Element elementSelectDsNode = XMLUtils.selectDsNode(element.getFirstChild(), Constants._TAG_X509DATA, 0);
                if (elementSelectDsNode != null) {
                    return engineLookupResolveX509Certificate(elementSelectDsNode, str, storageResolver);
                }
                return null;
            }
            for (Element element2 : elementArrSelectDsNodes) {
                X509Certificate x509Certificate = new XMLX509Certificate(element2, str).getX509Certificate();
                if (x509Certificate != null) {
                    return x509Certificate;
                }
            }
            return null;
        } catch (XMLSecurityException e2) {
            LOG.debug("Security Exception", e2);
            throw new KeyResolverException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) {
        return null;
    }
}
