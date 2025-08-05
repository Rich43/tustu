package com.sun.crypto.provider;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.SealedObject;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import sun.security.util.ObjectIdentifier;
import sun.security.util.SecurityProperties;
import sun.security.x509.AlgorithmId;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyProtector.class */
final class KeyProtector {
    private static final String PBE_WITH_MD5_AND_DES3_CBC_OID = "1.3.6.1.4.1.42.2.19.1";
    private static final String KEY_PROTECTOR_OID = "1.3.6.1.4.1.42.2.17.1.1";
    private static final int MAX_ITERATION_COUNT = 5000000;
    private static final int MIN_ITERATION_COUNT = 10000;
    private static final int DEFAULT_ITERATION_COUNT = 200000;
    private static final int SALT_LEN = 20;
    private static final int DIGEST_LEN = 20;
    private static final int ITERATION_COUNT;
    private char[] password;

    static {
        int i2 = DEFAULT_ITERATION_COUNT;
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("jdk.jceks.iterationCount");
        if (strPrivilegedGetOverridable != null && !strPrivilegedGetOverridable.isEmpty()) {
            try {
                i2 = Integer.parseInt(strPrivilegedGetOverridable);
                if (i2 < 10000 || i2 > MAX_ITERATION_COUNT) {
                    i2 = DEFAULT_ITERATION_COUNT;
                }
            } catch (NumberFormatException e2) {
            }
        }
        ITERATION_COUNT = i2;
    }

    KeyProtector(char[] cArr) {
        if (cArr == null) {
            throw new IllegalArgumentException("password can't be null");
        }
        this.password = cArr;
    }

    byte[] protect(PrivateKey privateKey) throws Exception {
        byte[] bArr = new byte[8];
        SunJCE.getRandom().nextBytes(bArr);
        PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(bArr, ITERATION_COUNT);
        PBEKeySpec pBEKeySpec = new PBEKeySpec(this.password);
        PBEKey pBEKey = null;
        try {
            pBEKey = new PBEKey(pBEKeySpec, "PBEWithMD5AndTripleDES");
            PBEWithMD5AndTripleDESCipher pBEWithMD5AndTripleDESCipher = new PBEWithMD5AndTripleDESCipher();
            pBEWithMD5AndTripleDESCipher.engineInit(1, pBEKey, pBEParameterSpec, (SecureRandom) null);
            pBEKeySpec.clearPassword();
            if (pBEKey != null) {
                pBEKey.destroy();
            }
            byte[] encoded = privateKey.getEncoded();
            byte[] bArrEngineDoFinal = pBEWithMD5AndTripleDESCipher.engineDoFinal(encoded, 0, encoded.length);
            Arrays.fill(encoded, (byte) 0);
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("PBE", SunJCE.getInstance());
            algorithmParameters.init(pBEParameterSpec);
            return new EncryptedPrivateKeyInfo(new AlgorithmId(new ObjectIdentifier(PBE_WITH_MD5_AND_DES3_CBC_OID), algorithmParameters), bArrEngineDoFinal).getEncoded();
        } catch (Throwable th) {
            pBEKeySpec.clearPassword();
            if (pBEKey != null) {
                pBEKey.destroy();
            }
            throw th;
        }
    }

    Key recover(EncryptedPrivateKeyInfo encryptedPrivateKeyInfo) throws UnrecoverableKeyException, NoSuchAlgorithmException {
        byte[] bArrEngineDoFinal;
        PBEKey pBEKey = null;
        try {
            try {
                try {
                    String string = encryptedPrivateKeyInfo.getAlgorithm().getOID().toString();
                    if (!string.equals(PBE_WITH_MD5_AND_DES3_CBC_OID) && !string.equals(KEY_PROTECTOR_OID)) {
                        throw new UnrecoverableKeyException("Unsupported encryption algorithm");
                    }
                    if (string.equals(KEY_PROTECTOR_OID)) {
                        bArrEngineDoFinal = recover(encryptedPrivateKeyInfo.getEncryptedData());
                    } else {
                        byte[] encodedParams = encryptedPrivateKeyInfo.getAlgorithm().getEncodedParams();
                        if (encodedParams == null) {
                            throw new IOException("Missing PBE parameters");
                        }
                        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("PBE");
                        algorithmParameters.init(encodedParams);
                        PBEParameterSpec pBEParameterSpec = (PBEParameterSpec) algorithmParameters.getParameterSpec(PBEParameterSpec.class);
                        if (pBEParameterSpec.getIterationCount() > MAX_ITERATION_COUNT) {
                            throw new IOException("PBE iteration count too large");
                        }
                        PBEKeySpec pBEKeySpec = new PBEKeySpec(this.password);
                        pBEKey = new PBEKey(pBEKeySpec, "PBEWithMD5AndTripleDES");
                        pBEKeySpec.clearPassword();
                        PBEWithMD5AndTripleDESCipher pBEWithMD5AndTripleDESCipher = new PBEWithMD5AndTripleDESCipher();
                        pBEWithMD5AndTripleDESCipher.engineInit(2, pBEKey, pBEParameterSpec, (SecureRandom) null);
                        bArrEngineDoFinal = pBEWithMD5AndTripleDESCipher.engineDoFinal(encryptedPrivateKeyInfo.getEncryptedData(), 0, encryptedPrivateKeyInfo.getEncryptedData().length);
                    }
                    PrivateKey privateKeyGeneratePrivate = KeyFactory.getInstance(new AlgorithmId(new PrivateKeyInfo(bArrEngineDoFinal).getAlgorithm().getOID()).getName()).generatePrivate(new PKCS8EncodedKeySpec(bArrEngineDoFinal));
                    if (bArrEngineDoFinal != null) {
                        Arrays.fill(bArrEngineDoFinal, (byte) 0);
                    }
                    if (pBEKey != null) {
                        try {
                            pBEKey.destroy();
                        } catch (DestroyFailedException e2) {
                        }
                    }
                    return privateKeyGeneratePrivate;
                } catch (NoSuchAlgorithmException e3) {
                    throw e3;
                } catch (GeneralSecurityException e4) {
                    throw new UnrecoverableKeyException(e4.getMessage());
                }
            } catch (IOException e5) {
                throw new UnrecoverableKeyException(e5.getMessage());
            }
        } catch (Throwable th) {
            if (0 != 0) {
                Arrays.fill((byte[]) null, (byte) 0);
            }
            if (0 != 0) {
                try {
                    pBEKey.destroy();
                } catch (DestroyFailedException e6) {
                }
            }
            throw th;
        }
    }

    private byte[] recover(byte[] bArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        byte[] bArr2 = new byte[20];
        System.arraycopy(bArr, 0, bArr2, 0, 20);
        int length = (bArr.length - 20) - 20;
        int i2 = length / 20;
        if (length % 20 != 0) {
            i2++;
        }
        byte[] bArr3 = new byte[length];
        System.arraycopy(bArr, 20, bArr3, 0, length);
        byte[] bArr4 = new byte[bArr3.length];
        byte[] bArr5 = new byte[this.password.length * 2];
        int i3 = 0;
        for (int i4 = 0; i4 < this.password.length; i4++) {
            int i5 = i3;
            int i6 = i3 + 1;
            bArr5[i5] = (byte) (this.password[i4] >> '\b');
            i3 = i6 + 1;
            bArr5[i6] = (byte) this.password[i4];
        }
        int i7 = 0;
        int i8 = 0;
        byte[] bArrDigest = bArr2;
        while (i7 < i2) {
            messageDigest.update(bArr5);
            messageDigest.update(bArrDigest);
            bArrDigest = messageDigest.digest();
            messageDigest.reset();
            if (i7 < i2 - 1) {
                System.arraycopy(bArrDigest, 0, bArr4, i8, bArrDigest.length);
            } else {
                System.arraycopy(bArrDigest, 0, bArr4, i8, bArr4.length - i8);
            }
            i7++;
            i8 += 20;
        }
        byte[] bArr6 = new byte[bArr3.length];
        for (int i9 = 0; i9 < bArr6.length; i9++) {
            bArr6[i9] = (byte) (bArr3[i9] ^ bArr4[i9]);
        }
        messageDigest.update(bArr5);
        Arrays.fill(bArr5, (byte) 0);
        messageDigest.update(bArr6);
        byte[] bArrDigest2 = messageDigest.digest();
        messageDigest.reset();
        for (int i10 = 0; i10 < bArrDigest2.length; i10++) {
            if (bArrDigest2[i10] != bArr[20 + length + i10]) {
                throw new UnrecoverableKeyException("Cannot recover key");
            }
        }
        return bArr6;
    }

    SealedObject seal(Key key) throws Exception {
        byte[] bArr = new byte[8];
        SunJCE.getRandom().nextBytes(bArr);
        PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(bArr, ITERATION_COUNT);
        PBEKeySpec pBEKeySpec = new PBEKeySpec(this.password);
        PBEKey pBEKey = null;
        try {
            pBEKey = new PBEKey(pBEKeySpec, "PBEWithMD5AndTripleDES");
            pBEKeySpec.clearPassword();
            CipherForKeyProtector cipherForKeyProtector = new CipherForKeyProtector(new PBEWithMD5AndTripleDESCipher(), SunJCE.getInstance(), "PBEWithMD5AndTripleDES");
            cipherForKeyProtector.init(1, pBEKey, pBEParameterSpec);
            if (pBEKey != null) {
                pBEKey.destroy();
            }
            return new SealedObjectForKeyProtector(key, cipherForKeyProtector);
        } catch (Throwable th) {
            if (pBEKey != null) {
                pBEKey.destroy();
            }
            throw th;
        }
    }

    Key unseal(SealedObject sealedObject, int i2) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        SealedObjectForKeyProtector sealedObjectForKeyProtector;
        Destroyable destroyable = null;
        try {
            try {
                try {
                    try {
                        PBEKeySpec pBEKeySpec = new PBEKeySpec(this.password);
                        PBEKey pBEKey = new PBEKey(pBEKeySpec, "PBEWithMD5AndTripleDES");
                        pBEKeySpec.clearPassword();
                        if (!(sealedObject instanceof SealedObjectForKeyProtector)) {
                            sealedObjectForKeyProtector = new SealedObjectForKeyProtector(sealedObject);
                        } else {
                            sealedObjectForKeyProtector = (SealedObjectForKeyProtector) sealedObject;
                        }
                        AlgorithmParameters parameters = sealedObjectForKeyProtector.getParameters();
                        if (parameters == null) {
                            throw new UnrecoverableKeyException("Cannot get algorithm parameters");
                        }
                        try {
                            if (((PBEParameterSpec) parameters.getParameterSpec(PBEParameterSpec.class)).getIterationCount() > MAX_ITERATION_COUNT) {
                                throw new IOException("PBE iteration count too large");
                            }
                            CipherForKeyProtector cipherForKeyProtector = new CipherForKeyProtector(new PBEWithMD5AndTripleDESCipher(), SunJCE.getInstance(), "PBEWithMD5AndTripleDES");
                            cipherForKeyProtector.init(2, pBEKey, parameters);
                            Key key = sealedObjectForKeyProtector.getKey(cipherForKeyProtector, i2);
                            if (pBEKey != null) {
                                try {
                                    pBEKey.destroy();
                                } catch (DestroyFailedException e2) {
                                }
                            }
                            return key;
                        } catch (InvalidParameterSpecException e3) {
                            throw new IOException("Invalid PBE algorithm parameters");
                        }
                    } catch (NoSuchAlgorithmException e4) {
                        throw e4;
                    } catch (GeneralSecurityException e5) {
                        throw new UnrecoverableKeyException(e5.getMessage());
                    }
                } catch (ClassNotFoundException e6) {
                    throw new UnrecoverableKeyException(e6.getMessage());
                }
            } catch (IOException e7) {
                throw new UnrecoverableKeyException(e7.getMessage());
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    destroyable.destroy();
                } catch (DestroyFailedException e8) {
                }
            }
            throw th;
        }
    }
}
