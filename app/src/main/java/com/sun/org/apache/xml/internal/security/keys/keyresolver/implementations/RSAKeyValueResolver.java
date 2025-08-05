package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.RSAKeyValue;
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

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/RSAKeyValueResolver.class */
public class RSAKeyValueResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(RSAKeyValueResolver.class);

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) {
        if (element == null) {
            return null;
        }
        LOG.debug("Can I resolve {}", element.getTagName());
        Element elementSelectDsNode = null;
        if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYVALUE)) {
            elementSelectDsNode = XMLUtils.selectDsNode(element.getFirstChild(), Constants._TAG_RSAKEYVALUE, 0);
        } else if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_RSAKEYVALUE)) {
            elementSelectDsNode = element;
        }
        if (elementSelectDsNode == null) {
            return null;
        }
        try {
            return new RSAKeyValue(elementSelectDsNode, str).getPublicKey();
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) {
        return null;
    }
}
