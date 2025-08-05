package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
import com.sun.org.apache.xml.internal.security.keys.content.KeyInfoReference;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/KeyInfoReferenceResolver.class */
public class KeyInfoReferenceResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(KeyInfoReferenceResolver.class);

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public boolean engineCanResolve(Element element, String str, StorageResolver storageResolver) {
        return XMLUtils.elementIsInSignature11Space(element, Constants._TAG_KEYINFOREFERENCE);
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}", element.getTagName());
        if (!engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        try {
            KeyInfo keyInfoResolveReferentKeyInfo = resolveReferentKeyInfo(element, str, storageResolver);
            if (keyInfoResolveReferentKeyInfo != null) {
                return keyInfoResolveReferentKeyInfo.getPublicKey();
            }
            return null;
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}", element.getTagName());
        if (!engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        try {
            KeyInfo keyInfoResolveReferentKeyInfo = resolveReferentKeyInfo(element, str, storageResolver);
            if (keyInfoResolveReferentKeyInfo != null) {
                return keyInfoResolveReferentKeyInfo.getX509Certificate();
            }
            return null;
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}", element.getTagName());
        if (!engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        try {
            KeyInfo keyInfoResolveReferentKeyInfo = resolveReferentKeyInfo(element, str, storageResolver);
            if (keyInfoResolveReferentKeyInfo != null) {
                return keyInfoResolveReferentKeyInfo.getSecretKey();
            }
            return null;
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PrivateKey engineLookupAndResolvePrivateKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve " + element.getTagName());
        if (!engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        try {
            KeyInfo keyInfoResolveReferentKeyInfo = resolveReferentKeyInfo(element, str, storageResolver);
            if (keyInfoResolveReferentKeyInfo != null) {
                return keyInfoResolveReferentKeyInfo.getPrivateKey();
            }
            return null;
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        }
    }

    private KeyInfo resolveReferentKeyInfo(Element element, String str, StorageResolver storageResolver) throws XMLSecurityException {
        Attr uRIAttr = new KeyInfoReference(element, str).getURIAttr();
        try {
            Element elementObtainReferenceElement = obtainReferenceElement(resolveInput(uRIAttr, str, this.secureValidation));
            if (elementObtainReferenceElement == null) {
                LOG.debug("De-reference of KeyInfoReference URI returned null: {}", uRIAttr.getValue());
                return null;
            }
            validateReference(elementObtainReferenceElement);
            KeyInfo keyInfo = new KeyInfo(elementObtainReferenceElement, str);
            keyInfo.setSecureValidation(this.secureValidation);
            keyInfo.addStorageResolver(storageResolver);
            return keyInfo;
        } catch (Exception e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        }
    }

    private void validateReference(Element element) throws XMLSecurityException {
        if (!XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYINFO)) {
            throw new XMLSecurityException("KeyInfoReferenceResolver.InvalidReferentElement.WrongType", new Object[]{new QName(element.getNamespaceURI(), element.getLocalName())});
        }
        KeyInfo keyInfo = new KeyInfo(element, "");
        if (keyInfo.containsKeyInfoReference() || keyInfo.containsRetrievalMethod()) {
            if (this.secureValidation) {
                throw new XMLSecurityException("KeyInfoReferenceResolver.InvalidReferentElement.ReferenceWithSecure");
            }
            throw new XMLSecurityException("KeyInfoReferenceResolver.InvalidReferentElement.ReferenceWithoutSecure");
        }
    }

    private XMLSignatureInput resolveInput(Attr attr, String str, boolean z2) throws XMLSecurityException {
        return ResourceResolver.getInstance(attr, str, z2).resolve(attr, str, z2);
    }

    private Element obtainReferenceElement(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, KeyResolverException, ParserConfigurationException, SAXException, IOException {
        Element docFromBytes;
        if (xMLSignatureInput.isElement()) {
            docFromBytes = (Element) xMLSignatureInput.getSubNode();
        } else {
            if (xMLSignatureInput.isNodeSet()) {
                LOG.debug("De-reference of KeyInfoReference returned an unsupported NodeSet");
                return null;
            }
            docFromBytes = getDocFromBytes(xMLSignatureInput.getBytes(), this.secureValidation);
        }
        return docFromBytes;
    }
}
