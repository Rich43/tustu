package javax.xml.crypto.dsig.keyinfo;

import java.math.BigInteger;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.PublicKey;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.NoSuchMechanismException;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.XMLStructure;
import sun.security.jca.GetInstance;

/* loaded from: rt.jar:javax/xml/crypto/dsig/keyinfo/KeyInfoFactory.class */
public abstract class KeyInfoFactory {
    private String mechanismType;
    private Provider provider;

    public abstract KeyInfo newKeyInfo(List list);

    public abstract KeyInfo newKeyInfo(List list, String str);

    public abstract KeyName newKeyName(String str);

    public abstract KeyValue newKeyValue(PublicKey publicKey) throws KeyException;

    public abstract PGPData newPGPData(byte[] bArr);

    public abstract PGPData newPGPData(byte[] bArr, byte[] bArr2, List list);

    public abstract PGPData newPGPData(byte[] bArr, List list);

    public abstract RetrievalMethod newRetrievalMethod(String str);

    public abstract RetrievalMethod newRetrievalMethod(String str, String str2, List list);

    public abstract X509Data newX509Data(List list);

    public abstract X509IssuerSerial newX509IssuerSerial(String str, BigInteger bigInteger);

    public abstract boolean isFeatureSupported(String str);

    public abstract URIDereferencer getURIDereferencer();

    public abstract KeyInfo unmarshalKeyInfo(XMLStructure xMLStructure) throws MarshalException;

    protected KeyInfoFactory() {
    }

    public static KeyInfoFactory getInstance(String str) {
        if (str == null) {
            throw new NullPointerException("mechanismType cannot be null");
        }
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("KeyInfoFactory", (Class<?>) null, str);
            KeyInfoFactory keyInfoFactory = (KeyInfoFactory) getInstance.impl;
            keyInfoFactory.mechanismType = str;
            keyInfoFactory.provider = getInstance.provider;
            return keyInfoFactory;
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchMechanismException(e2);
        }
    }

    public static KeyInfoFactory getInstance(String str, Provider provider) {
        if (str == null) {
            throw new NullPointerException("mechanismType cannot be null");
        }
        if (provider == null) {
            throw new NullPointerException("provider cannot be null");
        }
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("KeyInfoFactory", (Class<?>) null, str, provider);
            KeyInfoFactory keyInfoFactory = (KeyInfoFactory) getInstance.impl;
            keyInfoFactory.mechanismType = str;
            keyInfoFactory.provider = getInstance.provider;
            return keyInfoFactory;
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchMechanismException(e2);
        }
    }

    public static KeyInfoFactory getInstance(String str, String str2) throws NoSuchProviderException {
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
            GetInstance.Instance getInstance = GetInstance.getInstance("KeyInfoFactory", (Class<?>) null, str, str2);
            KeyInfoFactory keyInfoFactory = (KeyInfoFactory) getInstance.impl;
            keyInfoFactory.mechanismType = str;
            keyInfoFactory.provider = getInstance.provider;
            return keyInfoFactory;
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchMechanismException(e2);
        }
    }

    public static KeyInfoFactory getInstance() {
        return getInstance("DOM");
    }

    public final String getMechanismType() {
        return this.mechanismType;
    }

    public final Provider getProvider() {
        return this.provider;
    }
}
