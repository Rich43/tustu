package com.sun.crypto.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.security.InvalidKeyException;
import java.security.KeyRep;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.SecretKey;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESedeKey.class */
final class DESedeKey implements SecretKey {
    static final long serialVersionUID = 2463986565756745178L;
    private byte[] key;

    DESedeKey(byte[] bArr) throws InvalidKeyException {
        this(bArr, 0);
    }

    DESedeKey(byte[] bArr, int i2) throws InvalidKeyException {
        if (bArr == null || bArr.length - i2 < 24) {
            throw new InvalidKeyException("Wrong key size");
        }
        this.key = new byte[24];
        System.arraycopy(bArr, i2, this.key, 0, 24);
        DESKeyGenerator.setParityBit(this.key, 0);
        DESKeyGenerator.setParityBit(this.key, 8);
        DESKeyGenerator.setParityBit(this.key, 16);
    }

    @Override // java.security.Key
    public synchronized byte[] getEncoded() {
        return (byte[]) this.key.clone();
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return "DESede";
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
        return i2 ^ "desede".hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SecretKey)) {
            return false;
        }
        String algorithm = ((SecretKey) obj).getAlgorithm();
        if (!algorithm.equalsIgnoreCase("DESede") && !algorithm.equalsIgnoreCase("TripleDES")) {
            return false;
        }
        byte[] encoded = ((SecretKey) obj).getEncoded();
        boolean zIsEqual = MessageDigest.isEqual(this.key, encoded);
        Arrays.fill(encoded, (byte) 0);
        return zIsEqual;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.key = (byte[]) this.key.clone();
    }

    private Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.SECRET, getAlgorithm(), getFormat(), getEncoded());
    }

    protected void finalize() throws Throwable {
        try {
            synchronized (this) {
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
