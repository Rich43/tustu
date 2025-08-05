package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

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

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/SingleKeyResolver.class */
public class SingleKeyResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(SingleKeyResolver.class);
    private String keyName;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private SecretKey secretKey;

    public SingleKeyResolver(String str, PublicKey publicKey) {
        this.keyName = str;
        this.publicKey = publicKey;
    }

    public SingleKeyResolver(String str, PrivateKey privateKey) {
        this.keyName = str;
        this.privateKey = privateKey;
    }

    public SingleKeyResolver(String str, SecretKey secretKey) {
        this.keyName = str;
        this.secretKey = secretKey;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public boolean engineCanResolve(Element element, String str, StorageResolver storageResolver) {
        return XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME);
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}?", element.getTagName());
        if (this.publicKey != null && XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME) && this.keyName.equals(element.getFirstChild().getNodeValue())) {
            return this.publicKey;
        }
        LOG.debug("I can't");
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineResolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}?", element.getTagName());
        if (this.secretKey != null && XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME) && this.keyName.equals(element.getFirstChild().getNodeValue())) {
            return this.secretKey;
        }
        LOG.debug("I can't");
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PrivateKey engineLookupAndResolvePrivateKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}?", element.getTagName());
        if (this.privateKey != null && XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME) && this.keyName.equals(element.getFirstChild().getNodeValue())) {
            return this.privateKey;
        }
        LOG.debug("I can't");
        return null;
    }
}
