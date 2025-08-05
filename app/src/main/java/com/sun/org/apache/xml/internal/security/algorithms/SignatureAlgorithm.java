package com.sun.org.apache.xml.internal.security.algorithms;

import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;
import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA;
import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureDSA;
import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA;
import com.sun.org.apache.xml.internal.security.exceptions.AlgorithmAlreadyRegisteredException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/SignatureAlgorithm.class */
public class SignatureAlgorithm extends Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(SignatureAlgorithm.class);
    private static Map<String, Class<? extends SignatureAlgorithmSpi>> algorithmHash = new ConcurrentHashMap();
    private final SignatureAlgorithmSpi signatureAlgorithm;
    private final String algorithmURI;

    public SignatureAlgorithm(Document document, String str) throws XMLSecurityException {
        super(document, str);
        this.algorithmURI = str;
        this.signatureAlgorithm = getSignatureAlgorithmSpi(str);
        this.signatureAlgorithm.engineGetContextFromElement(getElement());
    }

    public SignatureAlgorithm(Document document, String str, int i2) throws XMLSecurityException {
        super(document, str);
        this.algorithmURI = str;
        this.signatureAlgorithm = getSignatureAlgorithmSpi(str);
        this.signatureAlgorithm.engineGetContextFromElement(getElement());
        this.signatureAlgorithm.engineSetHMACOutputLength(i2);
        ((IntegrityHmac) this.signatureAlgorithm).engineAddContextToElement(getElement());
    }

    public SignatureAlgorithm(Element element, String str) throws XMLSecurityException {
        this(element, str, true);
    }

    public SignatureAlgorithm(Element element, String str, boolean z2) throws DOMException, XMLSecurityException {
        super(element, str);
        this.algorithmURI = getURI();
        Attr attributeNodeNS = element.getAttributeNodeNS(null, Constants._ATT_ID);
        if (attributeNodeNS != null) {
            element.setIdAttributeNode(attributeNodeNS, true);
        }
        if (z2 && (XMLSignature.ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5.equals(this.algorithmURI) || XMLSignature.ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5.equals(this.algorithmURI))) {
            throw new XMLSecurityException("signature.signatureAlgorithm", new Object[]{this.algorithmURI});
        }
        this.signatureAlgorithm = getSignatureAlgorithmSpi(this.algorithmURI);
        this.signatureAlgorithm.engineGetContextFromElement(getElement());
    }

    private static SignatureAlgorithmSpi getSignatureAlgorithmSpi(String str) throws XMLSignatureException {
        try {
            Class<? extends SignatureAlgorithmSpi> cls = algorithmHash.get(str);
            LOG.debug("Create URI \"{}\" class \"{}\"", str, cls);
            if (cls == null) {
                throw new XMLSignatureException("algorithms.NoSuchAlgorithmNoEx", new Object[]{str});
            }
            return cls.newInstance();
        } catch (IllegalAccessException | InstantiationException | NullPointerException e2) {
            throw new XMLSignatureException(e2, "algorithms.NoSuchAlgorithm", new Object[]{str, e2.getMessage()});
        }
    }

    public byte[] sign() throws XMLSignatureException {
        return this.signatureAlgorithm.engineSign();
    }

    public String getJCEAlgorithmString() {
        return this.signatureAlgorithm.engineGetJCEAlgorithmString();
    }

    public String getJCEProviderName() {
        return this.signatureAlgorithm.engineGetJCEProviderName();
    }

    public void update(byte[] bArr) throws XMLSignatureException {
        this.signatureAlgorithm.engineUpdate(bArr);
    }

    public void update(byte b2) throws XMLSignatureException {
        this.signatureAlgorithm.engineUpdate(b2);
    }

    public void update(byte[] bArr, int i2, int i3) throws XMLSignatureException {
        this.signatureAlgorithm.engineUpdate(bArr, i2, i3);
    }

    public void initSign(Key key) throws XMLSignatureException {
        this.signatureAlgorithm.engineInitSign(key);
    }

    public void initSign(Key key, SecureRandom secureRandom) throws XMLSignatureException {
        this.signatureAlgorithm.engineInitSign(key, secureRandom);
    }

    public void initSign(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException {
        this.signatureAlgorithm.engineInitSign(key, algorithmParameterSpec);
    }

    public void setParameter(AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException {
        this.signatureAlgorithm.engineSetParameter(algorithmParameterSpec);
    }

    public void initVerify(Key key) throws XMLSignatureException {
        this.signatureAlgorithm.engineInitVerify(key);
    }

    public boolean verify(byte[] bArr) throws XMLSignatureException {
        return this.signatureAlgorithm.engineVerify(bArr);
    }

    public final String getURI() {
        return getLocalAttribute(Constants._ATT_ALGORITHM);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void register(String str, String str2) throws AlgorithmAlreadyRegisteredException, ClassNotFoundException, XMLSignatureException {
        JavaUtils.checkRegisterPermission();
        LOG.debug("Try to register {} {}", str, str2);
        Class<? extends SignatureAlgorithmSpi> cls = algorithmHash.get(str);
        if (cls != null) {
            throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", new Object[]{str, cls});
        }
        try {
            algorithmHash.put(str, ClassLoaderUtils.loadClass(str2, SignatureAlgorithm.class));
        } catch (NullPointerException e2) {
            throw new XMLSignatureException(e2, "algorithms.NoSuchAlgorithm", new Object[]{str, e2.getMessage()});
        }
    }

    public static void register(String str, Class<? extends SignatureAlgorithmSpi> cls) throws AlgorithmAlreadyRegisteredException, ClassNotFoundException, XMLSignatureException {
        JavaUtils.checkRegisterPermission();
        LOG.debug("Try to register {} {}", str, cls);
        Class<? extends SignatureAlgorithmSpi> cls2 = algorithmHash.get(str);
        if (cls2 != null) {
            throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", new Object[]{str, cls2});
        }
        algorithmHash.put(str, cls);
    }

    public static void registerDefaultAlgorithms() {
        algorithmHash.put("http://www.w3.org/2000/09/xmldsig#dsa-sha1", SignatureDSA.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_DSA_SHA256, SignatureDSA.SHA256.class);
        algorithmHash.put("http://www.w3.org/2000/09/xmldsig#rsa-sha1", SignatureBaseRSA.SignatureRSASHA1.class);
        algorithmHash.put("http://www.w3.org/2000/09/xmldsig#hmac-sha1", IntegrityHmac.IntegrityHmacSHA1.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5, SignatureBaseRSA.SignatureRSAMD5.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_RIPEMD160, SignatureBaseRSA.SignatureRSARIPEMD160.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA224, SignatureBaseRSA.SignatureRSASHA224.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256, SignatureBaseRSA.SignatureRSASHA256.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384, SignatureBaseRSA.SignatureRSASHA384.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512, SignatureBaseRSA.SignatureRSASHA512.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1_MGF1, SignatureBaseRSA.SignatureRSASHA1MGF1.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA224_MGF1, SignatureBaseRSA.SignatureRSASHA224MGF1.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256_MGF1, SignatureBaseRSA.SignatureRSASHA256MGF1.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384_MGF1, SignatureBaseRSA.SignatureRSASHA384MGF1.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512_MGF1, SignatureBaseRSA.SignatureRSASHA512MGF1.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA1, SignatureECDSA.SignatureECDSASHA1.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA224, SignatureECDSA.SignatureECDSASHA224.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA256, SignatureECDSA.SignatureECDSASHA256.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA384, SignatureECDSA.SignatureECDSASHA384.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA512, SignatureECDSA.SignatureECDSASHA512.class);
        algorithmHash.put(XMLSignature.ALGO_ID_SIGNATURE_ECDSA_RIPEMD160, SignatureECDSA.SignatureECDSARIPEMD160.class);
        algorithmHash.put(XMLSignature.ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5, IntegrityHmac.IntegrityHmacMD5.class);
        algorithmHash.put(XMLSignature.ALGO_ID_MAC_HMAC_RIPEMD160, IntegrityHmac.IntegrityHmacRIPEMD160.class);
        algorithmHash.put(XMLSignature.ALGO_ID_MAC_HMAC_SHA224, IntegrityHmac.IntegrityHmacSHA224.class);
        algorithmHash.put(XMLSignature.ALGO_ID_MAC_HMAC_SHA256, IntegrityHmac.IntegrityHmacSHA256.class);
        algorithmHash.put(XMLSignature.ALGO_ID_MAC_HMAC_SHA384, IntegrityHmac.IntegrityHmacSHA384.class);
        algorithmHash.put(XMLSignature.ALGO_ID_MAC_HMAC_SHA512, IntegrityHmac.IntegrityHmacSHA512.class);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy, com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseNamespace() {
        return "http://www.w3.org/2000/09/xmldsig#";
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_SIGNATUREMETHOD;
    }
}
