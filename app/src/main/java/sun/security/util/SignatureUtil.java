package sun.security.util;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.util.Locale;
import sun.misc.SharedSecrets;
import sun.security.rsa.RSAUtil;

/* loaded from: rt.jar:sun/security/util/SignatureUtil.class */
public class SignatureUtil {
    private static String checkName(String str) throws ProviderException {
        if (str.indexOf(".") == -1) {
            return str;
        }
        try {
            return Signature.getInstance(str).getAlgorithm();
        } catch (Exception e2) {
            throw new ProviderException("Error mapping algorithm name", e2);
        }
    }

    private static AlgorithmParameters createAlgorithmParameters(String str, byte[] bArr) throws ProviderException {
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(checkName(str));
            algorithmParameters.init(bArr);
            return algorithmParameters;
        } catch (IOException | NoSuchAlgorithmException e2) {
            throw new ProviderException(e2);
        }
    }

    public static AlgorithmParameterSpec getParamSpec(String str, AlgorithmParameters algorithmParameters) throws ProviderException {
        String upperCase = checkName(str).toUpperCase(Locale.ENGLISH);
        AlgorithmParameterSpec parameterSpec = null;
        if (algorithmParameters != null) {
            if (algorithmParameters.getAlgorithm().indexOf(".") != -1) {
                try {
                    algorithmParameters = createAlgorithmParameters(upperCase, algorithmParameters.getEncoded());
                } catch (IOException e2) {
                    throw new ProviderException(e2);
                }
            }
            if (upperCase.indexOf("RSA") != -1) {
                parameterSpec = RSAUtil.getParamSpec(algorithmParameters);
            } else if (upperCase.indexOf("ECDSA") != -1) {
                try {
                    parameterSpec = algorithmParameters.getParameterSpec(ECParameterSpec.class);
                } catch (Exception e3) {
                    throw new ProviderException("Error handling EC parameters", e3);
                }
            } else {
                throw new ProviderException("Unrecognized algorithm for signature parameters " + upperCase);
            }
        }
        return parameterSpec;
    }

    public static AlgorithmParameterSpec getParamSpec(String str, byte[] bArr) throws ProviderException {
        String upperCase = checkName(str).toUpperCase(Locale.ENGLISH);
        AlgorithmParameterSpec eCParameterSpec = null;
        if (bArr != null) {
            if (upperCase.indexOf("RSA") != -1) {
                eCParameterSpec = RSAUtil.getParamSpec(createAlgorithmParameters(upperCase, bArr));
            } else if (upperCase.indexOf("ECDSA") != -1) {
                try {
                    eCParameterSpec = ECUtil.getECParameterSpec(Signature.getInstance(upperCase).getProvider(), bArr);
                    if (eCParameterSpec == null) {
                        throw new ProviderException("Error handling EC parameters");
                    }
                } catch (Exception e2) {
                    throw new ProviderException("Error handling EC parameters", e2);
                }
            } else {
                throw new ProviderException("Unrecognized algorithm for signature parameters " + upperCase);
            }
        }
        return eCParameterSpec;
    }

    public static void initVerifyWithParam(Signature signature, PublicKey publicKey, AlgorithmParameterSpec algorithmParameterSpec) throws ProviderException, InvalidKeyException, InvalidAlgorithmParameterException {
        SharedSecrets.getJavaSecuritySignatureAccess().initVerify(signature, publicKey, algorithmParameterSpec);
    }

    public static void initVerifyWithParam(Signature signature, Certificate certificate, AlgorithmParameterSpec algorithmParameterSpec) throws ProviderException, InvalidKeyException, InvalidAlgorithmParameterException {
        SharedSecrets.getJavaSecuritySignatureAccess().initVerify(signature, certificate, algorithmParameterSpec);
    }

    public static void initSignWithParam(Signature signature, PrivateKey privateKey, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws ProviderException, InvalidKeyException, InvalidAlgorithmParameterException {
        SharedSecrets.getJavaSecuritySignatureAccess().initSign(signature, privateKey, algorithmParameterSpec, secureRandom);
    }
}
