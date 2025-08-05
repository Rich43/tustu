package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.I18n;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.SignerOutputStream;
import com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/XMLSignature.class */
public final class XMLSignature extends SignatureElementProxy {
    public static final String ALGO_ID_MAC_HMAC_SHA1 = "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
    public static final String ALGO_ID_SIGNATURE_DSA = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
    public static final String ALGO_ID_SIGNATURE_DSA_SHA256 = "http://www.w3.org/2009/xmldsig11#dsa-sha256";
    public static final String ALGO_ID_SIGNATURE_RSA = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
    public static final String ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5 = "http://www.w3.org/2001/04/xmldsig-more#rsa-md5";
    public static final String ALGO_ID_SIGNATURE_RSA_RIPEMD160 = "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA224 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha224";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA1_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha1-rsa-MGF1";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA224_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha224-rsa-MGF1";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA256_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha256-rsa-MGF1";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA384_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha384-rsa-MGF1";
    public static final String ALGO_ID_SIGNATURE_RSA_SHA512_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha512-rsa-MGF1";
    public static final String ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5 = "http://www.w3.org/2001/04/xmldsig-more#hmac-md5";
    public static final String ALGO_ID_MAC_HMAC_RIPEMD160 = "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160";
    public static final String ALGO_ID_MAC_HMAC_SHA224 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha224";
    public static final String ALGO_ID_MAC_HMAC_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
    public static final String ALGO_ID_MAC_HMAC_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
    public static final String ALGO_ID_MAC_HMAC_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
    public static final String ALGO_ID_SIGNATURE_ECDSA_SHA1 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1";
    public static final String ALGO_ID_SIGNATURE_ECDSA_SHA224 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224";
    public static final String ALGO_ID_SIGNATURE_ECDSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256";
    public static final String ALGO_ID_SIGNATURE_ECDSA_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384";
    public static final String ALGO_ID_SIGNATURE_ECDSA_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512";
    public static final String ALGO_ID_SIGNATURE_ECDSA_RIPEMD160 = "http://www.w3.org/2007/05/xmldsig-more#ecdsa-ripemd160";
    private static final Logger LOG = LoggerFactory.getLogger(XMLSignature.class);
    private SignedInfo signedInfo;
    private KeyInfo keyInfo;
    private boolean followManifestsDuringValidation;
    private Element signatureValueElement;
    private static final int MODE_SIGN = 0;
    private static final int MODE_VERIFY = 1;
    private int state;

    public XMLSignature(Document document, String str, String str2) throws XMLSecurityException {
        this(document, str, str2, 0, "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
    }

    public XMLSignature(Document document, String str, String str2, int i2) throws XMLSecurityException {
        this(document, str, str2, i2, "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
    }

    public XMLSignature(Document document, String str, String str2, String str3) throws XMLSecurityException {
        this(document, str, str2, 0, str3);
    }

    public XMLSignature(Document document, String str, String str2, int i2, String str3) throws DOMException, XMLSecurityException {
        super(document);
        this.followManifestsDuringValidation = false;
        this.state = 0;
        String defaultPrefix = getDefaultPrefix("http://www.w3.org/2000/09/xmldsig#");
        if (defaultPrefix == null || defaultPrefix.length() == 0) {
            getElement().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
        } else {
            getElement().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + defaultPrefix, "http://www.w3.org/2000/09/xmldsig#");
        }
        addReturnToSelf();
        this.baseURI = str;
        this.signedInfo = new SignedInfo(getDocument(), str2, i2, str3);
        appendSelf(this.signedInfo);
        addReturnToSelf();
        this.signatureValueElement = XMLUtils.createElementInSignatureSpace(getDocument(), Constants._TAG_SIGNATUREVALUE);
        appendSelf(this.signatureValueElement);
        addReturnToSelf();
    }

    public XMLSignature(Document document, String str, Element element, Element element2) throws DOMException, XMLSecurityException {
        super(document);
        this.followManifestsDuringValidation = false;
        this.state = 0;
        String defaultPrefix = getDefaultPrefix("http://www.w3.org/2000/09/xmldsig#");
        if (defaultPrefix == null || defaultPrefix.length() == 0) {
            getElement().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
        } else {
            getElement().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + defaultPrefix, "http://www.w3.org/2000/09/xmldsig#");
        }
        addReturnToSelf();
        this.baseURI = str;
        this.signedInfo = new SignedInfo(getDocument(), element, element2);
        appendSelf(this.signedInfo);
        addReturnToSelf();
        this.signatureValueElement = XMLUtils.createElementInSignatureSpace(getDocument(), Constants._TAG_SIGNATUREVALUE);
        appendSelf(this.signatureValueElement);
        addReturnToSelf();
    }

    public XMLSignature(Element element, String str) throws XMLSecurityException {
        this(element, str, true);
    }

    public XMLSignature(Element element, String str, boolean z2) throws DOMException, XMLSecurityException {
        super(element, str);
        this.followManifestsDuringValidation = false;
        this.state = 0;
        Element nextElement = XMLUtils.getNextElement(element.getFirstChild());
        if (nextElement == null) {
            throw new XMLSignatureException("xml.WrongContent", new Object[]{Constants._TAG_SIGNEDINFO, Constants._TAG_SIGNATURE});
        }
        this.signedInfo = new SignedInfo(nextElement, str, z2);
        this.signatureValueElement = XMLUtils.getNextElement(XMLUtils.getNextElement(element.getFirstChild()).getNextSibling());
        if (this.signatureValueElement == null) {
            throw new XMLSignatureException("xml.WrongContent", new Object[]{Constants._TAG_SIGNATUREVALUE, Constants._TAG_SIGNATURE});
        }
        Attr attributeNodeNS = this.signatureValueElement.getAttributeNodeNS(null, Constants._ATT_ID);
        if (attributeNodeNS != null) {
            this.signatureValueElement.setIdAttributeNode(attributeNodeNS, true);
        }
        Element nextElement2 = XMLUtils.getNextElement(this.signatureValueElement.getNextSibling());
        if (nextElement2 != null && "http://www.w3.org/2000/09/xmldsig#".equals(nextElement2.getNamespaceURI()) && Constants._TAG_KEYINFO.equals(nextElement2.getLocalName())) {
            this.keyInfo = new KeyInfo(nextElement2, str);
            this.keyInfo.setSecureValidation(z2);
        }
        Element nextElement3 = XMLUtils.getNextElement(this.signatureValueElement.getNextSibling());
        while (true) {
            Element element2 = nextElement3;
            if (element2 != null) {
                Attr attributeNodeNS2 = element2.getAttributeNodeNS(null, Constants._ATT_ID);
                if (attributeNodeNS2 != null) {
                    element2.setIdAttributeNode(attributeNodeNS2, true);
                }
                Node firstChild = element2.getFirstChild();
                while (true) {
                    Node node = firstChild;
                    if (node != null) {
                        if (node.getNodeType() == 1) {
                            Element element3 = (Element) node;
                            String localName = element3.getLocalName();
                            if (Constants._TAG_MANIFEST.equals(localName)) {
                                new Manifest(element3, str);
                            } else if (Constants._TAG_SIGNATUREPROPERTIES.equals(localName)) {
                                new SignatureProperties(element3, str);
                            }
                        }
                        firstChild = node.getNextSibling();
                    }
                }
                nextElement3 = XMLUtils.getNextElement(element2.getNextSibling());
            } else {
                this.state = 1;
                return;
            }
        }
    }

    public void setId(String str) {
        if (str != null) {
            setLocalIdAttribute(Constants._ATT_ID, str);
        }
    }

    public String getId() {
        return getLocalAttribute(Constants._ATT_ID);
    }

    public SignedInfo getSignedInfo() {
        return this.signedInfo;
    }

    public byte[] getSignatureValue() throws XMLSignatureException {
        return XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(this.signatureValueElement));
    }

    private void setSignatureValueElement(byte[] bArr) {
        while (this.signatureValueElement.hasChildNodes()) {
            this.signatureValueElement.removeChild(this.signatureValueElement.getFirstChild());
        }
        String strEncodeToString = XMLUtils.encodeToString(bArr);
        if (strEncodeToString.length() > 76 && !XMLUtils.ignoreLineBreaks()) {
            strEncodeToString = "\n" + strEncodeToString + "\n";
        }
        this.signatureValueElement.appendChild(createText(strEncodeToString));
    }

    public KeyInfo getKeyInfo() {
        if (this.state == 0 && this.keyInfo == null) {
            this.keyInfo = new KeyInfo(getDocument());
            Element element = this.keyInfo.getElement();
            Element elementSelectDsNode = XMLUtils.selectDsNode(getElement().getFirstChild(), "Object", 0);
            if (elementSelectDsNode != null) {
                getElement().insertBefore(element, elementSelectDsNode);
                XMLUtils.addReturnBeforeChild(getElement(), elementSelectDsNode);
            } else {
                appendSelf(element);
                addReturnToSelf();
            }
        }
        return this.keyInfo;
    }

    public void appendObject(ObjectContainer objectContainer) throws XMLSignatureException {
        appendSelf(objectContainer);
        addReturnToSelf();
    }

    public ObjectContainer getObjectItem(int i2) {
        try {
            return new ObjectContainer(XMLUtils.selectDsNode(getFirstChild(), "Object", i2), this.baseURI);
        } catch (XMLSecurityException e2) {
            return null;
        }
    }

    public int getObjectLength() {
        return length("http://www.w3.org/2000/09/xmldsig#", "Object");
    }

    /* JADX WARN: Failed to calculate best type for var: r8v5 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r9v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 8, insn: 0x00d8: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r8 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:43:0x00d8 */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x00dd: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:45:0x00dd */
    /* JADX WARN: Type inference failed for: r8v5, types: [com.sun.org.apache.xml.internal.security.utils.SignerOutputStream] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.Throwable] */
    public void sign(Key key) throws XMLSignatureException {
        ?? r8;
        ?? r9;
        if (key instanceof PublicKey) {
            throw new IllegalArgumentException(I18n.translate("algorithms.operationOnlyVerification"));
        }
        SignedInfo signedInfo = getSignedInfo();
        SignatureAlgorithm signatureAlgorithm = signedInfo.getSignatureAlgorithm();
        try {
            try {
                SignerOutputStream signerOutputStream = new SignerOutputStream(signatureAlgorithm);
                Throwable th = null;
                UnsyncBufferedOutputStream unsyncBufferedOutputStream = new UnsyncBufferedOutputStream(signerOutputStream);
                Throwable th2 = null;
                try {
                    try {
                        signedInfo.generateDigestValues();
                        signatureAlgorithm.initSign(key);
                        signedInfo.signInOctetStream(unsyncBufferedOutputStream);
                        setSignatureValueElement(signatureAlgorithm.sign());
                        if (unsyncBufferedOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    unsyncBufferedOutputStream.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                unsyncBufferedOutputStream.close();
                            }
                        }
                        if (signerOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    signerOutputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                signerOutputStream.close();
                            }
                        }
                    } catch (Throwable th5) {
                        if (unsyncBufferedOutputStream != null) {
                            if (th2 != null) {
                                try {
                                    unsyncBufferedOutputStream.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                unsyncBufferedOutputStream.close();
                            }
                        }
                        throw th5;
                    }
                } finally {
                }
            } catch (CanonicalizationException e2) {
                throw new XMLSignatureException(e2);
            } catch (InvalidCanonicalizerException e3) {
                throw new XMLSignatureException(e3);
            } catch (XMLSignatureException e4) {
                throw e4;
            } catch (XMLSecurityException e5) {
                throw new XMLSignatureException(e5);
            } catch (IOException e6) {
                throw new XMLSignatureException(e6);
            }
        } catch (Throwable th7) {
            if (r8 != 0) {
                if (r9 != 0) {
                    try {
                        r8.close();
                    } catch (Throwable th8) {
                        r9.addSuppressed(th8);
                    }
                } else {
                    r8.close();
                }
            }
            throw th7;
        }
    }

    public void addResourceResolver(ResourceResolver resourceResolver) {
        getSignedInfo().addResourceResolver(resourceResolver);
    }

    public void addResourceResolver(ResourceResolverSpi resourceResolverSpi) {
        getSignedInfo().addResourceResolver(resourceResolverSpi);
    }

    public boolean checkSignatureValue(X509Certificate x509Certificate) throws XMLSignatureException {
        if (x509Certificate != null) {
            return checkSignatureValue(x509Certificate.getPublicKey());
        }
        throw new XMLSignatureException(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_EMPTY_STRING, new Object[]{"Didn't get a certificate"});
    }

    /* JADX WARN: Finally extract failed */
    public boolean checkSignatureValue(Key key) throws XMLSecurityException {
        SignerOutputStream signerOutputStream;
        Throwable th;
        if (key == null) {
            throw new XMLSignatureException(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_EMPTY_STRING, new Object[]{"Didn't get a key"});
        }
        try {
            SignedInfo signedInfo = getSignedInfo();
            SignatureAlgorithm signatureAlgorithm = signedInfo.getSignatureAlgorithm();
            LOG.debug("signatureMethodURI = {}", signatureAlgorithm.getAlgorithmURI());
            LOG.debug("jceSigAlgorithm = {}", signatureAlgorithm.getJCEAlgorithmString());
            LOG.debug("jceSigProvider = {}", signatureAlgorithm.getJCEProviderName());
            LOG.debug("PublicKey = {}", key);
            byte[] signatureValue = null;
            try {
                signerOutputStream = new SignerOutputStream(signatureAlgorithm);
                th = null;
            } catch (XMLSecurityException e2) {
                throw e2;
            } catch (IOException e3) {
                LOG.debug(e3.getMessage(), e3);
            }
            try {
                UnsyncBufferedOutputStream unsyncBufferedOutputStream = new UnsyncBufferedOutputStream(signerOutputStream);
                Throwable th2 = null;
                try {
                    try {
                        signatureAlgorithm.initVerify(key);
                        signedInfo.signInOctetStream(unsyncBufferedOutputStream);
                        signatureValue = getSignatureValue();
                        if (unsyncBufferedOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    unsyncBufferedOutputStream.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                unsyncBufferedOutputStream.close();
                            }
                        }
                        if (signerOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    signerOutputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                signerOutputStream.close();
                            }
                        }
                        if (signatureAlgorithm.verify(signatureValue)) {
                            return signedInfo.verify(this.followManifestsDuringValidation);
                        }
                        LOG.warn("Signature verification failed.");
                        return false;
                    } catch (Throwable th5) {
                        if (unsyncBufferedOutputStream != null) {
                            if (th2 != null) {
                                try {
                                    unsyncBufferedOutputStream.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                unsyncBufferedOutputStream.close();
                            }
                        }
                        throw th5;
                    }
                } finally {
                }
            } catch (Throwable th7) {
                if (signerOutputStream != null) {
                    if (0 != 0) {
                        try {
                            signerOutputStream.close();
                        } catch (Throwable th8) {
                            th.addSuppressed(th8);
                        }
                    } else {
                        signerOutputStream.close();
                    }
                }
                throw th7;
            }
        } catch (XMLSignatureException e4) {
            throw e4;
        } catch (XMLSecurityException e5) {
            throw new XMLSignatureException(e5);
        }
    }

    public void addDocument(String str, Transforms transforms, String str2, String str3, String str4) throws XMLSignatureException {
        this.signedInfo.addDocument(this.baseURI, str, transforms, str2, str3, str4);
    }

    public void addDocument(String str, Transforms transforms, String str2) throws XMLSignatureException {
        this.signedInfo.addDocument(this.baseURI, str, transforms, str2, null, null);
    }

    public void addDocument(String str, Transforms transforms) throws XMLSignatureException {
        this.signedInfo.addDocument(this.baseURI, str, transforms, "http://www.w3.org/2000/09/xmldsig#sha1", null, null);
    }

    public void addDocument(String str) throws XMLSignatureException {
        this.signedInfo.addDocument(this.baseURI, str, null, "http://www.w3.org/2000/09/xmldsig#sha1", null, null);
    }

    public void addKeyInfo(X509Certificate x509Certificate) throws XMLSecurityException {
        X509Data x509Data = new X509Data(getDocument());
        x509Data.addCertificate(x509Certificate);
        getKeyInfo().add(x509Data);
    }

    public void addKeyInfo(PublicKey publicKey) {
        getKeyInfo().add(publicKey);
    }

    public SecretKey createSecretKey(byte[] bArr) {
        return getSignedInfo().createSecretKey(bArr);
    }

    public void setFollowNestedManifests(boolean z2) {
        this.followManifestsDuringValidation = z2;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_SIGNATURE;
    }
}
