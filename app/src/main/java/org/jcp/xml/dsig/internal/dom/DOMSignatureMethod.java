package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.DSAKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import org.jcp.xml.dsig.internal.SignerOutputStream;
import org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod;
import org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import sun.security.util.KeyUtil;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod.class */
public abstract class DOMSignatureMethod extends AbstractDOMSignatureMethod {
    private static final Logger LOG = LoggerFactory.getLogger(DOMSignatureMethod.class);
    private SignatureMethodParameterSpec params;
    private Signature signature;
    static final String RSA_SHA224 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha224";
    static final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
    static final String RSA_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
    static final String RSA_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
    static final String RSA_RIPEMD160 = "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160";
    static final String ECDSA_SHA1 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1";
    static final String ECDSA_SHA224 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224";
    static final String ECDSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256";
    static final String ECDSA_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384";
    static final String ECDSA_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512";
    static final String DSA_SHA256 = "http://www.w3.org/2009/xmldsig11#dsa-sha256";
    static final String ECDSA_RIPEMD160 = "http://www.w3.org/2007/05/xmldsig-more#ecdsa-ripemd160";
    static final String RSA_SHA1_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha1-rsa-MGF1";
    static final String RSA_SHA224_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha224-rsa-MGF1";
    static final String RSA_SHA256_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha256-rsa-MGF1";
    static final String RSA_SHA384_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha384-rsa-MGF1";
    static final String RSA_SHA512_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#sha512-rsa-MGF1";
    static final String RSA_RIPEMD160_MGF1 = "http://www.w3.org/2007/05/xmldsig-more#ripemd160-rsa-MGF1";

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod, org.jcp.xml.dsig.internal.dom.DOMStructure
    public /* bridge */ /* synthetic */ void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        super.marshal(node, str, dOMCryptoContext);
    }

    DOMSignatureMethod(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof SignatureMethodParameterSpec)) {
            throw new InvalidAlgorithmParameterException("params must be of type SignatureMethodParameterSpec");
        }
        checkParams((SignatureMethodParameterSpec) algorithmParameterSpec);
        this.params = (SignatureMethodParameterSpec) algorithmParameterSpec;
    }

    DOMSignatureMethod(Element element) throws MarshalException {
        Element firstChildElement = DOMUtils.getFirstChildElement(element);
        if (firstChildElement != null) {
            this.params = unmarshalParams(firstChildElement);
        }
        try {
            checkParams(this.params);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new MarshalException(e2);
        }
    }

    static SignatureMethod unmarshal(Element element) throws MarshalException, DOMException {
        String attributeValue = DOMUtils.getAttributeValue(element, Constants._ATT_ALGORITHM);
        if (attributeValue.equals("http://www.w3.org/2000/09/xmldsig#rsa-sha1")) {
            return new SHA1withRSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha224")) {
            return new SHA224withRSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256")) {
            return new SHA256withRSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384")) {
            return new SHA384withRSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512")) {
            return new SHA512withRSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160")) {
            return new RIPEMD160withRSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2007/05/xmldsig-more#sha1-rsa-MGF1")) {
            return new SHA1withRSAandMGF1(element);
        }
        if (attributeValue.equals("http://www.w3.org/2007/05/xmldsig-more#sha224-rsa-MGF1")) {
            return new SHA224withRSAandMGF1(element);
        }
        if (attributeValue.equals("http://www.w3.org/2007/05/xmldsig-more#sha256-rsa-MGF1")) {
            return new SHA256withRSAandMGF1(element);
        }
        if (attributeValue.equals("http://www.w3.org/2007/05/xmldsig-more#sha384-rsa-MGF1")) {
            return new SHA384withRSAandMGF1(element);
        }
        if (attributeValue.equals("http://www.w3.org/2007/05/xmldsig-more#sha512-rsa-MGF1")) {
            return new SHA512withRSAandMGF1(element);
        }
        if (attributeValue.equals(RSA_RIPEMD160_MGF1)) {
            return new RIPEMD160withRSAandMGF1(element);
        }
        if (attributeValue.equals("http://www.w3.org/2000/09/xmldsig#dsa-sha1")) {
            return new SHA1withDSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2009/xmldsig11#dsa-sha256")) {
            return new SHA256withDSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1")) {
            return new SHA1withECDSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224")) {
            return new SHA224withECDSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256")) {
            return new SHA256withECDSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384")) {
            return new SHA384withECDSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512")) {
            return new SHA512withECDSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2007/05/xmldsig-more#ecdsa-ripemd160")) {
            return new RIPEMD160withECDSA(element);
        }
        if (attributeValue.equals("http://www.w3.org/2000/09/xmldsig#hmac-sha1")) {
            return new DOMHMACSignatureMethod.SHA1(element);
        }
        if (attributeValue.equals(XMLSignature.ALGO_ID_MAC_HMAC_SHA224)) {
            return new DOMHMACSignatureMethod.SHA224(element);
        }
        if (attributeValue.equals(XMLSignature.ALGO_ID_MAC_HMAC_SHA256)) {
            return new DOMHMACSignatureMethod.SHA256(element);
        }
        if (attributeValue.equals(XMLSignature.ALGO_ID_MAC_HMAC_SHA384)) {
            return new DOMHMACSignatureMethod.SHA384(element);
        }
        if (attributeValue.equals(XMLSignature.ALGO_ID_MAC_HMAC_SHA512)) {
            return new DOMHMACSignatureMethod.SHA512(element);
        }
        if (attributeValue.equals(XMLSignature.ALGO_ID_MAC_HMAC_RIPEMD160)) {
            return new DOMHMACSignatureMethod.RIPEMD160(element);
        }
        throw new MarshalException("unsupported SignatureMethod algorithm: " + attributeValue);
    }

    @Override // javax.xml.crypto.dsig.SignatureMethod, javax.xml.crypto.AlgorithmMethod
    public final AlgorithmParameterSpec getParameterSpec() {
        return this.params;
    }

    Signature getSignature(Provider provider) throws NoSuchAlgorithmException {
        if (provider == null) {
            return Signature.getInstance(getJCAAlgorithm());
        }
        return Signature.getInstance(getJCAAlgorithm(), provider);
    }

    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x01a0: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:63:0x01a0 */
    /* JADX WARN: Type inference failed for: r0v66, types: [java.security.Provider, org.jcp.xml.dsig.internal.SignerOutputStream] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r7v0, types: [org.jcp.xml.dsig.internal.dom.DOMSignatureMethod] */
    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    boolean verify(Key key, SignedInfo signedInfo, byte[] bArr, XMLValidateContext xMLValidateContext) throws SignatureException, XMLSignatureException, InvalidKeyException {
        if (key == null || signedInfo == null || bArr == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof PublicKey)) {
            throw new InvalidKeyException("key must be PublicKey");
        }
        checkKeySize(xMLValidateContext, key);
        if (this.signature == null) {
            try {
                this.signature = getSignature((Provider) xMLValidateContext.getProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider"));
            } catch (NoSuchAlgorithmException e2) {
                throw new XMLSignatureException(e2);
            }
        }
        this.signature.initVerify((PublicKey) key);
        LOG.debug("Signature provider: {}", this.signature.getProvider());
        LOG.debug("Verifying with key: {}", key);
        LOG.debug("JCA Algorithm: {}", getJCAAlgorithm());
        LOG.debug("Signature Bytes length: {}", Integer.valueOf(bArr.length));
        try {
            try {
                SignerOutputStream signerOutputStream = new SignerOutputStream(this.signature);
                Throwable th = null;
                ((DOMSignedInfo) signedInfo).canonicalize(xMLValidateContext, signerOutputStream);
                AbstractDOMSignatureMethod.Type algorithmType = getAlgorithmType();
                if (algorithmType == AbstractDOMSignatureMethod.Type.DSA) {
                    boolean zVerify = this.signature.verify(JavaUtils.convertDsaXMLDSIGtoASN1(bArr, ((DSAKey) key).getParams().getQ().bitLength() / 8));
                    if (signerOutputStream != null) {
                        if (0 != 0) {
                            try {
                                signerOutputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            signerOutputStream.close();
                        }
                    }
                    return zVerify;
                }
                if (algorithmType == AbstractDOMSignatureMethod.Type.ECDSA) {
                    boolean zVerify2 = this.signature.verify(SignatureECDSA.convertXMLDSIGtoASN1(bArr));
                    if (signerOutputStream != null) {
                        if (0 != 0) {
                            try {
                                signerOutputStream.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            signerOutputStream.close();
                        }
                    }
                    return zVerify2;
                }
                boolean zVerify3 = this.signature.verify(bArr);
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
                return zVerify3;
            } finally {
            }
        } catch (IOException e3) {
            throw new XMLSignatureException(e3);
        }
        throw new XMLSignatureException(e3);
    }

    private static void checkKeySize(XMLCryptoContext xMLCryptoContext, Key key) throws XMLSignatureException {
        if (Utils.secureValidation(xMLCryptoContext)) {
            int keySize = KeyUtil.getKeySize(key);
            if (keySize == -1) {
                LOG.debug("Size for " + key.getAlgorithm() + " key cannot be determined");
            } else if (Policy.restrictKey(key.getAlgorithm(), keySize)) {
                throw new XMLSignatureException(key.getAlgorithm() + " keys less than " + Policy.minKeySize(key.getAlgorithm()) + " bits are forbidden when secure validation is enabled");
            }
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r12v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x0182: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:61:0x0182 */
    /* JADX WARN: Type inference failed for: r0v67, types: [java.security.Provider, org.jcp.xml.dsig.internal.SignerOutputStream] */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r7v0, types: [org.jcp.xml.dsig.internal.dom.DOMSignatureMethod] */
    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    byte[] sign(Key key, SignedInfo signedInfo, XMLSignContext xMLSignContext) throws XMLSignatureException, InvalidKeyException {
        ?? r12;
        ?? r0;
        if (key == null || signedInfo == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof PrivateKey)) {
            throw new InvalidKeyException("key must be PrivateKey");
        }
        checkKeySize(xMLSignContext, key);
        if (this.signature == null) {
            r0 = (Provider) xMLSignContext.getProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider");
            try {
                this.signature = getSignature(r0);
            } catch (NoSuchAlgorithmException e2) {
                throw new XMLSignatureException(e2);
            }
        }
        this.signature.initSign((PrivateKey) key);
        LOG.debug("Signature provider: {}", this.signature.getProvider());
        LOG.debug("Signing with key: {}", key);
        LOG.debug("JCA Algorithm: {}", getJCAAlgorithm());
        try {
            try {
                SignerOutputStream signerOutputStream = new SignerOutputStream(this.signature);
                Throwable th = null;
                ((DOMSignedInfo) signedInfo).canonicalize(xMLSignContext, signerOutputStream);
                AbstractDOMSignatureMethod.Type algorithmType = getAlgorithmType();
                if (algorithmType == AbstractDOMSignatureMethod.Type.DSA) {
                    byte[] bArrConvertDsaASN1toXMLDSIG = JavaUtils.convertDsaASN1toXMLDSIG(this.signature.sign(), ((DSAKey) key).getParams().getQ().bitLength() / 8);
                    if (signerOutputStream != null) {
                        if (0 != 0) {
                            try {
                                signerOutputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            signerOutputStream.close();
                        }
                    }
                    return bArrConvertDsaASN1toXMLDSIG;
                }
                if (algorithmType == AbstractDOMSignatureMethod.Type.ECDSA) {
                    byte[] bArrConvertASN1toXMLDSIG = SignatureECDSA.convertASN1toXMLDSIG(this.signature.sign());
                    if (signerOutputStream != null) {
                        if (0 != 0) {
                            try {
                                signerOutputStream.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            signerOutputStream.close();
                        }
                    }
                    return bArrConvertASN1toXMLDSIG;
                }
                byte[] bArrSign = this.signature.sign();
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
                return bArrSign;
            } catch (IOException e3) {
                throw new XMLSignatureException(e3);
            } catch (SignatureException e4) {
                throw new XMLSignatureException(e4);
            }
        } catch (Throwable th5) {
            if (r0 != 0) {
                if (r12 != 0) {
                    try {
                        r0.close();
                    } catch (Throwable th6) {
                        r12.addSuppressed(th6);
                    }
                } else {
                    r0.close();
                }
            }
            throw th5;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$AbstractRSAPSSSignatureMethod.class */
    static abstract class AbstractRSAPSSSignatureMethod extends DOMSignatureMethod {
        public abstract PSSParameterSpec getPSSParameterSpec();

        AbstractRSAPSSSignatureMethod(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        AbstractRSAPSSSignatureMethod(Element element) throws MarshalException {
            super(element);
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMSignatureMethod
        Signature getSignature(Provider provider) throws NoSuchAlgorithmException {
            Signature signature;
            try {
                if (provider == null) {
                    signature = Signature.getInstance("RSASSA-PSS");
                } else {
                    signature = Signature.getInstance("RSASSA-PSS", provider);
                }
                Signature signature2 = signature;
                try {
                    signature2.setParameter(getPSSParameterSpec());
                    return signature2;
                } catch (InvalidAlgorithmParameterException e2) {
                    throw new NoSuchAlgorithmException("Should not happen", e2);
                }
            } catch (NoSuchAlgorithmException e3) {
                if (provider == null) {
                    return Signature.getInstance(getJCAAlgorithm());
                }
                return Signature.getInstance(getJCAAlgorithm(), provider);
            }
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA1withRSA.class */
    static final class SHA1withRSA extends DOMSignatureMethod {
        SHA1withRSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA1withRSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA1withRSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA224withRSA.class */
    static final class SHA224withRSA extends DOMSignatureMethod {
        SHA224withRSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA224withRSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha224";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA224withRSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA256withRSA.class */
    static final class SHA256withRSA extends DOMSignatureMethod {
        SHA256withRSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA256withRSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA256withRSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA384withRSA.class */
    static final class SHA384withRSA extends DOMSignatureMethod {
        SHA384withRSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA384withRSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA384withRSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA512withRSA.class */
    static final class SHA512withRSA extends DOMSignatureMethod {
        SHA512withRSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA512withRSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA512withRSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$RIPEMD160withRSA.class */
    static final class RIPEMD160withRSA extends DOMSignatureMethod {
        RIPEMD160withRSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        RIPEMD160withRSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "RIPEMD160withRSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA1withRSAandMGF1.class */
    static final class SHA1withRSAandMGF1 extends AbstractRSAPSSSignatureMethod {
        private static PSSParameterSpec spec = new PSSParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, 20, 1);

        SHA1withRSAandMGF1(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA1withRSAandMGF1(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2007/05/xmldsig-more#sha1-rsa-MGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMSignatureMethod.AbstractRSAPSSSignatureMethod
        public PSSParameterSpec getPSSParameterSpec() {
            return spec;
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA1withRSAandMGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA224withRSAandMGF1.class */
    static final class SHA224withRSAandMGF1 extends AbstractRSAPSSSignatureMethod {
        private static PSSParameterSpec spec = new PSSParameterSpec("SHA-224", "MGF1", MGF1ParameterSpec.SHA224, 28, 1);

        SHA224withRSAandMGF1(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA224withRSAandMGF1(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2007/05/xmldsig-more#sha224-rsa-MGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMSignatureMethod.AbstractRSAPSSSignatureMethod
        public PSSParameterSpec getPSSParameterSpec() {
            return spec;
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA224withRSAandMGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA256withRSAandMGF1.class */
    static final class SHA256withRSAandMGF1 extends AbstractRSAPSSSignatureMethod {
        private static PSSParameterSpec spec = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);

        SHA256withRSAandMGF1(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA256withRSAandMGF1(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2007/05/xmldsig-more#sha256-rsa-MGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMSignatureMethod.AbstractRSAPSSSignatureMethod
        public PSSParameterSpec getPSSParameterSpec() {
            return spec;
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA256withRSAandMGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA384withRSAandMGF1.class */
    static final class SHA384withRSAandMGF1 extends AbstractRSAPSSSignatureMethod {
        private static PSSParameterSpec spec = new PSSParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, 48, 1);

        SHA384withRSAandMGF1(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA384withRSAandMGF1(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2007/05/xmldsig-more#sha384-rsa-MGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMSignatureMethod.AbstractRSAPSSSignatureMethod
        public PSSParameterSpec getPSSParameterSpec() {
            return spec;
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA384withRSAandMGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA512withRSAandMGF1.class */
    static final class SHA512withRSAandMGF1 extends AbstractRSAPSSSignatureMethod {
        private static PSSParameterSpec spec = new PSSParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, 64, 1);

        SHA512withRSAandMGF1(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA512withRSAandMGF1(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2007/05/xmldsig-more#sha512-rsa-MGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMSignatureMethod.AbstractRSAPSSSignatureMethod
        public PSSParameterSpec getPSSParameterSpec() {
            return spec;
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA512withRSAandMGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$RIPEMD160withRSAandMGF1.class */
    static final class RIPEMD160withRSAandMGF1 extends DOMSignatureMethod {
        RIPEMD160withRSAandMGF1(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        RIPEMD160withRSAandMGF1(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return DOMSignatureMethod.RSA_RIPEMD160_MGF1;
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "RIPEMD160withRSAandMGF1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.RSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA1withDSA.class */
    static final class SHA1withDSA extends DOMSignatureMethod {
        SHA1withDSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA1withDSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA1withDSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.DSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA256withDSA.class */
    static final class SHA256withDSA extends DOMSignatureMethod {
        SHA256withDSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA256withDSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2009/xmldsig11#dsa-sha256";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA256withDSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.DSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA1withECDSA.class */
    static final class SHA1withECDSA extends DOMSignatureMethod {
        SHA1withECDSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA1withECDSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA1withECDSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.ECDSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA224withECDSA.class */
    static final class SHA224withECDSA extends DOMSignatureMethod {
        SHA224withECDSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA224withECDSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA224withECDSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.ECDSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA256withECDSA.class */
    static final class SHA256withECDSA extends DOMSignatureMethod {
        SHA256withECDSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA256withECDSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA256withECDSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.ECDSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA384withECDSA.class */
    static final class SHA384withECDSA extends DOMSignatureMethod {
        SHA384withECDSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA384withECDSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA384withECDSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.ECDSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$SHA512withECDSA.class */
    static final class SHA512withECDSA extends DOMSignatureMethod {
        SHA512withECDSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA512withECDSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "SHA512withECDSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.ECDSA;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureMethod$RIPEMD160withECDSA.class */
    static final class RIPEMD160withECDSA extends DOMSignatureMethod {
        RIPEMD160withECDSA(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        RIPEMD160withECDSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2007/05/xmldsig-more#ecdsa-ripemd160";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "RIPEMD160withECDSA";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        AbstractDOMSignatureMethod.Type getAlgorithmType() {
            return AbstractDOMSignatureMethod.Type.ECDSA;
        }
    }
}
