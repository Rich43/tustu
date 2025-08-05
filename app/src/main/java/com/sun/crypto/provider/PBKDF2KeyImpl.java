package com.sun.crypto.provider;

import java.io.ObjectStreamException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyRep;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBKDF2KeyImpl.class */
final class PBKDF2KeyImpl implements javax.crypto.interfaces.PBEKey {
    static final long serialVersionUID = -2234868909660948157L;
    private char[] passwd;
    private byte[] salt;
    private int iterCount;
    private byte[] key;
    private Mac prf;

    private static byte[] getPasswordBytes(char[] cArr) {
        ByteBuffer byteBufferEncode = Charset.forName("UTF-8").encode(CharBuffer.wrap(cArr));
        int iLimit = byteBufferEncode.limit();
        byte[] bArr = new byte[iLimit];
        byteBufferEncode.get(bArr, 0, iLimit);
        return bArr;
    }

    PBKDF2KeyImpl(PBEKeySpec pBEKeySpec, String str) throws InvalidKeySpecException {
        char[] password = pBEKeySpec.getPassword();
        if (password == null) {
            this.passwd = new char[0];
        } else {
            this.passwd = (char[]) password.clone();
        }
        byte[] passwordBytes = getPasswordBytes(this.passwd);
        if (password != null) {
            Arrays.fill(password, (char) 0);
        }
        this.salt = pBEKeySpec.getSalt();
        if (this.salt == null) {
            throw new InvalidKeySpecException("Salt not found");
        }
        this.iterCount = pBEKeySpec.getIterationCount();
        if (this.iterCount == 0) {
            throw new InvalidKeySpecException("Iteration count not found");
        }
        if (this.iterCount < 0) {
            throw new InvalidKeySpecException("Iteration count is negative");
        }
        int keyLength = pBEKeySpec.getKeyLength();
        if (keyLength == 0) {
            throw new InvalidKeySpecException("Key length not found");
        }
        if (keyLength < 0) {
            throw new InvalidKeySpecException("Key length is negative");
        }
        try {
            try {
                this.prf = Mac.getInstance(str, SunJCE.getInstance());
                this.key = deriveKey(this.prf, passwordBytes, this.salt, this.iterCount, keyLength);
                Arrays.fill(passwordBytes, (byte) 0);
            } catch (NoSuchAlgorithmException e2) {
                InvalidKeySpecException invalidKeySpecException = new InvalidKeySpecException();
                invalidKeySpecException.initCause(e2);
                throw invalidKeySpecException;
            }
        } catch (Throwable th) {
            Arrays.fill(passwordBytes, (byte) 0);
            throw th;
        }
    }

    private static byte[] deriveKey(final Mac mac, final byte[] bArr, byte[] bArr2, int i2, int i3) throws IllegalStateException {
        int i4 = i3 / 8;
        byte[] bArr3 = new byte[i4];
        try {
            int macLength = mac.getMacLength();
            int i5 = ((i4 + macLength) - 1) / macLength;
            int i6 = i4 - ((i5 - 1) * macLength);
            byte[] bArr4 = new byte[macLength];
            byte[] bArr5 = new byte[macLength];
            mac.init(new SecretKey() { // from class: com.sun.crypto.provider.PBKDF2KeyImpl.1
                private static final long serialVersionUID = 7874493593505141603L;

                @Override // java.security.Key
                public String getAlgorithm() {
                    return mac.getAlgorithm();
                }

                @Override // java.security.Key
                public String getFormat() {
                    return "RAW";
                }

                @Override // java.security.Key
                public byte[] getEncoded() {
                    return bArr;
                }

                public int hashCode() {
                    return (Arrays.hashCode(bArr) * 41) + mac.getAlgorithm().toLowerCase(Locale.ENGLISH).hashCode();
                }

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (getClass() != obj.getClass()) {
                        return false;
                    }
                    SecretKey secretKey = (SecretKey) obj;
                    return mac.getAlgorithm().equalsIgnoreCase(secretKey.getAlgorithm()) && MessageDigest.isEqual(bArr, secretKey.getEncoded());
                }
            });
            byte[] bArr6 = new byte[4];
            for (int i7 = 1; i7 <= i5; i7++) {
                mac.update(bArr2);
                bArr6[3] = (byte) i7;
                bArr6[2] = (byte) ((i7 >> 8) & 255);
                bArr6[1] = (byte) ((i7 >> 16) & 255);
                bArr6[0] = (byte) ((i7 >> 24) & 255);
                mac.update(bArr6);
                mac.doFinal(bArr4, 0);
                System.arraycopy(bArr4, 0, bArr5, 0, bArr4.length);
                for (int i8 = 2; i8 <= i2; i8++) {
                    mac.update(bArr4);
                    mac.doFinal(bArr4, 0);
                    for (int i9 = 0; i9 < bArr4.length; i9++) {
                        int i10 = i9;
                        bArr5[i10] = (byte) (bArr5[i10] ^ bArr4[i9]);
                    }
                }
                if (i7 == i5) {
                    System.arraycopy(bArr5, 0, bArr3, (i7 - 1) * macLength, i6);
                } else {
                    System.arraycopy(bArr5, 0, bArr3, (i7 - 1) * macLength, macLength);
                }
            }
            return bArr3;
        } catch (GeneralSecurityException e2) {
            throw new RuntimeException("Error deriving PBKDF2 keys");
        }
    }

    @Override // java.security.Key
    public synchronized byte[] getEncoded() {
        return (byte[]) this.key.clone();
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return "PBKDF2With" + this.prf.getAlgorithm();
    }

    @Override // javax.crypto.interfaces.PBEKey
    public int getIterationCount() {
        return this.iterCount;
    }

    @Override // javax.crypto.interfaces.PBEKey
    public synchronized char[] getPassword() {
        return (char[]) this.passwd.clone();
    }

    @Override // javax.crypto.interfaces.PBEKey
    public byte[] getSalt() {
        return (byte[]) this.salt.clone();
    }

    @Override // java.security.Key
    public String getFormat() {
        return "RAW";
    }

    public int hashCode() {
        int i2 = 0;
        for (int i3 = 1; i3 < this.key.length; i3++) {
            i2 += this.key[i3] * i3;
        }
        return i2 ^ getAlgorithm().toLowerCase(Locale.ENGLISH).hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SecretKey)) {
            return false;
        }
        SecretKey secretKey = (SecretKey) obj;
        if (!secretKey.getAlgorithm().equalsIgnoreCase(getAlgorithm()) || !secretKey.getFormat().equalsIgnoreCase("RAW")) {
            return false;
        }
        byte[] encoded = secretKey.getEncoded();
        boolean zIsEqual = MessageDigest.isEqual(this.key, encoded);
        Arrays.fill(encoded, (byte) 0);
        return zIsEqual;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.SECRET, getAlgorithm(), getFormat(), getEncoded());
    }

    protected void finalize() throws Throwable {
        try {
            synchronized (this) {
                if (this.passwd != null) {
                    Arrays.fill(this.passwd, (char) 0);
                    this.passwd = null;
                }
                if (this.key != null) {
                    Arrays.fill(this.key, (byte) 0);
                    this.key = null;
                }
            }
        } finally {
            super.finalize();
        }
    }
}
