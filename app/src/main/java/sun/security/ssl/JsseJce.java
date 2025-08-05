package sun.security.ssl;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.IOException;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.security.spec.RSAPublicKeySpec;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import sun.security.jca.ProviderList;
import sun.security.jca.Providers;
import sun.security.util.ECUtil;
import sun.security.util.SecurityConstants;

/* loaded from: jsse.jar:sun/security/ssl/JsseJce.class */
final class JsseJce {
    static final boolean ALLOW_ECC = Utilities.getBooleanProperty("com.sun.net.ssl.enableECC", true);
    private static final ProviderList fipsProviderList;
    private static final boolean kerberosAvailable;
    static final String CIPHER_RSA_PKCS1 = "RSA/ECB/PKCS1Padding";
    static final String CIPHER_RC4 = "RC4";
    static final String CIPHER_DES = "DES/CBC/NoPadding";
    static final String CIPHER_3DES = "DESede/CBC/NoPadding";
    static final String CIPHER_AES = "AES/CBC/NoPadding";
    static final String CIPHER_AES_GCM = "AES/GCM/NoPadding";
    static final String SIGNATURE_DSA = "DSA";
    static final String SIGNATURE_ECDSA = "SHA1withECDSA";
    static final String SIGNATURE_RAWDSA = "RawDSA";
    static final String SIGNATURE_RAWECDSA = "NONEwithECDSA";
    static final String SIGNATURE_RAWRSA = "NONEwithRSA";
    static final String SIGNATURE_SSLRSA = "MD5andSHA1withRSA";

    static {
        boolean z2;
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.security.ssl.JsseJce.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws Exception {
                    Class.forName("sun.security.krb5.PrincipalName", true, null);
                    return null;
                }
            });
            z2 = true;
        } catch (Exception e2) {
            z2 = false;
        }
        kerberosAvailable = z2;
        if (!SunJSSE.isFIPS()) {
            fipsProviderList = null;
            return;
        }
        Provider provider = Security.getProvider("SUN");
        if (provider == null) {
            throw new RuntimeException("FIPS mode: SUN provider must be installed");
        }
        fipsProviderList = ProviderList.newList(SunJSSE.cryptoProvider, new SunCertificates(provider));
    }

    /* loaded from: jsse.jar:sun/security/ssl/JsseJce$SunCertificates.class */
    private static final class SunCertificates extends Provider {
        private static final long serialVersionUID = -3284138292032213752L;

        SunCertificates(final Provider provider) {
            super("SunCertificates", SecurityConstants.PROVIDER_VER.doubleValue(), "SunJSSE internal");
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.security.ssl.JsseJce.SunCertificates.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    for (Map.Entry<Object, Object> entry : provider.entrySet()) {
                        String str = (String) entry.getKey();
                        if (str.startsWith("CertPathValidator.") || str.startsWith("CertPathBuilder.") || str.startsWith("CertStore.") || str.startsWith("CertificateFactory.")) {
                            SunCertificates.this.put(str, entry.getValue());
                        }
                    }
                    return null;
                }
            });
        }
    }

    private JsseJce() {
    }

    static boolean isEcAvailable() {
        return EcAvailability.isAvailable;
    }

    static boolean isKerberosAvailable() {
        return kerberosAvailable;
    }

    static Cipher getCipher(String str) throws NoSuchAlgorithmException {
        try {
            if (SunJSSE.cryptoProvider == null) {
                return Cipher.getInstance(str);
            }
            return Cipher.getInstance(str, SunJSSE.cryptoProvider);
        } catch (NoSuchPaddingException e2) {
            throw new NoSuchAlgorithmException(e2);
        }
    }

    static Signature getSignature(String str) throws NoSuchAlgorithmException {
        if (SunJSSE.cryptoProvider == null) {
            return Signature.getInstance(str);
        }
        if (str == SIGNATURE_SSLRSA && SunJSSE.cryptoProvider.getService(Constants._TAG_SIGNATURE, str) == null) {
            try {
                return Signature.getInstance(str, "SunJSSE");
            } catch (NoSuchProviderException e2) {
                throw new NoSuchAlgorithmException(e2);
            }
        }
        return Signature.getInstance(str, SunJSSE.cryptoProvider);
    }

    static KeyGenerator getKeyGenerator(String str) throws NoSuchAlgorithmException {
        if (SunJSSE.cryptoProvider == null) {
            return KeyGenerator.getInstance(str);
        }
        return KeyGenerator.getInstance(str, SunJSSE.cryptoProvider);
    }

    static KeyPairGenerator getKeyPairGenerator(String str) throws NoSuchAlgorithmException {
        if (SunJSSE.cryptoProvider == null) {
            return KeyPairGenerator.getInstance(str);
        }
        return KeyPairGenerator.getInstance(str, SunJSSE.cryptoProvider);
    }

    static KeyAgreement getKeyAgreement(String str) throws NoSuchAlgorithmException {
        if (SunJSSE.cryptoProvider == null) {
            return KeyAgreement.getInstance(str);
        }
        return KeyAgreement.getInstance(str, SunJSSE.cryptoProvider);
    }

    static Mac getMac(String str) throws NoSuchAlgorithmException {
        if (SunJSSE.cryptoProvider == null) {
            return Mac.getInstance(str);
        }
        return Mac.getInstance(str, SunJSSE.cryptoProvider);
    }

    static KeyFactory getKeyFactory(String str) throws NoSuchAlgorithmException {
        if (SunJSSE.cryptoProvider == null) {
            return KeyFactory.getInstance(str);
        }
        return KeyFactory.getInstance(str, SunJSSE.cryptoProvider);
    }

    static AlgorithmParameters getAlgorithmParameters(String str) throws NoSuchAlgorithmException {
        if (SunJSSE.cryptoProvider == null) {
            return AlgorithmParameters.getInstance(str);
        }
        return AlgorithmParameters.getInstance(str, SunJSSE.cryptoProvider);
    }

    static SecureRandom getSecureRandom() throws KeyManagementException {
        if (SunJSSE.cryptoProvider == null) {
            return new SecureRandom();
        }
        try {
            return SecureRandom.getInstance("PKCS11", SunJSSE.cryptoProvider);
        } catch (NoSuchAlgorithmException e2) {
            for (Provider.Service service : SunJSSE.cryptoProvider.getServices()) {
                if (service.getType().equals("SecureRandom")) {
                    try {
                        return SecureRandom.getInstance(service.getAlgorithm(), SunJSSE.cryptoProvider);
                    } catch (NoSuchAlgorithmException e3) {
                    }
                }
            }
            throw new KeyManagementException("FIPS mode: no SecureRandom  implementation found in provider " + SunJSSE.cryptoProvider.getName());
        }
    }

    static MessageDigest getMD5() {
        return getMessageDigest("MD5");
    }

    static MessageDigest getSHA() {
        return getMessageDigest("SHA");
    }

    static MessageDigest getMessageDigest(String str) {
        try {
            if (SunJSSE.cryptoProvider == null) {
                return MessageDigest.getInstance(str);
            }
            return MessageDigest.getInstance(str, SunJSSE.cryptoProvider);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("Algorithm " + str + " not available", e2);
        }
    }

    static int getRSAKeyLength(PublicKey publicKey) {
        BigInteger modulus;
        if (publicKey instanceof RSAPublicKey) {
            modulus = ((RSAPublicKey) publicKey).getModulus();
        } else {
            modulus = getRSAPublicKeySpec(publicKey).getModulus();
        }
        return modulus.bitLength();
    }

    static RSAPublicKeySpec getRSAPublicKeySpec(PublicKey publicKey) {
        if (publicKey instanceof RSAPublicKey) {
            RSAPublicKey rSAPublicKey = (RSAPublicKey) publicKey;
            return new RSAPublicKeySpec(rSAPublicKey.getModulus(), rSAPublicKey.getPublicExponent());
        }
        try {
            return (RSAPublicKeySpec) getKeyFactory("RSA").getKeySpec(publicKey, RSAPublicKeySpec.class);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    static ECParameterSpec getECParameterSpec(String str) {
        return ECUtil.getECParameterSpec(SunJSSE.cryptoProvider, str);
    }

    static String getNamedCurveOid(ECParameterSpec eCParameterSpec) {
        return ECUtil.getCurveName(SunJSSE.cryptoProvider, eCParameterSpec);
    }

    static ECPoint decodePoint(byte[] bArr, EllipticCurve ellipticCurve) throws IOException {
        return ECUtil.decodePoint(bArr, ellipticCurve);
    }

    static byte[] encodePoint(ECPoint eCPoint, EllipticCurve ellipticCurve) {
        return ECUtil.encodePoint(eCPoint, ellipticCurve);
    }

    static Object beginFipsProvider() {
        if (fipsProviderList == null) {
            return null;
        }
        return Providers.beginThreadProviderList(fipsProviderList);
    }

    static void endFipsProvider(Object obj) {
        if (fipsProviderList != null) {
            Providers.endThreadProviderList((ProviderList) obj);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/JsseJce$EcAvailability.class */
    private static class EcAvailability {
        private static final boolean isAvailable;

        private EcAvailability() {
        }

        static {
            boolean z2 = true;
            try {
                JsseJce.getSignature(JsseJce.SIGNATURE_ECDSA);
                JsseJce.getSignature(JsseJce.SIGNATURE_RAWECDSA);
                JsseJce.getKeyAgreement("ECDH");
                JsseJce.getKeyFactory("EC");
                JsseJce.getKeyPairGenerator("EC");
                JsseJce.getAlgorithmParameters("EC");
            } catch (Exception e2) {
                z2 = false;
            }
            isAvailable = z2;
        }
    }
}
