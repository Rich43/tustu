package javax.crypto.spec;

import java.security.spec.KeySpec;
import java.util.Arrays;

/* loaded from: jce.jar:javax/crypto/spec/PBEKeySpec.class */
public class PBEKeySpec implements KeySpec {
    private char[] password;
    private byte[] salt;
    private int iterationCount;
    private int keyLength;

    public PBEKeySpec(char[] cArr) {
        this.salt = null;
        this.iterationCount = 0;
        this.keyLength = 0;
        if (cArr == null || cArr.length == 0) {
            this.password = new char[0];
        } else {
            this.password = (char[]) cArr.clone();
        }
    }

    public PBEKeySpec(char[] cArr, byte[] bArr, int i2, int i3) {
        this.salt = null;
        this.iterationCount = 0;
        this.keyLength = 0;
        if (cArr == null || cArr.length == 0) {
            this.password = new char[0];
        } else {
            this.password = (char[]) cArr.clone();
        }
        if (bArr == null) {
            throw new NullPointerException("the salt parameter must be non-null");
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("the salt parameter must not be empty");
        }
        this.salt = (byte[]) bArr.clone();
        if (i2 <= 0) {
            throw new IllegalArgumentException("invalid iterationCount value");
        }
        if (i3 <= 0) {
            throw new IllegalArgumentException("invalid keyLength value");
        }
        this.iterationCount = i2;
        this.keyLength = i3;
    }

    public PBEKeySpec(char[] cArr, byte[] bArr, int i2) {
        this.salt = null;
        this.iterationCount = 0;
        this.keyLength = 0;
        if (cArr == null || cArr.length == 0) {
            this.password = new char[0];
        } else {
            this.password = (char[]) cArr.clone();
        }
        if (bArr == null) {
            throw new NullPointerException("the salt parameter must be non-null");
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("the salt parameter must not be empty");
        }
        this.salt = (byte[]) bArr.clone();
        if (i2 <= 0) {
            throw new IllegalArgumentException("invalid iterationCount value");
        }
        this.iterationCount = i2;
    }

    public final void clearPassword() {
        if (this.password != null) {
            Arrays.fill(this.password, ' ');
            this.password = null;
        }
    }

    public final char[] getPassword() {
        if (this.password == null) {
            throw new IllegalStateException("password has been cleared");
        }
        return (char[]) this.password.clone();
    }

    public final byte[] getSalt() {
        if (this.salt != null) {
            return (byte[]) this.salt.clone();
        }
        return null;
    }

    public final int getIterationCount() {
        return this.iterationCount;
    }

    public final int getKeyLength() {
        return this.keyLength;
    }
}
