package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.DEREncodedKeyValue;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/DEREncodedKeyValueResolver.class */
public class DEREncodedKeyValueResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(DEREncodedKeyValueResolver.class);

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public boolean engineCanResolve(Element element, String str, StorageResolver storageResolver) {
        return XMLUtils.elementIsInSignature11Space(element, Constants._TAG_DERENCODEDKEYVALUE);
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}", element.getTagName());
        if (!engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        try {
            return new DEREncodedKeyValue(element, str).getPublicKey();
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PrivateKey engineLookupAndResolvePrivateKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }
}
