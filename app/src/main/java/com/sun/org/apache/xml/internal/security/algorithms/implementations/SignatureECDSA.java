package com.sun.org.apache.xml.internal.security.algorithms.implementations;

import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureECDSA.class */
public abstract class SignatureECDSA extends SignatureAlgorithmSpi {
    private static final Logger LOG = LoggerFactory.getLogger(SignatureECDSA.class);
    private Signature signatureAlgorithm;

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    public abstract String engineGetURI();

    public static byte[] convertASN1toXMLDSIG(byte[] bArr) throws IOException {
        return ECDSAUtils.convertASN1toXMLDSIG(bArr);
    }

    public static byte[] convertXMLDSIGtoASN1(byte[] bArr) throws IOException {
        return ECDSAUtils.convertXMLDSIGtoASN1(bArr);
    }

    public SignatureECDSA() throws XMLSignatureException {
        String strTranslateURItoJCEID = JCEMapper.translateURItoJCEID(engineGetURI());
        LOG.debug("Created SignatureECDSA using {}", strTranslateURItoJCEID);
        String providerId = JCEMapper.getProviderId();
        try {
            if (providerId == null) {
                this.signatureAlgorithm = Signature.getInstance(strTranslateURItoJCEID);
            } else {
                this.signatureAlgorithm = Signature.getInstance(strTranslateURItoJCEID, providerId);
            }
        } catch (NoSuchAlgorithmException e2) {
            throw new XMLSignatureException("algorithms.NoSuchAlgorithm", new Object[]{strTranslateURItoJCEID, e2.getLocalizedMessage()});
        } catch (NoSuchProviderException e3) {
            throw new XMLSignatureException("algorithms.NoSuchAlgorithm", new Object[]{strTranslateURItoJCEID, e3.getLocalizedMessage()});
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException {
        try {
            this.signatureAlgorithm.setParameter(algorithmParameterSpec);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected boolean engineVerify(byte[] bArr) throws XMLSignatureException {
        try {
            byte[] bArrConvertXMLDSIGtoASN1 = convertXMLDSIGtoASN1(bArr);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Called ECDSA.verify() on " + XMLUtils.encodeToString(bArr));
            }
            return this.signatureAlgorithm.verify(bArrConvertXMLDSIGtoASN1);
        } catch (IOException e2) {
            throw new XMLSignatureException(e2);
        } catch (SignatureException e3) {
            throw new XMLSignatureException(e3);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineInitVerify(Key key) throws XMLSignatureException {
        if (!(key instanceof PublicKey)) {
            String name = null;
            if (key != null) {
                name = key.getClass().getName();
            }
            throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", new Object[]{name, PublicKey.class.getName()});
        }
        try {
            this.signatureAlgorithm.initVerify((PublicKey) key);
        } catch (InvalidKeyException e2) {
            Signature signature = this.signatureAlgorithm;
            try {
                this.signatureAlgorithm = Signature.getInstance(this.signatureAlgorithm.getAlgorithm());
            } catch (Exception e3) {
                LOG.debug("Exception when reinstantiating Signature: {}", e3);
                this.signatureAlgorithm = signature;
            }
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected byte[] engineSign() throws XMLSignatureException {
        try {
            return convertASN1toXMLDSIG(this.signatureAlgorithm.sign());
        } catch (IOException e2) {
            throw new XMLSignatureException(e2);
        } catch (SignatureException e3) {
            throw new XMLSignatureException(e3);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineInitSign(Key key, SecureRandom secureRandom) throws XMLSignatureException {
        if (!(key instanceof PrivateKey)) {
            String name = null;
            if (key != null) {
                name = key.getClass().getName();
            }
            throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", new Object[]{name, PrivateKey.class.getName()});
        }
        try {
            if (secureRandom == null) {
                this.signatureAlgorithm.initSign((PrivateKey) key);
            } else {
                this.signatureAlgorithm.initSign((PrivateKey) key, secureRandom);
            }
        } catch (InvalidKeyException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineInitSign(Key key) throws XMLSignatureException {
        engineInitSign(key, (SecureRandom) null);
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineUpdate(byte[] bArr) throws XMLSignatureException {
        try {
            this.signatureAlgorithm.update(bArr);
        } catch (SignatureException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineUpdate(byte b2) throws XMLSignatureException {
        try {
            this.signatureAlgorithm.update(b2);
        } catch (SignatureException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) throws XMLSignatureException {
        try {
            this.signatureAlgorithm.update(bArr, i2, i3);
        } catch (SignatureException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected String engineGetJCEAlgorithmString() {
        return this.signatureAlgorithm.getAlgorithm();
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected String engineGetJCEProviderName() {
        return this.signatureAlgorithm.getProvider().getName();
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineSetHMACOutputLength(int i2) throws XMLSignatureException {
        throw new XMLSignatureException("algorithms.HMACOutputLengthOnlyForHMAC");
    }

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected void engineInitSign(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException {
        throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnRSA");
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureECDSA$SignatureECDSASHA1.class */
    public static class SignatureECDSASHA1 extends SignatureECDSA {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureECDSA$SignatureECDSASHA224.class */
    public static class SignatureECDSASHA224 extends SignatureECDSA {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA224;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureECDSA$SignatureECDSASHA256.class */
    public static class SignatureECDSASHA256 extends SignatureECDSA {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA256;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureECDSA$SignatureECDSASHA384.class */
    public static class SignatureECDSASHA384 extends SignatureECDSA {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA384;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureECDSA$SignatureECDSASHA512.class */
    public static class SignatureECDSASHA512 extends SignatureECDSA {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA512;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureECDSA$SignatureECDSARIPEMD160.class */
    public static class SignatureECDSARIPEMD160 extends SignatureECDSA {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_ECDSA_RIPEMD160;
        }
    }
}
