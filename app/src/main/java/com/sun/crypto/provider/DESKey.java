package com.sun.crypto.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.security.InvalidKeyException;
import java.security.KeyRep;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.SecretKey;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESKey.class */
final class DESKey implements SecretKey {
    static final long serialVersionUID = 7724971015953279128L;
    private byte[] key;

    DESKey(byte[] bArr) throws InvalidKeyException {
        this(bArr, 0);
    }

    DESKey(byte[] bArr, int i2) throws InvalidKeyException {
        if (bArr == null || bArr.length - i2 < 8) {
            throw new InvalidKeyException("Wrong key size");
        }
        this.key = new byte[8];
        System.arraycopy(bArr, i2, this.key, 0, 8);
        DESKeyGenerator.setParityBit(this.key, 0);
    }

    @Override // java.security.Key
    public synchronized byte[] getEncoded() {
        return (byte[]) this.key.clone();
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return "DES";
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
        return i2 ^ "des".hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SecretKey) || !((SecretKey) obj).getAlgorithm().equalsIgnoreCase("DES")) {
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
