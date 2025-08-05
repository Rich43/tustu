package com.sun.org.apache.xml.internal.security.keys;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.DEREncodedKeyValue;
import com.sun.org.apache.xml.internal.security.keys.content.KeyInfoReference;
import com.sun.org.apache.xml.internal.security.keys.content.KeyName;
import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;
import com.sun.org.apache.xml.internal.security.keys.content.MgmtData;
import com.sun.org.apache.xml.internal.security.keys.content.PGPData;
import com.sun.org.apache.xml.internal.security.keys.content.RetrievalMethod;
import com.sun.org.apache.xml.internal.security.keys.content.SPKIData;
import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.DSAKeyValue;
import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.RSAKeyValue;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.crypto.SecretKey;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/KeyInfo.class */
public class KeyInfo extends SignatureElementProxy {
    private static final Logger LOG = LoggerFactory.getLogger(KeyInfo.class);
    private List<X509Data> x509Datas;
    private static final List<StorageResolver> nullList;
    private List<StorageResolver> storageResolvers;
    private List<KeyResolverSpi> internalKeyResolvers;
    private boolean secureValidation;

    static {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(null);
        nullList = Collections.unmodifiableList(arrayList);
    }

    public KeyInfo(Document document) throws DOMException {
        super(document);
        this.storageResolvers = nullList;
        this.internalKeyResolvers = new ArrayList();
        addReturnToSelf();
        String defaultPrefix = ElementProxy.getDefaultPrefix(getBaseNamespace());
        if (defaultPrefix != null && defaultPrefix.length() > 0) {
            getElement().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + defaultPrefix, getBaseNamespace());
        }
    }

    public KeyInfo(Element element, String str) throws DOMException, XMLSecurityException {
        super(element, str);
        this.storageResolvers = nullList;
        this.internalKeyResolvers = new ArrayList();
        Attr attributeNodeNS = element.getAttributeNodeNS(null, Constants._ATT_ID);
        if (attributeNodeNS != null) {
            element.setIdAttributeNode(attributeNodeNS, true);
        }
    }

    public void setSecureValidation(boolean z2) {
        this.secureValidation = z2;
    }

    public void setId(String str) {
        if (str != null) {
            setLocalIdAttribute(Constants._ATT_ID, str);
        }
    }

    public String getId() {
        return getLocalAttribute(Constants._ATT_ID);
    }

    public void addKeyName(String str) {
        add(new KeyName(getDocument(), str));
    }

    public void add(KeyName keyName) {
        appendSelf(keyName);
        addReturnToSelf();
    }

    public void addKeyValue(PublicKey publicKey) {
        add(new KeyValue(getDocument(), publicKey));
    }

    public void addKeyValue(Element element) {
        add(new KeyValue(getDocument(), element));
    }

    public void add(DSAKeyValue dSAKeyValue) {
        add(new KeyValue(getDocument(), dSAKeyValue));
    }

    public void add(RSAKeyValue rSAKeyValue) {
        add(new KeyValue(getDocument(), rSAKeyValue));
    }

    public void add(PublicKey publicKey) {
        add(new KeyValue(getDocument(), publicKey));
    }

    public void add(KeyValue keyValue) {
        appendSelf(keyValue);
        addReturnToSelf();
    }

    public void addMgmtData(String str) {
        add(new MgmtData(getDocument(), str));
    }

    public void add(MgmtData mgmtData) {
        appendSelf(mgmtData);
        addReturnToSelf();
    }

    public void add(PGPData pGPData) {
        appendSelf(pGPData);
        addReturnToSelf();
    }

    public void addRetrievalMethod(String str, Transforms transforms, String str2) {
        add(new RetrievalMethod(getDocument(), str, transforms, str2));
    }

    public void add(RetrievalMethod retrievalMethod) {
        appendSelf(retrievalMethod);
        addReturnToSelf();
    }

    public void add(SPKIData sPKIData) {
        appendSelf(sPKIData);
        addReturnToSelf();
    }

    public void add(X509Data x509Data) {
        if (this.x509Datas == null) {
            this.x509Datas = new ArrayList();
        }
        this.x509Datas.add(x509Data);
        appendSelf(x509Data);
        addReturnToSelf();
    }

    public void addDEREncodedKeyValue(PublicKey publicKey) throws XMLSecurityException {
        add(new DEREncodedKeyValue(getDocument(), publicKey));
    }

    public void add(DEREncodedKeyValue dEREncodedKeyValue) {
        appendSelf(dEREncodedKeyValue);
        addReturnToSelf();
    }

    public void addKeyInfoReference(String str) throws XMLSecurityException {
        add(new KeyInfoReference(getDocument(), str));
    }

    public void add(KeyInfoReference keyInfoReference) {
        appendSelf(keyInfoReference);
        addReturnToSelf();
    }

    public void addUnknownElement(Element element) {
        appendSelf(element);
        addReturnToSelf();
    }

    public int lengthKeyName() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_KEYNAME);
    }

    public int lengthKeyValue() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_KEYVALUE);
    }

    public int lengthMgmtData() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_MGMTDATA);
    }

    public int lengthPGPData() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_PGPDATA);
    }

    public int lengthRetrievalMethod() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_RETRIEVALMETHOD);
    }

    public int lengthSPKIData() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_SPKIDATA);
    }

    public int lengthX509Data() {
        if (this.x509Datas != null) {
            return this.x509Datas.size();
        }
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_X509DATA);
    }

    public int lengthDEREncodedKeyValue() {
        return length(Constants.SignatureSpec11NS, Constants._TAG_DERENCODEDKEYVALUE);
    }

    public int lengthKeyInfoReference() {
        return length(Constants.SignatureSpec11NS, Constants._TAG_KEYINFOREFERENCE);
    }

    public int lengthUnknownElement() {
        int i2 = 0;
        Node firstChild = getElement().getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 1 && node.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) {
                    i2++;
                }
                firstChild = node.getNextSibling();
            } else {
                return i2;
            }
        }
    }

    public KeyName itemKeyName(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_KEYNAME, i2);
        if (elementSelectDsNode != null) {
            return new KeyName(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public KeyValue itemKeyValue(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_KEYVALUE, i2);
        if (elementSelectDsNode != null) {
            return new KeyValue(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public MgmtData itemMgmtData(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_MGMTDATA, i2);
        if (elementSelectDsNode != null) {
            return new MgmtData(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public PGPData itemPGPData(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_PGPDATA, i2);
        if (elementSelectDsNode != null) {
            return new PGPData(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public RetrievalMethod itemRetrievalMethod(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_RETRIEVALMETHOD, i2);
        if (elementSelectDsNode != null) {
            return new RetrievalMethod(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public SPKIData itemSPKIData(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_SPKIDATA, i2);
        if (elementSelectDsNode != null) {
            return new SPKIData(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public X509Data itemX509Data(int i2) throws XMLSecurityException {
        if (this.x509Datas != null) {
            return this.x509Datas.get(i2);
        }
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_X509DATA, i2);
        if (elementSelectDsNode != null) {
            return new X509Data(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public DEREncodedKeyValue itemDEREncodedKeyValue(int i2) throws XMLSecurityException {
        Element elementSelectDs11Node = XMLUtils.selectDs11Node(getFirstChild(), Constants._TAG_DERENCODEDKEYVALUE, i2);
        if (elementSelectDs11Node != null) {
            return new DEREncodedKeyValue(elementSelectDs11Node, this.baseURI);
        }
        return null;
    }

    public KeyInfoReference itemKeyInfoReference(int i2) throws XMLSecurityException {
        Element elementSelectDs11Node = XMLUtils.selectDs11Node(getFirstChild(), Constants._TAG_KEYINFOREFERENCE, i2);
        if (elementSelectDs11Node != null) {
            return new KeyInfoReference(elementSelectDs11Node, this.baseURI);
        }
        return null;
    }

    public Element itemUnknownElement(int i2) {
        int i3 = 0;
        Node firstChild = getElement().getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 1 && node.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) {
                    i3++;
                    if (i3 == i2) {
                        return (Element) node;
                    }
                }
                firstChild = node.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public boolean isEmpty() {
        return getFirstChild() == null;
    }

    public boolean containsKeyName() {
        return lengthKeyName() > 0;
    }

    public boolean containsKeyValue() {
        return lengthKeyValue() > 0;
    }

    public boolean containsMgmtData() {
        return lengthMgmtData() > 0;
    }

    public boolean containsPGPData() {
        return lengthPGPData() > 0;
    }

    public boolean containsRetrievalMethod() {
        return lengthRetrievalMethod() > 0;
    }

    public boolean containsSPKIData() {
        return lengthSPKIData() > 0;
    }

    public boolean containsUnknownElement() {
        return lengthUnknownElement() > 0;
    }

    public boolean containsX509Data() {
        return lengthX509Data() > 0;
    }

    public boolean containsDEREncodedKeyValue() {
        return lengthDEREncodedKeyValue() > 0;
    }

    public boolean containsKeyInfoReference() {
        return lengthKeyInfoReference() > 0;
    }

    public PublicKey getPublicKey() throws KeyResolverException {
        PublicKey publicKeyFromInternalResolvers = getPublicKeyFromInternalResolvers();
        if (publicKeyFromInternalResolvers != null) {
            LOG.debug("I could find a key using the per-KeyInfo key resolvers");
            return publicKeyFromInternalResolvers;
        }
        LOG.debug("I couldn't find a key using the per-KeyInfo key resolvers");
        PublicKey publicKeyFromStaticResolvers = getPublicKeyFromStaticResolvers();
        if (publicKeyFromStaticResolvers != null) {
            LOG.debug("I could find a key using the system-wide key resolvers");
            return publicKeyFromStaticResolvers;
        }
        LOG.debug("I couldn't find a key using the system-wide key resolvers");
        return null;
    }

    PublicKey getPublicKeyFromStaticResolvers() throws KeyResolverException {
        Iterator<KeyResolverSpi> it = KeyResolver.iterator();
        while (it.hasNext()) {
            KeyResolverSpi next = it.next();
            next.setSecureValidation(this.secureValidation);
            String baseURI = getBaseURI();
            for (Node firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild.getNodeType() == 1) {
                    Iterator<StorageResolver> it2 = this.storageResolvers.iterator();
                    while (it2.hasNext()) {
                        PublicKey publicKeyEngineLookupAndResolvePublicKey = next.engineLookupAndResolvePublicKey((Element) firstChild, baseURI, it2.next());
                        if (publicKeyEngineLookupAndResolvePublicKey != null) {
                            return publicKeyEngineLookupAndResolvePublicKey;
                        }
                    }
                }
            }
        }
        return null;
    }

    PublicKey getPublicKeyFromInternalResolvers() throws KeyResolverException {
        for (KeyResolverSpi keyResolverSpi : this.internalKeyResolvers) {
            LOG.debug("Try {}", keyResolverSpi.getClass().getName());
            keyResolverSpi.setSecureValidation(this.secureValidation);
            String baseURI = getBaseURI();
            for (Node firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild.getNodeType() == 1) {
                    Iterator<StorageResolver> it = this.storageResolvers.iterator();
                    while (it.hasNext()) {
                        PublicKey publicKeyEngineLookupAndResolvePublicKey = keyResolverSpi.engineLookupAndResolvePublicKey((Element) firstChild, baseURI, it.next());
                        if (publicKeyEngineLookupAndResolvePublicKey != null) {
                            return publicKeyEngineLookupAndResolvePublicKey;
                        }
                    }
                }
            }
        }
        return null;
    }

    public X509Certificate getX509Certificate() throws KeyResolverException {
        X509Certificate x509CertificateFromInternalResolvers = getX509CertificateFromInternalResolvers();
        if (x509CertificateFromInternalResolvers != null) {
            LOG.debug("I could find a X509Certificate using the per-KeyInfo key resolvers");
            return x509CertificateFromInternalResolvers;
        }
        LOG.debug("I couldn't find a X509Certificate using the per-KeyInfo key resolvers");
        X509Certificate x509CertificateFromStaticResolvers = getX509CertificateFromStaticResolvers();
        if (x509CertificateFromStaticResolvers != null) {
            LOG.debug("I could find a X509Certificate using the system-wide key resolvers");
            return x509CertificateFromStaticResolvers;
        }
        LOG.debug("I couldn't find a X509Certificate using the system-wide key resolvers");
        return null;
    }

    X509Certificate getX509CertificateFromStaticResolvers() throws KeyResolverException {
        LOG.debug("Start getX509CertificateFromStaticResolvers() with {} resolvers", Integer.valueOf(KeyResolver.length()));
        String baseURI = getBaseURI();
        Iterator<KeyResolverSpi> it = KeyResolver.iterator();
        while (it.hasNext()) {
            KeyResolverSpi next = it.next();
            next.setSecureValidation(this.secureValidation);
            X509Certificate x509CertificateApplyCurrentResolver = applyCurrentResolver(baseURI, next);
            if (x509CertificateApplyCurrentResolver != null) {
                return x509CertificateApplyCurrentResolver;
            }
        }
        return null;
    }

    private X509Certificate applyCurrentResolver(String str, KeyResolverSpi keyResolverSpi) throws KeyResolverException {
        Node firstChild = getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 1) {
                    Iterator<StorageResolver> it = this.storageResolvers.iterator();
                    while (it.hasNext()) {
                        X509Certificate x509CertificateEngineLookupResolveX509Certificate = keyResolverSpi.engineLookupResolveX509Certificate((Element) node, str, it.next());
                        if (x509CertificateEngineLookupResolveX509Certificate != null) {
                            return x509CertificateEngineLookupResolveX509Certificate;
                        }
                    }
                }
                firstChild = node.getNextSibling();
            } else {
                return null;
            }
        }
    }

    X509Certificate getX509CertificateFromInternalResolvers() throws KeyResolverException {
        LOG.debug("Start getX509CertificateFromInternalResolvers() with {} resolvers", Integer.valueOf(lengthInternalKeyResolver()));
        String baseURI = getBaseURI();
        for (KeyResolverSpi keyResolverSpi : this.internalKeyResolvers) {
            LOG.debug("Try {}", keyResolverSpi.getClass().getName());
            keyResolverSpi.setSecureValidation(this.secureValidation);
            X509Certificate x509CertificateApplyCurrentResolver = applyCurrentResolver(baseURI, keyResolverSpi);
            if (x509CertificateApplyCurrentResolver != null) {
                return x509CertificateApplyCurrentResolver;
            }
        }
        return null;
    }

    public SecretKey getSecretKey() throws KeyResolverException {
        SecretKey secretKeyFromInternalResolvers = getSecretKeyFromInternalResolvers();
        if (secretKeyFromInternalResolvers != null) {
            LOG.debug("I could find a secret key using the per-KeyInfo key resolvers");
            return secretKeyFromInternalResolvers;
        }
        LOG.debug("I couldn't find a secret key using the per-KeyInfo key resolvers");
        SecretKey secretKeyFromStaticResolvers = getSecretKeyFromStaticResolvers();
        if (secretKeyFromStaticResolvers != null) {
            LOG.debug("I could find a secret key using the system-wide key resolvers");
            return secretKeyFromStaticResolvers;
        }
        LOG.debug("I couldn't find a secret key using the system-wide key resolvers");
        return null;
    }

    SecretKey getSecretKeyFromStaticResolvers() throws KeyResolverException {
        Iterator<KeyResolverSpi> it = KeyResolver.iterator();
        while (it.hasNext()) {
            KeyResolverSpi next = it.next();
            next.setSecureValidation(this.secureValidation);
            String baseURI = getBaseURI();
            for (Node firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild.getNodeType() == 1) {
                    Iterator<StorageResolver> it2 = this.storageResolvers.iterator();
                    while (it2.hasNext()) {
                        SecretKey secretKeyEngineLookupAndResolveSecretKey = next.engineLookupAndResolveSecretKey((Element) firstChild, baseURI, it2.next());
                        if (secretKeyEngineLookupAndResolveSecretKey != null) {
                            return secretKeyEngineLookupAndResolveSecretKey;
                        }
                    }
                }
            }
        }
        return null;
    }

    SecretKey getSecretKeyFromInternalResolvers() throws KeyResolverException {
        for (KeyResolverSpi keyResolverSpi : this.internalKeyResolvers) {
            LOG.debug("Try {}", keyResolverSpi.getClass().getName());
            keyResolverSpi.setSecureValidation(this.secureValidation);
            String baseURI = getBaseURI();
            for (Node firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild.getNodeType() == 1) {
                    Iterator<StorageResolver> it = this.storageResolvers.iterator();
                    while (it.hasNext()) {
                        SecretKey secretKeyEngineLookupAndResolveSecretKey = keyResolverSpi.engineLookupAndResolveSecretKey((Element) firstChild, baseURI, it.next());
                        if (secretKeyEngineLookupAndResolveSecretKey != null) {
                            return secretKeyEngineLookupAndResolveSecretKey;
                        }
                    }
                }
            }
        }
        return null;
    }

    public PrivateKey getPrivateKey() throws KeyResolverException {
        PrivateKey privateKeyFromInternalResolvers = getPrivateKeyFromInternalResolvers();
        if (privateKeyFromInternalResolvers != null) {
            LOG.debug("I could find a private key using the per-KeyInfo key resolvers");
            return privateKeyFromInternalResolvers;
        }
        LOG.debug("I couldn't find a secret key using the per-KeyInfo key resolvers");
        PrivateKey privateKeyFromStaticResolvers = getPrivateKeyFromStaticResolvers();
        if (privateKeyFromStaticResolvers != null) {
            LOG.debug("I could find a private key using the system-wide key resolvers");
            return privateKeyFromStaticResolvers;
        }
        LOG.debug("I couldn't find a private key using the system-wide key resolvers");
        return null;
    }

    PrivateKey getPrivateKeyFromStaticResolvers() throws KeyResolverException {
        PrivateKey privateKeyEngineLookupAndResolvePrivateKey;
        Iterator<KeyResolverSpi> it = KeyResolver.iterator();
        while (it.hasNext()) {
            KeyResolverSpi next = it.next();
            next.setSecureValidation(this.secureValidation);
            String baseURI = getBaseURI();
            for (Node firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild.getNodeType() == 1 && (privateKeyEngineLookupAndResolvePrivateKey = next.engineLookupAndResolvePrivateKey((Element) firstChild, baseURI, null)) != null) {
                    return privateKeyEngineLookupAndResolvePrivateKey;
                }
            }
        }
        return null;
    }

    PrivateKey getPrivateKeyFromInternalResolvers() throws KeyResolverException {
        PrivateKey privateKeyEngineLookupAndResolvePrivateKey;
        for (KeyResolverSpi keyResolverSpi : this.internalKeyResolvers) {
            LOG.debug("Try {}", keyResolverSpi.getClass().getName());
            keyResolverSpi.setSecureValidation(this.secureValidation);
            String baseURI = getBaseURI();
            for (Node firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild.getNodeType() == 1 && (privateKeyEngineLookupAndResolvePrivateKey = keyResolverSpi.engineLookupAndResolvePrivateKey((Element) firstChild, baseURI, null)) != null) {
                    return privateKeyEngineLookupAndResolvePrivateKey;
                }
            }
        }
        return null;
    }

    public void registerInternalKeyResolver(KeyResolverSpi keyResolverSpi) {
        this.internalKeyResolvers.add(keyResolverSpi);
    }

    int lengthInternalKeyResolver() {
        return this.internalKeyResolvers.size();
    }

    KeyResolverSpi itemInternalKeyResolver(int i2) {
        return this.internalKeyResolvers.get(i2);
    }

    public void addStorageResolver(StorageResolver storageResolver) {
        if (this.storageResolvers == nullList) {
            this.storageResolvers = new ArrayList();
        }
        this.storageResolvers.add(storageResolver);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_KEYINFO;
    }
}
