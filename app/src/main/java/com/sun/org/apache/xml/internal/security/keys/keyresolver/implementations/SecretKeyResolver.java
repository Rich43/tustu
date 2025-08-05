package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/SecretKeyResolver.class */
public class SecretKeyResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(SecretKeyResolver.class);
    private KeyStore keyStore;
    private char[] password;

    public SecretKeyResolver(KeyStore keyStore, char[] cArr) {
        this.keyStore = keyStore;
        this.password = cArr;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public boolean engineCanResolve(Element element, String str, StorageResolver storageResolver) {
        return XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME);
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineResolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}?", element.getTagName());
        if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME)) {
            try {
                Key key = this.keyStore.getKey(element.getFirstChild().getNodeValue(), this.password);
                if (key instanceof SecretKey) {
                    return (SecretKey) key;
                }
            } catch (Exception e2) {
                LOG.debug("Cannot recover the key", e2);
            }
        }
        LOG.debug("I can't");
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PrivateKey engineLookupAndResolvePrivateKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }
}
