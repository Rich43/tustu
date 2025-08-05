package sun.security.pkcs11;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.ProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11SecretKeyFactory.class */
final class P11SecretKeyFactory extends SecretKeyFactorySpi {
    private final Token token;
    private final String algorithm;
    private static final Map<String, Long> keyTypes = new HashMap();

    P11SecretKeyFactory(Token token, String str) {
        this.token = token;
        this.algorithm = str;
    }

    static {
        addKeyType("RC4", 18L);
        addKeyType("ARCFOUR", 18L);
        addKeyType("DES", 19L);
        addKeyType("DESede", 21L);
        addKeyType("AES", 31L);
        addKeyType("Blowfish", 32L);
        addKeyType("RC2", 17L);
        addKeyType("IDEA", 26L);
        addKeyType("TlsPremasterSecret", PKCS11Constants.PCKK_TLSPREMASTER);
        addKeyType("TlsRsaPremasterSecret", PKCS11Constants.PCKK_TLSRSAPREMASTER);
        addKeyType("TlsMasterSecret", PKCS11Constants.PCKK_TLSMASTER);
        addKeyType("Generic", 16L);
    }

    private static void addKeyType(String str, long j2) {
        Long lValueOf = Long.valueOf(j2);
        keyTypes.put(str, lValueOf);
        keyTypes.put(str.toUpperCase(Locale.ENGLISH), lValueOf);
    }

    static long getKeyType(String str) {
        Long l2 = keyTypes.get(str);
        if (l2 == null) {
            String upperCase = str.toUpperCase(Locale.ENGLISH);
            l2 = keyTypes.get(upperCase);
            if (l2 == null) {
                if (upperCase.startsWith("HMAC")) {
                    return 2147483427L;
                }
                if (upperCase.startsWith("SSLMAC")) {
                    return PKCS11Constants.PCKK_SSLMAC;
                }
            }
        }
        if (l2 != null) {
            return l2.longValue();
        }
        return -1L;
    }

    static P11Key convertKey(Token token, Key key, String str) throws InvalidKeyException {
        return convertKey(token, key, str, null);
    }

    static P11Key convertKey(Token token, Key key, String str, CK_ATTRIBUTE[] ck_attributeArr) throws InvalidKeyException {
        long keyType;
        token.ensureValid();
        if (key == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Key must be a SecretKey");
        }
        if (str == null) {
            str = key.getAlgorithm();
            keyType = getKeyType(str);
        } else {
            keyType = getKeyType(str);
            if (keyType != getKeyType(key.getAlgorithm()) && keyType != 2147483427 && keyType != PKCS11Constants.PCKK_SSLMAC) {
                throw new InvalidKeyException("Key algorithm must be " + str);
            }
        }
        if (key instanceof P11Key) {
            P11Key p11Key = (P11Key) key;
            if (p11Key.token == token) {
                if (ck_attributeArr != null) {
                    Session objSession = null;
                    long keyID = p11Key.getKeyID();
                    try {
                        try {
                            objSession = token.getObjSession();
                            P11Key p11Key2 = (P11Key) P11Key.secretKey(objSession, token.p11.C_CopyObject(objSession.id(), keyID, ck_attributeArr), p11Key.algorithm, p11Key.keyLength, ck_attributeArr);
                            p11Key.releaseKeyID();
                            token.releaseSession(objSession);
                            p11Key = p11Key2;
                        } catch (PKCS11Exception e2) {
                            throw new InvalidKeyException("Cannot duplicate the PKCS11 key", e2);
                        }
                    } catch (Throwable th) {
                        p11Key.releaseKeyID();
                        token.releaseSession(objSession);
                        throw th;
                    }
                }
                return p11Key;
            }
        }
        P11Key p11Key3 = token.secretCache.get(key);
        if (p11Key3 != null) {
            return p11Key3;
        }
        if (!"RAW".equalsIgnoreCase(key.getFormat())) {
            throw new InvalidKeyException("Encoded format must be RAW");
        }
        P11Key p11KeyCreateKey = createKey(token, key.getEncoded(), str, keyType, ck_attributeArr);
        token.secretCache.put(key, p11KeyCreateKey);
        return p11KeyCreateKey;
    }

    static void fixDESParity(byte[] bArr, int i2) {
        for (int i3 = 0; i3 < 8; i3++) {
            int i4 = bArr[i2] & 254;
            int i5 = i2;
            i2++;
            bArr[i5] = (byte) (i4 | ((Integer.bitCount(i4) & 1) ^ 1));
        }
    }

    private static P11Key createKey(Token token, byte[] bArr, String str, long j2, CK_ATTRIBUTE[] ck_attributeArr) throws InvalidKeyException {
        CK_ATTRIBUTE[] ck_attributeArr2;
        int length = bArr.length << 3;
        int iCheckKeySize = length;
        try {
            switch ((int) j2) {
                case 16:
                case 2147483429:
                case 2147483430:
                case 2147483431:
                    j2 = 16;
                    break;
                case 18:
                    iCheckKeySize = P11KeyGenerator.checkKeySize(272L, length, token);
                    break;
                case 19:
                    iCheckKeySize = P11KeyGenerator.checkKeySize(288L, length, token);
                    fixDESParity(bArr, 0);
                    break;
                case 21:
                    iCheckKeySize = P11KeyGenerator.checkKeySize(305L, length, token);
                    fixDESParity(bArr, 0);
                    fixDESParity(bArr, 8);
                    if (iCheckKeySize == 112) {
                        j2 = 20;
                        break;
                    } else {
                        j2 = 21;
                        fixDESParity(bArr, 16);
                        break;
                    }
                case 31:
                    iCheckKeySize = P11KeyGenerator.checkKeySize(PKCS11Constants.CKM_AES_KEY_GEN, length, token);
                    break;
                case 32:
                    iCheckKeySize = P11KeyGenerator.checkKeySize(PKCS11Constants.CKM_BLOWFISH_KEY_GEN, length, token);
                    break;
                case 2147483427:
                case 2147483428:
                    if (length == 0) {
                        throw new InvalidKeyException("MAC keys must not be empty");
                    }
                    j2 = 16;
                    break;
                default:
                    throw new InvalidKeyException("Unknown algorithm " + str);
            }
            Session objSession = null;
            try {
                try {
                    if (ck_attributeArr != null) {
                        ck_attributeArr2 = new CK_ATTRIBUTE[3 + ck_attributeArr.length];
                        System.arraycopy(ck_attributeArr, 0, ck_attributeArr2, 3, ck_attributeArr.length);
                    } else {
                        ck_attributeArr2 = new CK_ATTRIBUTE[3];
                    }
                    ck_attributeArr2[0] = new CK_ATTRIBUTE(0L, 4L);
                    ck_attributeArr2[1] = new CK_ATTRIBUTE(256L, j2);
                    ck_attributeArr2[2] = new CK_ATTRIBUTE(17L, bArr);
                    CK_ATTRIBUTE[] attributes = token.getAttributes("import", 4L, j2, ck_attributeArr2);
                    objSession = token.getObjSession();
                    P11Key p11Key = (P11Key) P11Key.secretKey(objSession, token.p11.C_CreateObject(objSession.id(), attributes), str, iCheckKeySize, attributes);
                    token.releaseSession(objSession);
                    return p11Key;
                } catch (PKCS11Exception e2) {
                    throw new InvalidKeyException("Could not create key", e2);
                }
            } catch (Throwable th) {
                token.releaseSession(objSession);
                throw th;
            }
        } catch (InvalidAlgorithmParameterException e3) {
            throw new InvalidKeyException("Invalid key for " + str, e3);
        } catch (ProviderException e4) {
            throw new InvalidKeyException("Could not create key", e4);
        }
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec == null) {
            throw new InvalidKeySpecException("KeySpec must not be null");
        }
        if (keySpec instanceof SecretKeySpec) {
            try {
                return (SecretKey) convertKey(this.token, (SecretKey) keySpec, this.algorithm);
            } catch (InvalidKeyException e2) {
                throw new InvalidKeySpecException(e2);
            }
        }
        if (this.algorithm.equalsIgnoreCase("DES")) {
            if (keySpec instanceof DESKeySpec) {
                return engineGenerateSecret(new SecretKeySpec(((DESKeySpec) keySpec).getKey(), "DES"));
            }
        } else if (this.algorithm.equalsIgnoreCase("DESede") && (keySpec instanceof DESedeKeySpec)) {
            return engineGenerateSecret(new SecretKeySpec(((DESedeKeySpec) keySpec).getKey(), "DESede"));
        }
        throw new InvalidKeySpecException("Unsupported spec: " + keySpec.getClass().getName());
    }

    private byte[] getKeyBytes(SecretKey secretKey) throws InvalidKeySpecException {
        try {
            SecretKey secretKeyEngineTranslateKey = engineTranslateKey(secretKey);
            if (!"RAW".equalsIgnoreCase(secretKeyEngineTranslateKey.getFormat())) {
                throw new InvalidKeySpecException("Could not obtain key bytes");
            }
            return secretKeyEngineTranslateKey.getEncoded();
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException(e2);
        }
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected KeySpec engineGetKeySpec(SecretKey secretKey, Class<?> cls) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (secretKey == null || cls == null) {
            throw new InvalidKeySpecException("key and keySpec must not be null");
        }
        if (cls.isAssignableFrom(SecretKeySpec.class)) {
            return new SecretKeySpec(getKeyBytes(secretKey), this.algorithm);
        }
        if (this.algorithm.equalsIgnoreCase("DES")) {
            try {
                if (cls.isAssignableFrom(DESKeySpec.class)) {
                    return new DESKeySpec(getKeyBytes(secretKey));
                }
            } catch (InvalidKeyException e2) {
                throw new InvalidKeySpecException(e2);
            }
        } else if (this.algorithm.equalsIgnoreCase("DESede")) {
            try {
                if (cls.isAssignableFrom(DESedeKeySpec.class)) {
                    return new DESedeKeySpec(getKeyBytes(secretKey));
                }
            } catch (InvalidKeyException e3) {
                throw new InvalidKeySpecException(e3);
            }
        }
        throw new InvalidKeySpecException("Unsupported spec: " + cls.getName());
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineTranslateKey(SecretKey secretKey) throws InvalidKeyException {
        return (SecretKey) convertKey(this.token, secretKey, this.algorithm);
    }
}
