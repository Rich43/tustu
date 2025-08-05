package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
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
import java.util.Iterator;
import javax.crypto.SecretKey;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/X509SKIResolver.class */
public class X509SKIResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(X509SKIResolver.class);

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
        if (!XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_X509DATA)) {
            LOG.debug("I can't");
            return null;
        }
        Element[] elementArrSelectDsNodes = XMLUtils.selectDsNodes(element.getFirstChild(), Constants._TAG_X509SKI);
        if (elementArrSelectDsNodes == null || elementArrSelectDsNodes.length <= 0) {
            LOG.debug("I can't");
            return null;
        }
        try {
            if (storageResolver == null) {
                KeyResolverException keyResolverException = new KeyResolverException("KeyResolver.needStorageResolver", new Object[]{Constants._TAG_X509SKI});
                LOG.debug("", keyResolverException);
                throw keyResolverException;
            }
            XMLX509SKI[] xmlx509skiArr = new XMLX509SKI[elementArrSelectDsNodes.length];
            for (int i2 = 0; i2 < elementArrSelectDsNodes.length; i2++) {
                xmlx509skiArr[i2] = new XMLX509SKI(elementArrSelectDsNodes[i2], str);
            }
            Iterator<Certificate> iterator = storageResolver.getIterator();
            while (iterator.hasNext()) {
                X509Certificate x509Certificate = (X509Certificate) iterator.next();
                XMLX509SKI xmlx509ski = new XMLX509SKI(element.getOwnerDocument(), x509Certificate);
                for (XMLX509SKI xmlx509ski2 : xmlx509skiArr) {
                    if (xmlx509ski.equals(xmlx509ski2)) {
                        LOG.debug("Return PublicKey from {}", x509Certificate.getSubjectX500Principal().getName());
                        return x509Certificate;
                    }
                }
            }
            return null;
        } catch (XMLSecurityException e2) {
            throw new KeyResolverException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) {
        return null;
    }
}
