package javax.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.Map;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import sun.security.jca.GetInstance;

/* loaded from: rt.jar:javax/xml/crypto/dsig/TransformService.class */
public abstract class TransformService implements Transform {
    private String algorithm;
    private String mechanism;
    private Provider provider;

    public abstract void init(TransformParameterSpec transformParameterSpec) throws InvalidAlgorithmParameterException;

    public abstract void marshalParams(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws MarshalException;

    public abstract void init(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws InvalidAlgorithmParameterException;

    protected TransformService() {
    }

    public static TransformService getInstance(String str, String str2) throws NoSuchAlgorithmException {
        if (str2 == null || str == null) {
            throw new NullPointerException();
        }
        boolean z2 = false;
        if (str2.equals("DOM")) {
            z2 = true;
        }
        for (Provider.Service service : GetInstance.getServices("TransformService", str)) {
            String attribute = service.getAttribute("MechanismType");
            if ((attribute == null && z2) || (attribute != null && attribute.equals(str2))) {
                GetInstance.Instance getInstance = GetInstance.getInstance(service, null);
                TransformService transformService = (TransformService) getInstance.impl;
                transformService.algorithm = str;
                transformService.mechanism = str2;
                transformService.provider = getInstance.provider;
                return transformService;
            }
        }
        throw new NoSuchAlgorithmException(str + " algorithm and " + str2 + " mechanism not available");
    }

    public static TransformService getInstance(String str, String str2, Provider provider) throws NoSuchAlgorithmException {
        if (str2 == null || str == null || provider == null) {
            throw new NullPointerException();
        }
        boolean z2 = false;
        if (str2.equals("DOM")) {
            z2 = true;
        }
        Provider.Service service = GetInstance.getService("TransformService", str, provider);
        String attribute = service.getAttribute("MechanismType");
        if ((attribute == null && z2) || (attribute != null && attribute.equals(str2))) {
            GetInstance.Instance getInstance = GetInstance.getInstance(service, null);
            TransformService transformService = (TransformService) getInstance.impl;
            transformService.algorithm = str;
            transformService.mechanism = str2;
            transformService.provider = getInstance.provider;
            return transformService;
        }
        throw new NoSuchAlgorithmException(str + " algorithm and " + str2 + " mechanism not available");
    }

    public static TransformService getInstance(String str, String str2, String str3) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str2 == null || str == null || str3 == null) {
            throw new NullPointerException();
        }
        if (str3.length() == 0) {
            throw new NoSuchProviderException();
        }
        boolean z2 = false;
        if (str2.equals("DOM")) {
            z2 = true;
        }
        Provider.Service service = GetInstance.getService("TransformService", str, str3);
        String attribute = service.getAttribute("MechanismType");
        if ((attribute == null && z2) || (attribute != null && attribute.equals(str2))) {
            GetInstance.Instance getInstance = GetInstance.getInstance(service, null);
            TransformService transformService = (TransformService) getInstance.impl;
            transformService.algorithm = str;
            transformService.mechanism = str2;
            transformService.provider = getInstance.provider;
            return transformService;
        }
        throw new NoSuchAlgorithmException(str + " algorithm and " + str2 + " mechanism not available");
    }

    /* loaded from: rt.jar:javax/xml/crypto/dsig/TransformService$MechanismMapEntry.class */
    private static class MechanismMapEntry implements Map.Entry<String, String> {
        private final String mechanism;
        private final String algorithm;
        private final String key;

        MechanismMapEntry(String str, String str2) {
            this.algorithm = str;
            this.mechanism = str2;
            this.key = "TransformService." + str + " MechanismType";
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            if (getKey() != null ? getKey().equals(entry.getKey()) : entry.getKey() == null) {
                if (getValue() != null ? getValue().equals(entry.getValue()) : entry.getValue() == null) {
                    return true;
                }
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public String getKey() {
            return this.key;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public String getValue() {
            return this.mechanism;
        }

        @Override // java.util.Map.Entry
        public String setValue(String str) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
        }
    }

    public final String getMechanismType() {
        return this.mechanism;
    }

    @Override // javax.xml.crypto.AlgorithmMethod
    public final String getAlgorithm() {
        return this.algorithm;
    }

    public final Provider getProvider() {
        return this.provider;
    }
}
