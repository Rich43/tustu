package javax.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.List;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.NoSuchMechanismException;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import sun.security.jca.GetInstance;

/* loaded from: rt.jar:javax/xml/crypto/dsig/XMLSignatureFactory.class */
public abstract class XMLSignatureFactory {
    private String mechanismType;
    private Provider provider;

    public abstract XMLSignature newXMLSignature(SignedInfo signedInfo, KeyInfo keyInfo);

    public abstract XMLSignature newXMLSignature(SignedInfo signedInfo, KeyInfo keyInfo, List list, String str, String str2);

    public abstract Reference newReference(String str, DigestMethod digestMethod);

    public abstract Reference newReference(String str, DigestMethod digestMethod, List list, String str2, String str3);

    public abstract Reference newReference(String str, DigestMethod digestMethod, List list, String str2, String str3, byte[] bArr);

    public abstract Reference newReference(String str, DigestMethod digestMethod, List list, Data data, List list2, String str2, String str3);

    public abstract SignedInfo newSignedInfo(CanonicalizationMethod canonicalizationMethod, SignatureMethod signatureMethod, List list);

    public abstract SignedInfo newSignedInfo(CanonicalizationMethod canonicalizationMethod, SignatureMethod signatureMethod, List list, String str);

    public abstract XMLObject newXMLObject(List list, String str, String str2, String str3);

    public abstract Manifest newManifest(List list);

    public abstract Manifest newManifest(List list, String str);

    public abstract SignatureProperty newSignatureProperty(List list, String str, String str2);

    public abstract SignatureProperties newSignatureProperties(List list, String str);

    public abstract DigestMethod newDigestMethod(String str, DigestMethodParameterSpec digestMethodParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

    public abstract SignatureMethod newSignatureMethod(String str, SignatureMethodParameterSpec signatureMethodParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

    public abstract Transform newTransform(String str, TransformParameterSpec transformParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

    public abstract Transform newTransform(String str, XMLStructure xMLStructure) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

    public abstract CanonicalizationMethod newCanonicalizationMethod(String str, C14NMethodParameterSpec c14NMethodParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

    public abstract CanonicalizationMethod newCanonicalizationMethod(String str, XMLStructure xMLStructure) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

    public abstract XMLSignature unmarshalXMLSignature(XMLValidateContext xMLValidateContext) throws MarshalException;

    public abstract XMLSignature unmarshalXMLSignature(XMLStructure xMLStructure) throws MarshalException;

    public abstract boolean isFeatureSupported(String str);

    public abstract URIDereferencer getURIDereferencer();

    protected XMLSignatureFactory() {
    }

    public static XMLSignatureFactory getInstance(String str) {
        if (str == null) {
            throw new NullPointerException("mechanismType cannot be null");
        }
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("XMLSignatureFactory", (Class<?>) null, str);
            XMLSignatureFactory xMLSignatureFactory = (XMLSignatureFactory) getInstance.impl;
            xMLSignatureFactory.mechanismType = str;
            xMLSignatureFactory.provider = getInstance.provider;
            return xMLSignatureFactory;
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchMechanismException(e2);
        }
    }

    public static XMLSignatureFactory getInstance(String str, Provider provider) {
        if (str == null) {
            throw new NullPointerException("mechanismType cannot be null");
        }
        if (provider == null) {
            throw new NullPointerException("provider cannot be null");
        }
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("XMLSignatureFactory", (Class<?>) null, str, provider);
            XMLSignatureFactory xMLSignatureFactory = (XMLSignatureFactory) getInstance.impl;
            xMLSignatureFactory.mechanismType = str;
            xMLSignatureFactory.provider = getInstance.provider;
            return xMLSignatureFactory;
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchMechanismException(e2);
        }
    }

    public static XMLSignatureFactory getInstance(String str, String str2) throws NoSuchProviderException {
        if (str == null) {
            throw new NullPointerException("mechanismType cannot be null");
        }
        if (str2 == null) {
            throw new NullPointerException("provider cannot be null");
        }
        if (str2.length() == 0) {
            throw new NoSuchProviderException();
        }
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("XMLSignatureFactory", (Class<?>) null, str, str2);
            XMLSignatureFactory xMLSignatureFactory = (XMLSignatureFactory) getInstance.impl;
            xMLSignatureFactory.mechanismType = str;
            xMLSignatureFactory.provider = getInstance.provider;
            return xMLSignatureFactory;
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchMechanismException(e2);
        }
    }

    public static XMLSignatureFactory getInstance() {
        return getInstance("DOM");
    }

    public final String getMechanismType() {
        return this.mechanismType;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final KeyInfoFactory getKeyInfoFactory() {
        return KeyInfoFactory.getInstance(getMechanismType(), getProvider());
    }
}
