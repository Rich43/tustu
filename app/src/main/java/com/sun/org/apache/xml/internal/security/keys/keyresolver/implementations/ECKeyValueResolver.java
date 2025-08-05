package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.ECKeyValue;
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

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/ECKeyValueResolver.class */
public class ECKeyValueResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(ECKeyValueResolver.class);

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) {
        if (element == null) {
            return null;
        }
        Element elementSelectDs11Node = null;
        if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYVALUE)) {
            elementSelectDs11Node = XMLUtils.selectDs11Node(element.getFirstChild(), Constants._TAG_ECKEYVALUE, 0);
        } else if (XMLUtils.elementIsInSignature11Space(element, Constants._TAG_ECKEYVALUE)) {
            elementSelectDs11Node = element;
        }
        if (elementSelectDs11Node == null) {
            return null;
        }
        try {
            return new ECKeyValue(elementSelectDs11Node, str).getPublicKey();
        } catch (XMLSecurityException e2) {
            LOG.debug(e2.getMessage(), e2);
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
