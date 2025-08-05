package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Manifest;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignatureProperties;
import javax.xml.crypto.dsig.SignatureProperty;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.TransformService;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.jcp.xml.dsig.internal.dom.DOMDigestMethod;
import org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod;
import org.jcp.xml.dsig.internal.dom.DOMSignatureMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMXMLSignatureFactory.class */
public final class DOMXMLSignatureFactory extends XMLSignatureFactory {
    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public XMLSignature newXMLSignature(SignedInfo signedInfo, KeyInfo keyInfo) {
        return new DOMXMLSignature(signedInfo, keyInfo, null, null, null);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public XMLSignature newXMLSignature(SignedInfo signedInfo, KeyInfo keyInfo, List list, String str, String str2) {
        return new DOMXMLSignature(signedInfo, keyInfo, list, str, str2);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public Reference newReference(String str, DigestMethod digestMethod) {
        return newReference(str, digestMethod, null, null, null);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public Reference newReference(String str, DigestMethod digestMethod, List list, String str2, String str3) {
        return new DOMReference(str, str2, digestMethod, list, str3, getProvider());
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public Reference newReference(String str, DigestMethod digestMethod, List list, Data data, List list2, String str2, String str3) {
        if (list == null) {
            throw new NullPointerException("appliedTransforms cannot be null");
        }
        if (list.isEmpty()) {
            throw new NullPointerException("appliedTransforms cannot be empty");
        }
        if (data == null) {
            throw new NullPointerException("result cannot be null");
        }
        return new DOMReference(str, str2, digestMethod, list, data, list2, str3, getProvider());
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public Reference newReference(String str, DigestMethod digestMethod, List list, String str2, String str3, byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("digestValue cannot be null");
        }
        return new DOMReference(str, str2, digestMethod, null, null, list, str3, bArr, getProvider());
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public SignedInfo newSignedInfo(CanonicalizationMethod canonicalizationMethod, SignatureMethod signatureMethod, List list) {
        return newSignedInfo(canonicalizationMethod, signatureMethod, list, null);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public SignedInfo newSignedInfo(CanonicalizationMethod canonicalizationMethod, SignatureMethod signatureMethod, List list, String str) {
        return new DOMSignedInfo(canonicalizationMethod, signatureMethod, list, str);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public XMLObject newXMLObject(List list, String str, String str2, String str3) {
        return new DOMXMLObject(list, str, str2, str3);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public Manifest newManifest(List list) {
        return newManifest(list, null);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public Manifest newManifest(List list, String str) {
        return new DOMManifest(list, str);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public SignatureProperties newSignatureProperties(List list, String str) {
        return new DOMSignatureProperties(list, str);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public SignatureProperty newSignatureProperty(List list, String str, String str2) {
        return new DOMSignatureProperty(list, str, str2);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public XMLSignature unmarshalXMLSignature(XMLValidateContext xMLValidateContext) throws MarshalException {
        if (xMLValidateContext == null) {
            throw new NullPointerException("context cannot be null");
        }
        return unmarshal(((DOMValidateContext) xMLValidateContext).getNode(), xMLValidateContext);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public XMLSignature unmarshalXMLSignature(XMLStructure xMLStructure) throws MarshalException {
        if (xMLStructure == null) {
            throw new NullPointerException("xmlStructure cannot be null");
        }
        if (!(xMLStructure instanceof javax.xml.crypto.dom.DOMStructure)) {
            throw new ClassCastException("xmlStructure must be of type DOMStructure");
        }
        return unmarshal(((javax.xml.crypto.dom.DOMStructure) xMLStructure).getNode(), new UnmarshalContext());
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMXMLSignatureFactory$UnmarshalContext.class */
    private static class UnmarshalContext extends DOMCryptoContext {
        UnmarshalContext() {
        }
    }

    private XMLSignature unmarshal(Node node, XMLCryptoContext xMLCryptoContext) throws MarshalException {
        Element documentElement;
        node.normalize();
        if (node.getNodeType() == 9) {
            documentElement = ((Document) node).getDocumentElement();
        } else if (node.getNodeType() == 1) {
            documentElement = (Element) node;
        } else {
            throw new MarshalException("Signature element is not a proper Node");
        }
        String localName = documentElement.getLocalName();
        String namespaceURI = documentElement.getNamespaceURI();
        if (localName == null || namespaceURI == null) {
            throw new MarshalException("Document implementation must support DOM Level 2 and be namespace aware");
        }
        if (Constants._TAG_SIGNATURE.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
            try {
                return new DOMXMLSignature(documentElement, xMLCryptoContext, getProvider());
            } catch (MarshalException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new MarshalException(e3);
            }
        }
        throw new MarshalException("Invalid Signature tag: " + namespaceURI + CallSiteDescriptor.TOKEN_DELIMITER + localName);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public boolean isFeatureSupported(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        return false;
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public DigestMethod newDigestMethod(String str, DigestMethodParameterSpec digestMethodParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (str == null) {
            throw new NullPointerException();
        }
        if (str.equals("http://www.w3.org/2000/09/xmldsig#sha1")) {
            return new DOMDigestMethod.SHA1(digestMethodParameterSpec);
        }
        if (str.equals(MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA224)) {
            return new DOMDigestMethod.SHA224(digestMethodParameterSpec);
        }
        if (str.equals("http://www.w3.org/2001/04/xmlenc#sha256")) {
            return new DOMDigestMethod.SHA256(digestMethodParameterSpec);
        }
        if (str.equals(MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA384)) {
            return new DOMDigestMethod.SHA384(digestMethodParameterSpec);
        }
        if (str.equals("http://www.w3.org/2001/04/xmlenc#sha512")) {
            return new DOMDigestMethod.SHA512(digestMethodParameterSpec);
        }
        if (str.equals("http://www.w3.org/2001/04/xmlenc#ripemd160")) {
            return new DOMDigestMethod.RIPEMD160(digestMethodParameterSpec);
        }
        throw new NoSuchAlgorithmException("unsupported algorithm");
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public SignatureMethod newSignatureMethod(String str, SignatureMethodParameterSpec signatureMethodParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (str == null) {
            throw new NullPointerException();
        }
        if (str.equals("http://www.w3.org/2000/09/xmldsig#rsa-sha1")) {
            return new DOMSignatureMethod.SHA1withRSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA224)) {
            return new DOMSignatureMethod.SHA224withRSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256)) {
            return new DOMSignatureMethod.SHA256withRSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384)) {
            return new DOMSignatureMethod.SHA384withRSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512)) {
            return new DOMSignatureMethod.SHA512withRSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_RIPEMD160)) {
            return new DOMSignatureMethod.RIPEMD160withRSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1_MGF1)) {
            return new DOMSignatureMethod.SHA1withRSAandMGF1(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA224_MGF1)) {
            return new DOMSignatureMethod.SHA224withRSAandMGF1(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256_MGF1)) {
            return new DOMSignatureMethod.SHA256withRSAandMGF1(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384_MGF1)) {
            return new DOMSignatureMethod.SHA384withRSAandMGF1(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512_MGF1)) {
            return new DOMSignatureMethod.SHA512withRSAandMGF1(signatureMethodParameterSpec);
        }
        if (str.equals("http://www.w3.org/2007/05/xmldsig-more#ripemd160-rsa-MGF1")) {
            return new DOMSignatureMethod.RIPEMD160withRSAandMGF1(signatureMethodParameterSpec);
        }
        if (str.equals("http://www.w3.org/2000/09/xmldsig#dsa-sha1")) {
            return new DOMSignatureMethod.SHA1withDSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_DSA_SHA256)) {
            return new DOMSignatureMethod.SHA256withDSA(signatureMethodParameterSpec);
        }
        if (str.equals("http://www.w3.org/2000/09/xmldsig#hmac-sha1")) {
            return new DOMHMACSignatureMethod.SHA1(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_MAC_HMAC_SHA224)) {
            return new DOMHMACSignatureMethod.SHA224(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_MAC_HMAC_SHA256)) {
            return new DOMHMACSignatureMethod.SHA256(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_MAC_HMAC_SHA384)) {
            return new DOMHMACSignatureMethod.SHA384(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_MAC_HMAC_SHA512)) {
            return new DOMHMACSignatureMethod.SHA512(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_MAC_HMAC_RIPEMD160)) {
            return new DOMHMACSignatureMethod.RIPEMD160(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA1)) {
            return new DOMSignatureMethod.SHA1withECDSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA224)) {
            return new DOMSignatureMethod.SHA224withECDSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA256)) {
            return new DOMSignatureMethod.SHA256withECDSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA384)) {
            return new DOMSignatureMethod.SHA384withECDSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA512)) {
            return new DOMSignatureMethod.SHA512withECDSA(signatureMethodParameterSpec);
        }
        if (str.equals(com.sun.org.apache.xml.internal.security.signature.XMLSignature.ALGO_ID_SIGNATURE_ECDSA_RIPEMD160)) {
            return new DOMSignatureMethod.RIPEMD160withECDSA(signatureMethodParameterSpec);
        }
        throw new NoSuchAlgorithmException("unsupported algorithm");
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public Transform newTransform(String str, TransformParameterSpec transformParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        TransformService transformService;
        if (getProvider() == null) {
            transformService = TransformService.getInstance(str, "DOM");
        } else {
            try {
                transformService = TransformService.getInstance(str, "DOM", getProvider());
            } catch (NoSuchAlgorithmException e2) {
                transformService = TransformService.getInstance(str, "DOM");
            }
        }
        transformService.init(transformParameterSpec);
        return new DOMTransform(transformService);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public Transform newTransform(String str, XMLStructure xMLStructure) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        TransformService transformService;
        if (getProvider() == null) {
            transformService = TransformService.getInstance(str, "DOM");
        } else {
            try {
                transformService = TransformService.getInstance(str, "DOM", getProvider());
            } catch (NoSuchAlgorithmException e2) {
                transformService = TransformService.getInstance(str, "DOM");
            }
        }
        if (xMLStructure == null) {
            transformService.init(null);
        } else {
            transformService.init(xMLStructure, null);
        }
        return new DOMTransform(transformService);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public CanonicalizationMethod newCanonicalizationMethod(String str, C14NMethodParameterSpec c14NMethodParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        TransformService transformService;
        if (getProvider() == null) {
            transformService = TransformService.getInstance(str, "DOM");
        } else {
            try {
                transformService = TransformService.getInstance(str, "DOM", getProvider());
            } catch (NoSuchAlgorithmException e2) {
                transformService = TransformService.getInstance(str, "DOM");
            }
        }
        transformService.init(c14NMethodParameterSpec);
        return new DOMCanonicalizationMethod(transformService);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public CanonicalizationMethod newCanonicalizationMethod(String str, XMLStructure xMLStructure) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        TransformService transformService;
        if (getProvider() == null) {
            transformService = TransformService.getInstance(str, "DOM");
        } else {
            try {
                transformService = TransformService.getInstance(str, "DOM", getProvider());
            } catch (NoSuchAlgorithmException e2) {
                transformService = TransformService.getInstance(str, "DOM");
            }
        }
        if (xMLStructure == null) {
            transformService.init(null);
        } else {
            transformService.init(xMLStructure, null);
        }
        return new DOMCanonicalizationMethod(transformService);
    }

    @Override // javax.xml.crypto.dsig.XMLSignatureFactory
    public URIDereferencer getURIDereferencer() {
        return DOMURIDereferencer.INSTANCE;
    }
}
