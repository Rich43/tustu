package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import org.jcp.xml.dsig.internal.MacOutputStream;
import org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMHMACSignatureMethod.class */
public abstract class DOMHMACSignatureMethod extends AbstractDOMSignatureMethod {
    private static final Logger LOG = LoggerFactory.getLogger(DOMHMACSignatureMethod.class);
    static final String HMAC_SHA224 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha224";
    static final String HMAC_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
    static final String HMAC_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
    static final String HMAC_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
    static final String HMAC_RIPEMD160 = "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160";
    private Mac hmac;
    private int outputLength;
    private boolean outputLengthSet;
    private SignatureMethodParameterSpec params;

    abstract int getDigestLength();

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

    DOMHMACSignatureMethod(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        checkParams((SignatureMethodParameterSpec) algorithmParameterSpec);
        this.params = (SignatureMethodParameterSpec) algorithmParameterSpec;
    }

    DOMHMACSignatureMethod(Element element) throws MarshalException {
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

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    void checkParams(SignatureMethodParameterSpec signatureMethodParameterSpec) throws InvalidAlgorithmParameterException {
        if (signatureMethodParameterSpec != null) {
            if (!(signatureMethodParameterSpec instanceof HMACParameterSpec)) {
                throw new InvalidAlgorithmParameterException("params must be of type HMACParameterSpec");
            }
            this.outputLength = ((HMACParameterSpec) signatureMethodParameterSpec).getOutputLength();
            this.outputLengthSet = true;
            LOG.debug("Setting outputLength from HMACParameterSpec to: {}", Integer.valueOf(this.outputLength));
        }
    }

    @Override // javax.xml.crypto.dsig.SignatureMethod, javax.xml.crypto.AlgorithmMethod
    public final AlgorithmParameterSpec getParameterSpec() {
        return this.params;
    }

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    SignatureMethodParameterSpec unmarshalParams(Element element) throws MarshalException {
        this.outputLength = Integer.parseInt(element.getFirstChild().getNodeValue());
        this.outputLengthSet = true;
        LOG.debug("unmarshalled outputLength: {}", Integer.valueOf(this.outputLength));
        return new HMACParameterSpec(this.outputLength);
    }

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    void marshalParams(Element element, String str) throws MarshalException {
        Document ownerDocument = DOMUtils.getOwnerDocument(element);
        Element elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_HMACOUTPUTLENGTH, "http://www.w3.org/2000/09/xmldsig#", str);
        elementCreateElement.appendChild(ownerDocument.createTextNode(String.valueOf(this.outputLength)));
        element.appendChild(elementCreateElement);
    }

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    boolean verify(Key key, SignedInfo signedInfo, byte[] bArr, XMLValidateContext xMLValidateContext) throws SignatureException, XMLSignatureException, InvalidKeyException {
        if (key == null || signedInfo == null || bArr == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("key must be SecretKey");
        }
        if (this.hmac == null) {
            try {
                this.hmac = Mac.getInstance(getJCAAlgorithm());
            } catch (NoSuchAlgorithmException e2) {
                throw new XMLSignatureException(e2);
            }
        }
        if (this.outputLengthSet && this.outputLength < getDigestLength()) {
            throw new XMLSignatureException("HMACOutputLength must not be less than " + getDigestLength());
        }
        this.hmac.init(key);
        ((DOMSignedInfo) signedInfo).canonicalize(xMLValidateContext, new MacOutputStream(this.hmac));
        return MessageDigest.isEqual(bArr, this.hmac.doFinal());
    }

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    byte[] sign(Key key, SignedInfo signedInfo, XMLSignContext xMLSignContext) throws XMLSignatureException, InvalidKeyException {
        if (key == null || signedInfo == null) {
            throw new NullPointerException();
        }
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("key must be SecretKey");
        }
        if (this.hmac == null) {
            try {
                this.hmac = Mac.getInstance(getJCAAlgorithm());
            } catch (NoSuchAlgorithmException e2) {
                throw new XMLSignatureException(e2);
            }
        }
        if (this.outputLengthSet && this.outputLength < getDigestLength()) {
            throw new XMLSignatureException("HMACOutputLength must not be less than " + getDigestLength());
        }
        this.hmac.init(key);
        ((DOMSignedInfo) signedInfo).canonicalize(xMLSignContext, new MacOutputStream(this.hmac));
        return this.hmac.doFinal();
    }

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    boolean paramsEqual(AlgorithmParameterSpec algorithmParameterSpec) {
        if (getParameterSpec() == algorithmParameterSpec) {
            return true;
        }
        return (algorithmParameterSpec instanceof HMACParameterSpec) && this.outputLength == ((HMACParameterSpec) algorithmParameterSpec).getOutputLength();
    }

    @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
    AbstractDOMSignatureMethod.Type getAlgorithmType() {
        return AbstractDOMSignatureMethod.Type.HMAC;
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMHMACSignatureMethod$SHA1.class */
    static final class SHA1 extends DOMHMACSignatureMethod {
        SHA1(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA1(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "HmacSHA1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod
        int getDigestLength() {
            return 160;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMHMACSignatureMethod$SHA224.class */
    static final class SHA224 extends DOMHMACSignatureMethod {
        SHA224(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA224(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha224";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "HmacSHA224";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod
        int getDigestLength() {
            return 224;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMHMACSignatureMethod$SHA256.class */
    static final class SHA256 extends DOMHMACSignatureMethod {
        SHA256(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA256(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "HmacSHA256";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod
        int getDigestLength() {
            return 256;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMHMACSignatureMethod$SHA384.class */
    static final class SHA384 extends DOMHMACSignatureMethod {
        SHA384(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA384(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "HmacSHA384";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod
        int getDigestLength() {
            return 384;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMHMACSignatureMethod$SHA512.class */
    static final class SHA512 extends DOMHMACSignatureMethod {
        SHA512(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA512(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "HmacSHA512";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod
        int getDigestLength() {
            return 512;
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMHMACSignatureMethod$RIPEMD160.class */
    static final class RIPEMD160 extends DOMHMACSignatureMethod {
        RIPEMD160(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        RIPEMD160(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160";
        }

        @Override // org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod
        String getJCAAlgorithm() {
            return "HMACRIPEMD160";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod
        int getDigestLength() {
            return 160;
        }
    }
}
