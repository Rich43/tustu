package com.sun.crypto.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.security.KeyRep;
import java.security.MessageDigest;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEKey.class */
final class PBEKey implements SecretKey {
    static final long serialVersionUID = -2234768909660948176L;
    private byte[] key;
    private String type;

    PBEKey(PBEKeySpec pBEKeySpec, String str) throws InvalidKeySpecException {
        char[] password = pBEKeySpec.getPassword();
        password = password == null ? new char[0] : password;
        if (password.length != 1 || password[0] != 0) {
            for (int i2 = 0; i2 < password.length; i2++) {
                if (password[i2] < ' ' || password[i2] > '~') {
                    throw new InvalidKeySpecException("Password is not ASCII");
                }
            }
        }
        this.key = new byte[password.length];
        for (int i3 = 0; i3 < password.length; i3++) {
            this.key[i3] = (byte) (password[i3] & 127);
        }
        Arrays.fill(password, (char) 0);
        this.type = str;
    }

    @Override // java.security.Key
    public synchronized byte[] getEncoded() {
        return (byte[]) this.key.clone();
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return this.type;
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
        if (!secretKey.getAlgorithm().equalsIgnoreCase(this.type)) {
            return false;
        }
        byte[] encoded = secretKey.getEncoded();
        boolean zIsEqual = MessageDigest.isEqual(this.key, encoded);
        Arrays.fill(encoded, (byte) 0);
        return zIsEqual;
    }

    @Override // javax.security.auth.Destroyable
    public void destroy() {
        if (this.key != null) {
            Arrays.fill(this.key, (byte) 0);
            this.key = null;
        }
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
