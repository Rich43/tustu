package com.sun.crypto.provider;

import java.security.AlgorithmParameters;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES1Core.class */
final class PBES1Core {
    private CipherCore cipher;
    private MessageDigest md;
    private int blkSize;
    private String algo;
    private byte[] salt = null;
    private int iCount = 10;

    PBES1Core(String str) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.algo = null;
        this.algo = str;
        if (this.algo.equals("DES")) {
            this.cipher = new CipherCore(new DESCrypt(), 8);
        } else if (this.algo.equals("DESede")) {
            this.cipher = new CipherCore(new DESedeCrypt(), 8);
        } else {
            throw new NoSuchAlgorithmException("No Cipher implementation for PBEWithMD5And" + this.algo);
        }
        this.cipher.setMode("CBC");
        this.cipher.setPadding("PKCS5Padding");
        this.md = MessageDigest.getInstance("MD5");
    }

    void setMode(String str) throws NoSuchAlgorithmException {
        this.cipher.setMode(str);
    }

    void setPadding(String str) throws NoSuchPaddingException {
        this.cipher.setPadding(str);
    }

    int getBlockSize() {
        return 8;
    }

    int getOutputSize(int i2) {
        return this.cipher.getOutputSize(i2);
    }

    byte[] getIV() {
        return this.cipher.getIV();
    }

    AlgorithmParameters getParameters() {
        if (this.salt == null) {
            this.salt = new byte[8];
            SunJCE.getRandom().nextBytes(this.salt);
        }
        PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(this.salt, this.iCount);
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("PBEWithMD5And" + (this.algo.equalsIgnoreCase("DES") ? "DES" : "TripleDES"), SunJCE.getInstance());
            algorithmParameters.init(pBEParameterSpec);
            return algorithmParameters;
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("SunJCE called, but not configured");
        } catch (InvalidParameterSpecException e3) {
            throw new RuntimeException("PBEParameterSpec not supported");
        }
    }

    void init(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if ((i2 == 2 || i2 == 4) && algorithmParameterSpec == null) {
            throw new InvalidAlgorithmParameterException("Parameters missing");
        }
        if (key == null) {
            throw new InvalidKeyException("Null key");
        }
        byte[] encoded = key.getEncoded();
        if (encoded != null) {
            try {
                if (key.getAlgorithm().regionMatches(true, 0, "PBE", 0, 3)) {
                    if (algorithmParameterSpec == null) {
                        this.salt = new byte[8];
                        secureRandom.nextBytes(this.salt);
                    } else {
                        if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                            throw new InvalidAlgorithmParameterException("Wrong parameter type: PBE expected");
                        }
                        this.salt = ((PBEParameterSpec) algorithmParameterSpec).getSalt();
                        if (this.salt.length != 8) {
                            throw new InvalidAlgorithmParameterException("Salt must be 8 bytes long");
                        }
                        this.iCount = ((PBEParameterSpec) algorithmParameterSpec).getIterationCount();
                        if (this.iCount <= 0) {
                            throw new InvalidAlgorithmParameterException("IterationCount must be a positive number");
                        }
                    }
                    byte[] bArrDeriveCipherKey = deriveCipherKey(encoded);
                    if (encoded != null) {
                        Arrays.fill(encoded, (byte) 0);
                    }
                    this.cipher.init(i2, new SecretKeySpec(bArrDeriveCipherKey, 0, bArrDeriveCipherKey.length - 8, this.algo), new IvParameterSpec(bArrDeriveCipherKey, bArrDeriveCipherKey.length - 8, 8), secureRandom);
                    return;
                }
            } catch (Throwable th) {
                if (encoded != null) {
                    Arrays.fill(encoded, (byte) 0);
                }
                throw th;
            }
        }
        throw new InvalidKeyException("Missing password");
    }

    private byte[] deriveCipherKey(byte[] bArr) {
        byte[] bArr2 = null;
        if (this.algo.equals("DES")) {
            this.md.update(bArr);
            this.md.update(this.salt);
            byte[] bArrDigest = this.md.digest();
            for (int i2 = 1; i2 < this.iCount; i2++) {
                this.md.update(bArrDigest);
                try {
                    this.md.digest(bArrDigest, 0, bArrDigest.length);
                } catch (DigestException e2) {
                    throw new ProviderException("Internal error", e2);
                }
            }
            bArr2 = bArrDigest;
        } else if (this.algo.equals("DESede")) {
            int i3 = 0;
            while (i3 < 4 && this.salt[i3] == this.salt[i3 + 4]) {
                i3++;
            }
            if (i3 == 4) {
                for (int i4 = 0; i4 < 2; i4++) {
                    byte b2 = this.salt[i4];
                    this.salt[i4] = this.salt[3 - i4];
                    this.salt[3 - i4] = b2;
                }
            }
            bArr2 = new byte[32];
            for (int i5 = 0; i5 < 2; i5++) {
                this.md.update(this.salt, i5 * (this.salt.length / 2), this.salt.length / 2);
                this.md.update(bArr);
                byte[] bArrDigest2 = this.md.digest();
                for (int i6 = 1; i6 < this.iCount; i6++) {
                    this.md.update(bArrDigest2);
                    this.md.update(bArr);
                    try {
                        this.md.digest(bArrDigest2, 0, bArrDigest2.length);
                    } catch (DigestException e3) {
                        throw new ProviderException("Internal error", e3);
                    }
                }
                System.arraycopy(bArrDigest2, 0, bArr2, i5 * 16, bArrDigest2.length);
            }
        }
        this.md.reset();
        return bArr2;
    }

    void init(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        PBEParameterSpec pBEParameterSpec = null;
        if (algorithmParameters != null) {
            try {
                pBEParameterSpec = (PBEParameterSpec) algorithmParameters.getParameterSpec(PBEParameterSpec.class);
            } catch (InvalidParameterSpecException e2) {
                throw new InvalidAlgorithmParameterException("Wrong parameter type: PBE expected");
            }
        }
        init(i2, key, pBEParameterSpec, secureRandom);
    }

    byte[] update(byte[] bArr, int i2, int i3) {
        return this.cipher.update(bArr, i2, i3);
    }

    int update(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        return this.cipher.update(bArr, i2, i3, bArr2, i4);
    }

    byte[] doFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        return this.cipher.doFinal(bArr, i2, i3);
    }

    int doFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        return this.cipher.doFinal(bArr, i2, i3, bArr2, i4);
    }

    byte[] wrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] encoded;
        byte[] bArrDoFinal = null;
        try {
            encoded = key.getEncoded();
        } catch (BadPaddingException e2) {
            if (0 != 0) {
                Arrays.fill((byte[]) null, (byte) 0);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                Arrays.fill((byte[]) null, (byte) 0);
            }
            throw th;
        }
        if (encoded == null || encoded.length == 0) {
            throw new InvalidKeyException("Cannot get an encoding of the key to be wrapped");
        }
        bArrDoFinal = doFinal(encoded, 0, encoded.length);
        if (encoded != null) {
            Arrays.fill(encoded, (byte) 0);
        }
        return bArrDoFinal;
    }

    Key unwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            return ConstructKeys.constructKey(doFinal(bArr, 0, bArr.length), str, i2);
        } catch (BadPaddingException e2) {
            throw new InvalidKeyException("The wrapped key is not padded correctly");
        } catch (IllegalBlockSizeException e3) {
            throw new InvalidKeyException("The wrapped key does not have the correct length");
        }
    }
}
