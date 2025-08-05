package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMDigestMethod.class */
public abstract class DOMDigestMethod extends DOMStructure implements DigestMethod {
    static final String SHA224 = "http://www.w3.org/2001/04/xmldsig-more#sha224";
    static final String SHA384 = "http://www.w3.org/2001/04/xmldsig-more#sha384";
    private DigestMethodParameterSpec params;

    abstract String getMessageDigestAlgorithm();

    DOMDigestMethod(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof DigestMethodParameterSpec)) {
            throw new InvalidAlgorithmParameterException("params must be of type DigestMethodParameterSpec");
        }
        checkParams((DigestMethodParameterSpec) algorithmParameterSpec);
        this.params = (DigestMethodParameterSpec) algorithmParameterSpec;
    }

    DOMDigestMethod(Element element) throws MarshalException {
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

    static DigestMethod unmarshal(Element element) throws MarshalException, DOMException {
        String attributeValue = DOMUtils.getAttributeValue(element, Constants._ATT_ALGORITHM);
        if (attributeValue.equals("http://www.w3.org/2000/09/xmldsig#sha1")) {
            return new SHA1(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#sha224")) {
            return new SHA224(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmlenc#sha256")) {
            return new SHA256(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmldsig-more#sha384")) {
            return new SHA384(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmlenc#sha512")) {
            return new SHA512(element);
        }
        if (attributeValue.equals("http://www.w3.org/2001/04/xmlenc#ripemd160")) {
            return new RIPEMD160(element);
        }
        throw new MarshalException("unsupported DigestMethod algorithm: " + attributeValue);
    }

    void checkParams(DigestMethodParameterSpec digestMethodParameterSpec) throws InvalidAlgorithmParameterException {
        if (digestMethodParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
        }
    }

    @Override // javax.xml.crypto.dsig.DigestMethod, javax.xml.crypto.AlgorithmMethod
    public final AlgorithmParameterSpec getParameterSpec() {
        return this.params;
    }

    DigestMethodParameterSpec unmarshalParams(Element element) throws MarshalException {
        throw new MarshalException("no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Element elementCreateElement = DOMUtils.createElement(DOMUtils.getOwnerDocument(node), Constants._TAG_DIGESTMETHOD, "http://www.w3.org/2000/09/xmldsig#", str);
        DOMUtils.setAttribute(elementCreateElement, Constants._ATT_ALGORITHM, getAlgorithm());
        if (this.params != null) {
            marshalParams(elementCreateElement, str);
        }
        node.appendChild(elementCreateElement);
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DigestMethod)) {
            return false;
        }
        DigestMethod digestMethod = (DigestMethod) obj;
        if (this.params == null) {
            zEquals = digestMethod.getParameterSpec() == null;
        } else {
            zEquals = this.params.equals(digestMethod.getParameterSpec());
        }
        return getAlgorithm().equals(digestMethod.getAlgorithm()) && zEquals;
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.params != null) {
            iHashCode = (31 * 17) + this.params.hashCode();
        }
        return (31 * iHashCode) + getAlgorithm().hashCode();
    }

    void marshalParams(Element element, String str) throws MarshalException {
        throw new MarshalException("no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMDigestMethod$SHA1.class */
    static final class SHA1 extends DOMDigestMethod {
        SHA1(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA1(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2000/09/xmldsig#sha1";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMDigestMethod
        String getMessageDigestAlgorithm() {
            return "SHA-1";
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMDigestMethod$SHA224.class */
    static final class SHA224 extends DOMDigestMethod {
        SHA224(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA224(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#sha224";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMDigestMethod
        String getMessageDigestAlgorithm() {
            return "SHA-224";
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMDigestMethod$SHA256.class */
    static final class SHA256 extends DOMDigestMethod {
        SHA256(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA256(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmlenc#sha256";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMDigestMethod
        String getMessageDigestAlgorithm() {
            return "SHA-256";
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMDigestMethod$SHA384.class */
    static final class SHA384 extends DOMDigestMethod {
        SHA384(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA384(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#sha384";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMDigestMethod
        String getMessageDigestAlgorithm() {
            return "SHA-384";
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMDigestMethod$SHA512.class */
    static final class SHA512 extends DOMDigestMethod {
        SHA512(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        SHA512(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmlenc#sha512";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMDigestMethod
        String getMessageDigestAlgorithm() {
            return "SHA-512";
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMDigestMethod$RIPEMD160.class */
    static final class RIPEMD160 extends DOMDigestMethod {
        RIPEMD160(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(algorithmParameterSpec);
        }

        RIPEMD160(Element element) throws MarshalException {
            super(element);
        }

        @Override // javax.xml.crypto.AlgorithmMethod
        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmlenc#ripemd160";
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMDigestMethod
        String getMessageDigestAlgorithm() {
            return "RIPEMD160";
        }
    }
}
