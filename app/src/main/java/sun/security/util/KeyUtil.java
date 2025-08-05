package sun.security.util;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.interfaces.DSAKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import sun.security.jca.JCAUtil;

/* loaded from: rt.jar:sun/security/util/KeyUtil.class */
public final class KeyUtil {
    public static final int getKeySize(Key key) {
        int length = -1;
        if (key instanceof Length) {
            try {
                length = ((Length) key).length();
            } catch (UnsupportedOperationException e2) {
            }
            if (length >= 0) {
                return length;
            }
        }
        if (key instanceof SecretKey) {
            SecretKey secretKey = (SecretKey) key;
            if ("RAW".equals(secretKey.getFormat()) && secretKey.getEncoded() != null) {
                length = secretKey.getEncoded().length * 8;
            }
        } else if (key instanceof RSAKey) {
            length = ((RSAKey) key).getModulus().bitLength();
        } else if (key instanceof ECKey) {
            length = ((ECKey) key).getParams().getOrder().bitLength();
        } else if (key instanceof DSAKey) {
            DSAParams params = ((DSAKey) key).getParams();
            length = params != null ? params.getP().bitLength() : -1;
        } else if (key instanceof DHKey) {
            length = ((DHKey) key).getParams().getP().bitLength();
        }
        return length;
    }

    public static final int getKeySize(AlgorithmParameters algorithmParameters) {
        switch (algorithmParameters.getAlgorithm()) {
            case "EC":
                try {
                    ECKeySizeParameterSpec eCKeySizeParameterSpec = (ECKeySizeParameterSpec) algorithmParameters.getParameterSpec(ECKeySizeParameterSpec.class);
                    if (eCKeySizeParameterSpec != null) {
                        return eCKeySizeParameterSpec.getKeySize();
                    }
                } catch (InvalidParameterSpecException e2) {
                }
                try {
                    ECParameterSpec eCParameterSpec = (ECParameterSpec) algorithmParameters.getParameterSpec(ECParameterSpec.class);
                    if (eCParameterSpec != null) {
                        return eCParameterSpec.getOrder().bitLength();
                    }
                    return -1;
                } catch (InvalidParameterSpecException e3) {
                    return -1;
                }
            case "DiffieHellman":
                try {
                    DHParameterSpec dHParameterSpec = (DHParameterSpec) algorithmParameters.getParameterSpec(DHParameterSpec.class);
                    if (dHParameterSpec != null) {
                        return dHParameterSpec.getP().bitLength();
                    }
                    return -1;
                } catch (InvalidParameterSpecException e4) {
                    return -1;
                }
            default:
                return -1;
        }
    }

    public static final void validate(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new NullPointerException("The key to be validated cannot be null");
        }
        if (key instanceof DHPublicKey) {
            validateDHPublicKey((DHPublicKey) key);
        }
    }

    public static final void validate(KeySpec keySpec) throws InvalidKeyException {
        if (keySpec == null) {
            throw new NullPointerException("The key spec to be validated cannot be null");
        }
        if (keySpec instanceof DHPublicKeySpec) {
            validateDHPublicKey((DHPublicKeySpec) keySpec);
        }
    }

    public static final boolean isOracleJCEProvider(String str) {
        return str != null && (str.equals("SunJCE") || str.equals("SunMSCAPI") || str.equals("OracleUcrypto") || str.startsWith("SunPKCS11"));
    }

    public static byte[] checkTlsPreMasterSecretKey(int i2, int i3, SecureRandom secureRandom, byte[] bArr, boolean z2) {
        if (secureRandom == null) {
            secureRandom = JCAUtil.getSecureRandom();
        }
        byte[] bArr2 = new byte[48];
        secureRandom.nextBytes(bArr2);
        if (!z2 && bArr != null) {
            if (bArr.length != 48) {
                return bArr2;
            }
            int i4 = ((bArr[0] & 255) << 8) | (bArr[1] & 255);
            if (i2 != i4 && (i2 > 769 || i3 != i4)) {
                bArr = bArr2;
            }
            return bArr;
        }
        return bArr2;
    }

    private static void validateDHPublicKey(DHPublicKey dHPublicKey) throws InvalidKeyException {
        DHParameterSpec params = dHPublicKey.getParams();
        validateDHPublicKey(params.getP(), params.getG(), dHPublicKey.getY());
    }

    private static void validateDHPublicKey(DHPublicKeySpec dHPublicKeySpec) throws InvalidKeyException {
        validateDHPublicKey(dHPublicKeySpec.getP(), dHPublicKeySpec.getG(), dHPublicKeySpec.getY());
    }

    private static void validateDHPublicKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) throws InvalidKeyException {
        BigInteger bigInteger4 = BigInteger.ONE;
        BigInteger bigIntegerSubtract = bigInteger.subtract(BigInteger.ONE);
        if (bigInteger3.compareTo(bigInteger4) <= 0) {
            throw new InvalidKeyException("Diffie-Hellman public key is too small");
        }
        if (bigInteger3.compareTo(bigIntegerSubtract) >= 0) {
            throw new InvalidKeyException("Diffie-Hellman public key is too large");
        }
        if (bigInteger.remainder(bigInteger3).equals(BigInteger.ZERO)) {
            throw new InvalidKeyException("Invalid Diffie-Hellman parameters");
        }
    }

    public static byte[] trimZeroes(byte[] bArr) {
        int i2 = 0;
        while (i2 < bArr.length - 1 && bArr[i2] == 0) {
            i2++;
        }
        if (i2 == 0) {
            return bArr;
        }
        byte[] bArr2 = new byte[bArr.length - i2];
        System.arraycopy(bArr, i2, bArr2, 0, bArr2.length);
        return bArr2;
    }
}
