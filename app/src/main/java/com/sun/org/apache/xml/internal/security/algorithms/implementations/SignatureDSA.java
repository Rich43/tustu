package com.sun.org.apache.xml.internal.security.algorithms.implementations;

import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
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
import java.security.interfaces.DSAKey;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureDSA.class */
public class SignatureDSA extends SignatureAlgorithmSpi {
    public static final String URI = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
    private static final Logger LOG = LoggerFactory.getLogger(SignatureDSA.class);
    private Signature signatureAlgorithm;
    private int size;

    @Override // com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
    protected String engineGetURI() {
        return "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
    }

    public SignatureDSA() throws XMLSignatureException {
        String strTranslateURItoJCEID = JCEMapper.translateURItoJCEID(engineGetURI());
        LOG.debug("Created SignatureDSA using {}", strTranslateURItoJCEID);
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("Called DSA.verify() on " + XMLUtils.encodeToString(bArr));
            }
            return this.signatureAlgorithm.verify(JavaUtils.convertDsaXMLDSIGtoASN1(bArr, this.size / 8));
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
            this.size = ((DSAKey) key).getParams().getQ().bitLength();
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
            return JavaUtils.convertDsaASN1toXMLDSIG(this.signatureAlgorithm.sign(), this.size / 8);
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
            this.size = ((DSAKey) key).getParams().getQ().bitLength();
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
        throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnDSA");
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/implementations/SignatureDSA$SHA256.class */
    public static class SHA256 extends SignatureDSA {
        @Override // com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureDSA, com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_DSA_SHA256;
        }
    }
}
