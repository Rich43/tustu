package com.sun.org.apache.xml.internal.security.algorithms.implementations;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/IntegrityHmac.class */
public abstract class IntegrityHmac extends SignatureAlgorithmSpi {
    private static final Logger LOG = LoggerFactory.getLogger(IntegrityHmac.class);
    private Mac macAlgorithm;
    private int HMACOutputLength;
    private boolean HMACOutputLengthSet = false;

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    public abstract String engineGetURI();

    abstract int getDigestLength();

    public IntegrityHmac() throws XMLSignatureException {
        String strTranslateURItoJCEID = JCEMapper.translateURItoJCEID(engineGetURI());
        LOG.debug("Created IntegrityHmacSHA1 using {}", strTranslateURItoJCEID);
        try {
            this.macAlgorithm = Mac.getInstance(strTranslateURItoJCEID);
        } catch (NoSuchAlgorithmException e2) {
            throw new XMLSignatureException("algorithms.NoSuchAlgorithm", new Object[]{strTranslateURItoJCEID, e2.getLocalizedMessage()});
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException {
        throw new XMLSignatureException(Constants.ELEMNAME_EMPTY_STRING, new Object[]{"Incorrect method call"});
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    public void reset() {
        this.HMACOutputLength = 0;
        this.HMACOutputLengthSet = false;
        this.macAlgorithm.reset();
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected boolean engineVerify(byte[] bArr) throws XMLSignatureException {
        try {
            if (this.HMACOutputLengthSet && this.HMACOutputLength < getDigestLength()) {
                LOG.debug("HMACOutputLength must not be less than {}", Integer.valueOf(getDigestLength()));
                throw new XMLSignatureException("algorithms.HMACOutputLengthMin", new Object[]{String.valueOf(getDigestLength())});
            }
            return MessageDigestAlgorithm.isEqual(this.macAlgorithm.doFinal(), bArr);
        } catch (IllegalStateException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineInitVerify(Key key) throws XMLSignatureException {
        if (!(key instanceof SecretKey)) {
            String name = null;
            if (key != null) {
                name = key.getClass().getName();
            }
            throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", new Object[]{name, SecretKey.class.getName()});
        }
        try {
            this.macAlgorithm.init(key);
        } catch (InvalidKeyException e2) {
            Mac mac = this.macAlgorithm;
            try {
                this.macAlgorithm = Mac.getInstance(this.macAlgorithm.getAlgorithm());
            } catch (Exception e3) {
                LOG.debug("Exception when reinstantiating Mac: {}", e3);
                this.macAlgorithm = mac;
            }
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected byte[] engineSign() throws XMLSignatureException {
        try {
            if (this.HMACOutputLengthSet && this.HMACOutputLength < getDigestLength()) {
                LOG.debug("HMACOutputLength must not be less than {}", Integer.valueOf(getDigestLength()));
                throw new XMLSignatureException("algorithms.HMACOutputLengthMin", new Object[]{String.valueOf(getDigestLength())});
            }
            return this.macAlgorithm.doFinal();
        } catch (IllegalStateException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineInitSign(Key key) throws XMLSignatureException {
        engineInitSign(key, (AlgorithmParameterSpec) null);
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineInitSign(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException {
        if (!(key instanceof SecretKey)) {
            String name = null;
            if (key != null) {
                name = key.getClass().getName();
            }
            throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", new Object[]{name, SecretKey.class.getName()});
        }
        try {
            if (algorithmParameterSpec == null) {
                this.macAlgorithm.init(key);
            } else {
                this.macAlgorithm.init(key, algorithmParameterSpec);
            }
        } catch (InvalidAlgorithmParameterException e2) {
            throw new XMLSignatureException(e2);
        } catch (InvalidKeyException e3) {
            throw new XMLSignatureException(e3);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineInitSign(Key key, SecureRandom secureRandom) throws XMLSignatureException {
        throw new XMLSignatureException("algorithms.CannotUseSecureRandomOnMAC");
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineUpdate(byte[] bArr) throws XMLSignatureException {
        try {
            this.macAlgorithm.update(bArr);
        } catch (IllegalStateException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineUpdate(byte b2) throws XMLSignatureException {
        try {
            this.macAlgorithm.update(b2);
        } catch (IllegalStateException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) throws XMLSignatureException {
        try {
            this.macAlgorithm.update(bArr, i2, i3);
        } catch (IllegalStateException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected String engineGetJCEAlgorithmString() {
        return this.macAlgorithm.getAlgorithm();
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected String engineGetJCEProviderName() {
        return this.macAlgorithm.getProvider().getName();
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineSetHMACOutputLength(int i2) {
        this.HMACOutputLength = i2;
        this.HMACOutputLengthSet = true;
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineGetContextFromElement(Element element) {
        String fullTextChildrenFromNode;
        super.engineGetContextFromElement(element);
        if (element == null) {
            throw new IllegalArgumentException("element null");
        }
        Element elementSelectDsNode = XMLUtils.selectDsNode(element.getFirstChild(), com.sun.org.apache.xml.internal.security.utils.Constants._TAG_HMACOUTPUTLENGTH, 0);
        if (elementSelectDsNode != null && (fullTextChildrenFromNode = XMLUtils.getFullTextChildrenFromNode(elementSelectDsNode)) != null && !"".equals(fullTextChildrenFromNode)) {
            this.HMACOutputLength = Integer.parseInt(fullTextChildrenFromNode);
            this.HMACOutputLengthSet = true;
        }
    }

    public void engineAddContextToElement(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("null element");
        }
        if (this.HMACOutputLengthSet) {
            Document ownerDocument = element.getOwnerDocument();
            Element elementCreateElementInSignatureSpace = XMLUtils.createElementInSignatureSpace(ownerDocument, com.sun.org.apache.xml.internal.security.utils.Constants._TAG_HMACOUTPUTLENGTH);
            elementCreateElementInSignatureSpace.appendChild(ownerDocument.createTextNode("" + this.HMACOutputLength));
            XMLUtils.addReturnToElement(element);
            element.appendChild(elementCreateElementInSignatureSpace);
            XMLUtils.addReturnToElement(element);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/IntegrityHmac$IntegrityHmacSHA1.class */
    public static class IntegrityHmacSHA1 extends IntegrityHmac {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
        }

        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac
        int getDigestLength() {
            return 160;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/IntegrityHmac$IntegrityHmacSHA224.class */
    public static class IntegrityHmacSHA224 extends IntegrityHmac {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_MAC_HMAC_SHA224;
        }

        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac
        int getDigestLength() {
            return 224;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/IntegrityHmac$IntegrityHmacSHA256.class */
    public static class IntegrityHmacSHA256 extends IntegrityHmac {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_MAC_HMAC_SHA256;
        }

        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac
        int getDigestLength() {
            return 256;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/IntegrityHmac$IntegrityHmacSHA384.class */
    public static class IntegrityHmacSHA384 extends IntegrityHmac {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_MAC_HMAC_SHA384;
        }

        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac
        int getDigestLength() {
            return 384;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/IntegrityHmac$IntegrityHmacSHA512.class */
    public static class IntegrityHmacSHA512 extends IntegrityHmac {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_MAC_HMAC_SHA512;
        }

        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac
        int getDigestLength() {
            return 512;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/IntegrityHmac$IntegrityHmacRIPEMD160.class */
    public static class IntegrityHmacRIPEMD160 extends IntegrityHmac {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_MAC_HMAC_RIPEMD160;
        }

        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac
        int getDigestLength() {
            return 160;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/IntegrityHmac$IntegrityHmacMD5.class */
    public static class IntegrityHmacMD5 extends IntegrityHmac {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5;
        }

        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac
        int getDigestLength() {
            return 128;
        }
    }
}
