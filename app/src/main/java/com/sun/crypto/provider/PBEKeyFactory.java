package com.sun.crypto.provider;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.PBEKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory.class */
abstract class PBEKeyFactory extends SecretKeyFactorySpi {
    private String type;
    private static HashSet<String> validTypes = new HashSet<>(17);

    private PBEKeyFactory(String str) {
        this.type = str;
    }

    static {
        validTypes.add("PBEWithMD5AndDES".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithSHA1AndDESede".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithSHA1AndRC2_40".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithSHA1AndRC2_128".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithSHA1AndRC4_40".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithSHA1AndRC4_128".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithMD5AndTripleDES".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA1AndAES_128".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA224AndAES_128".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA256AndAES_128".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA384AndAES_128".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA512AndAES_128".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA1AndAES_256".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA224AndAES_256".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA256AndAES_256".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA384AndAES_256".toUpperCase(Locale.ENGLISH));
        validTypes.add("PBEWithHmacSHA512AndAES_256".toUpperCase(Locale.ENGLISH));
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithMD5AndDES.class */
    public static final class PBEWithMD5AndDES extends PBEKeyFactory {
        public PBEWithMD5AndDES() {
            super("PBEWithMD5AndDES");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithSHA1AndDESede.class */
    public static final class PBEWithSHA1AndDESede extends PBEKeyFactory {
        public PBEWithSHA1AndDESede() {
            super("PBEWithSHA1AndDESede");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithSHA1AndRC2_40.class */
    public static final class PBEWithSHA1AndRC2_40 extends PBEKeyFactory {
        public PBEWithSHA1AndRC2_40() {
            super("PBEWithSHA1AndRC2_40");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithSHA1AndRC2_128.class */
    public static final class PBEWithSHA1AndRC2_128 extends PBEKeyFactory {
        public PBEWithSHA1AndRC2_128() {
            super("PBEWithSHA1AndRC2_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithSHA1AndRC4_40.class */
    public static final class PBEWithSHA1AndRC4_40 extends PBEKeyFactory {
        public PBEWithSHA1AndRC4_40() {
            super("PBEWithSHA1AndRC4_40");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithSHA1AndRC4_128.class */
    public static final class PBEWithSHA1AndRC4_128 extends PBEKeyFactory {
        public PBEWithSHA1AndRC4_128() {
            super("PBEWithSHA1AndRC4_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithMD5AndTripleDES.class */
    public static final class PBEWithMD5AndTripleDES extends PBEKeyFactory {
        public PBEWithMD5AndTripleDES() {
            super("PBEWithMD5AndTripleDES");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA1AndAES_128.class */
    public static final class PBEWithHmacSHA1AndAES_128 extends PBEKeyFactory {
        public PBEWithHmacSHA1AndAES_128() {
            super("PBEWithHmacSHA1AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA224AndAES_128.class */
    public static final class PBEWithHmacSHA224AndAES_128 extends PBEKeyFactory {
        public PBEWithHmacSHA224AndAES_128() {
            super("PBEWithHmacSHA224AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA256AndAES_128.class */
    public static final class PBEWithHmacSHA256AndAES_128 extends PBEKeyFactory {
        public PBEWithHmacSHA256AndAES_128() {
            super("PBEWithHmacSHA256AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA384AndAES_128.class */
    public static final class PBEWithHmacSHA384AndAES_128 extends PBEKeyFactory {
        public PBEWithHmacSHA384AndAES_128() {
            super("PBEWithHmacSHA384AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA512AndAES_128.class */
    public static final class PBEWithHmacSHA512AndAES_128 extends PBEKeyFactory {
        public PBEWithHmacSHA512AndAES_128() {
            super("PBEWithHmacSHA512AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA1AndAES_256.class */
    public static final class PBEWithHmacSHA1AndAES_256 extends PBEKeyFactory {
        public PBEWithHmacSHA1AndAES_256() {
            super("PBEWithHmacSHA1AndAES_256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA224AndAES_256.class */
    public static final class PBEWithHmacSHA224AndAES_256 extends PBEKeyFactory {
        public PBEWithHmacSHA224AndAES_256() {
            super("PBEWithHmacSHA224AndAES_256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA256AndAES_256.class */
    public static final class PBEWithHmacSHA256AndAES_256 extends PBEKeyFactory {
        public PBEWithHmacSHA256AndAES_256() {
            super("PBEWithHmacSHA256AndAES_256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA384AndAES_256.class */
    public static final class PBEWithHmacSHA384AndAES_256 extends PBEKeyFactory {
        public PBEWithHmacSHA384AndAES_256() {
            super("PBEWithHmacSHA384AndAES_256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKeyFactory$PBEWithHmacSHA512AndAES_256.class */
    public static final class PBEWithHmacSHA512AndAES_256 extends PBEKeyFactory {
        public PBEWithHmacSHA512AndAES_256() {
            super("PBEWithHmacSHA512AndAES_256");
        }
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        if (!(keySpec instanceof PBEKeySpec)) {
            throw new InvalidKeySpecException("Invalid key spec");
        }
        return new PBEKey((PBEKeySpec) keySpec, this.type);
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected KeySpec engineGetKeySpec(SecretKey secretKey, Class<?> cls) throws InvalidKeySpecException {
        if ((secretKey instanceof SecretKey) && validTypes.contains(secretKey.getAlgorithm().toUpperCase(Locale.ENGLISH)) && secretKey.getFormat().equalsIgnoreCase("RAW")) {
            if (cls != null && PBEKeySpec.class.isAssignableFrom(cls)) {
                byte[] encoded = secretKey.getEncoded();
                char[] cArr = new char[encoded.length];
                for (int i2 = 0; i2 < cArr.length; i2++) {
                    cArr[i2] = (char) (encoded[i2] & Byte.MAX_VALUE);
                }
                PBEKeySpec pBEKeySpec = new PBEKeySpec(cArr);
                Arrays.fill(cArr, ' ');
                Arrays.fill(encoded, (byte) 0);
                return pBEKeySpec;
            }
            throw new InvalidKeySpecException("Invalid key spec");
        }
        throw new InvalidKeySpecException("Invalid key format/algorithm");
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineTranslateKey(SecretKey secretKey) throws InvalidKeyException {
        if (secretKey != null) {
            try {
                if (validTypes.contains(secretKey.getAlgorithm().toUpperCase(Locale.ENGLISH)) && secretKey.getFormat().equalsIgnoreCase("RAW")) {
                    if (secretKey instanceof PBEKey) {
                        return secretKey;
                    }
                    return engineGenerateSecret((PBEKeySpec) engineGetKeySpec(secretKey, PBEKeySpec.class));
                }
            } catch (InvalidKeySpecException e2) {
                throw new InvalidKeyException("Cannot translate key: " + e2.getMessage());
            }
        }
        throw new InvalidKeyException("Invalid key format/algorithm");
    }
}
