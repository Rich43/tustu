package sun.security.rsa;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PSSParameterSpec;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/rsa/RSAUtil.class */
public class RSAUtil {

    /* loaded from: rt.jar:sun/security/rsa/RSAUtil$KeyType.class */
    public enum KeyType {
        RSA("RSA"),
        PSS("RSASSA-PSS");

        private final String algo;

        KeyType(String str) {
            this.algo = str;
        }

        public String keyAlgo() {
            return this.algo;
        }

        public static KeyType lookup(String str) throws ProviderException, InvalidKeyException {
            if (str == null) {
                throw new InvalidKeyException("Null key algorithm");
            }
            for (KeyType keyType : values()) {
                if (keyType.keyAlgo().equalsIgnoreCase(str)) {
                    return keyType;
                }
            }
            throw new ProviderException("Unsupported algorithm " + str);
        }
    }

    public static void checkParamsAgainstType(KeyType keyType, AlgorithmParameterSpec algorithmParameterSpec) throws ProviderException {
        switch (keyType) {
            case RSA:
                if (algorithmParameterSpec != null) {
                    throw new ProviderException("null params expected for " + keyType.keyAlgo());
                }
                return;
            case PSS:
                if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof PSSParameterSpec)) {
                    throw new ProviderException("PSSParmeterSpec expected for " + keyType.keyAlgo());
                }
                return;
            default:
                throw new ProviderException("Unsupported RSA algorithm " + ((Object) keyType));
        }
    }

    public static AlgorithmId createAlgorithmId(KeyType keyType, AlgorithmParameterSpec algorithmParameterSpec) throws ProviderException {
        ObjectIdentifier objectIdentifier;
        AlgorithmId algorithmId;
        checkParamsAgainstType(keyType, algorithmParameterSpec);
        AlgorithmParameters algorithmParameters = null;
        try {
            switch (keyType) {
                case RSA:
                    objectIdentifier = AlgorithmId.RSAEncryption_oid;
                    break;
                case PSS:
                    if (algorithmParameterSpec != null) {
                        algorithmParameters = AlgorithmParameters.getInstance(keyType.keyAlgo());
                        algorithmParameters.init(algorithmParameterSpec);
                    }
                    objectIdentifier = AlgorithmId.RSASSA_PSS_oid;
                    break;
                default:
                    throw new ProviderException("Unsupported RSA algorithm " + ((Object) keyType));
            }
            if (algorithmParameters == null) {
                algorithmId = new AlgorithmId(objectIdentifier);
            } else {
                algorithmId = new AlgorithmId(objectIdentifier, algorithmParameters);
            }
            return algorithmId;
        } catch (NoSuchAlgorithmException | InvalidParameterSpecException e2) {
            throw new ProviderException(e2);
        }
    }

    public static AlgorithmParameterSpec getParamSpec(AlgorithmId algorithmId) throws ProviderException {
        if (algorithmId == null) {
            throw new ProviderException("AlgorithmId should not be null");
        }
        return getParamSpec(algorithmId.getParameters());
    }

    public static AlgorithmParameterSpec getParamSpec(AlgorithmParameters algorithmParameters) throws ProviderException {
        if (algorithmParameters == null) {
            return null;
        }
        try {
            String algorithm = algorithmParameters.getAlgorithm();
            KeyType keyTypeLookup = KeyType.lookup(algorithm);
            switch (keyTypeLookup) {
                case RSA:
                    throw new ProviderException("No params accepted for " + keyTypeLookup.keyAlgo());
                case PSS:
                    return algorithmParameters.getParameterSpec(PSSParameterSpec.class);
                default:
                    throw new ProviderException("Unsupported RSA algorithm: " + algorithm);
            }
        } catch (ProviderException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new ProviderException(e3);
        }
    }
}
