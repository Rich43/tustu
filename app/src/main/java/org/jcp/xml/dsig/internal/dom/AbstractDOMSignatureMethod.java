package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/AbstractDOMSignatureMethod.class */
abstract class AbstractDOMSignatureMethod extends DOMStructure implements SignatureMethod {

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/AbstractDOMSignatureMethod$Type.class */
    enum Type {
        DSA,
        RSA,
        ECDSA,
        HMAC
    }

    abstract boolean verify(Key key, SignedInfo signedInfo, byte[] bArr, XMLValidateContext xMLValidateContext) throws SignatureException, XMLSignatureException, InvalidKeyException;

    abstract byte[] sign(Key key, SignedInfo signedInfo, XMLSignContext xMLSignContext) throws XMLSignatureException, InvalidKeyException;

    abstract String getJCAAlgorithm();

    abstract Type getAlgorithmType();

    AbstractDOMSignatureMethod() {
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Element elementCreateElement = DOMUtils.createElement(DOMUtils.getOwnerDocument(node), Constants._TAG_SIGNATUREMETHOD, "http://www.w3.org/2000/09/xmldsig#", str);
        DOMUtils.setAttribute(elementCreateElement, Constants._ATT_ALGORITHM, getAlgorithm());
        if (getParameterSpec() != null) {
            marshalParams(elementCreateElement, str);
        }
        node.appendChild(elementCreateElement);
    }

    void marshalParams(Element element, String str) throws MarshalException {
        throw new MarshalException("no parameters should be specified for the " + getAlgorithm() + " SignatureMethod algorithm");
    }

    SignatureMethodParameterSpec unmarshalParams(Element element) throws MarshalException {
        throw new MarshalException("no parameters should be specified for the " + getAlgorithm() + " SignatureMethod algorithm");
    }

    void checkParams(SignatureMethodParameterSpec signatureMethodParameterSpec) throws InvalidAlgorithmParameterException {
        if (signatureMethodParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("no parameters should be specified for the " + getAlgorithm() + " SignatureMethod algorithm");
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SignatureMethod)) {
            return false;
        }
        SignatureMethod signatureMethod = (SignatureMethod) obj;
        return getAlgorithm().equals(signatureMethod.getAlgorithm()) && paramsEqual(signatureMethod.getParameterSpec());
    }

    public int hashCode() {
        int iHashCode = (31 * 17) + getAlgorithm().hashCode();
        AlgorithmParameterSpec parameterSpec = getParameterSpec();
        if (parameterSpec != null) {
            iHashCode = (31 * iHashCode) + parameterSpec.hashCode();
        }
        return iHashCode;
    }

    boolean paramsEqual(AlgorithmParameterSpec algorithmParameterSpec) {
        return getParameterSpec() == algorithmParameterSpec;
    }
}
