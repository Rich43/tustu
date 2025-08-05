package javax.security.auth.kerberos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import sun.misc.HexDumpEncoder;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.util.DerValue;

/* loaded from: rt.jar:javax/security/auth/kerberos/KeyImpl.class */
class KeyImpl implements SecretKey, Destroyable, Serializable {
    private static final long serialVersionUID = -7889313790214321193L;
    private transient byte[] keyBytes;
    private transient int keyType;
    private volatile transient boolean destroyed = false;

    public KeyImpl(byte[] bArr, int i2) {
        this.keyBytes = (byte[]) bArr.clone();
        this.keyType = i2;
    }

    public KeyImpl(KerberosPrincipal kerberosPrincipal, char[] cArr, String str) {
        try {
            EncryptionKey encryptionKey = new EncryptionKey(cArr, new PrincipalName(kerberosPrincipal.getName()).getSalt(), str);
            this.keyBytes = encryptionKey.getBytes();
            this.keyType = encryptionKey.getEType();
        } catch (KrbException e2) {
            throw new IllegalArgumentException(e2.getMessage());
        }
    }

    public final int getKeyType() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return this.keyType;
    }

    @Override // java.security.Key
    public final String getAlgorithm() {
        return getAlgorithmName(this.keyType);
    }

    private String getAlgorithmName(int i2) {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        switch (i2) {
            case 0:
                return "NULL";
            case 1:
            case 3:
                return "DES";
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            case 20:
            case 21:
            case 22:
            default:
                throw new IllegalArgumentException("Unsupported encryption type: " + i2);
            case 16:
                return "DESede";
            case 17:
                return "AES128";
            case 18:
                return "AES256";
            case 23:
                return "ArcFourHmac";
        }
    }

    @Override // java.security.Key
    public final String getFormat() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return "RAW";
    }

    @Override // java.security.Key
    public final byte[] getEncoded() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return (byte[]) this.keyBytes.clone();
    }

    @Override // javax.security.auth.Destroyable
    public void destroy() throws DestroyFailedException {
        if (!this.destroyed) {
            this.destroyed = true;
            Arrays.fill(this.keyBytes, (byte) 0);
        }
    }

    @Override // javax.security.auth.Destroyable
    public boolean isDestroyed() {
        return this.destroyed;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.destroyed) {
            throw new IOException("This key is no longer valid");
        }
        try {
            objectOutputStream.writeObject(new EncryptionKey(this.keyType, this.keyBytes).asn1Encode());
        } catch (Asn1Exception e2) {
            throw new IOException(e2.getMessage());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            EncryptionKey encryptionKey = new EncryptionKey(new DerValue((byte[]) objectInputStream.readObject()));
            this.keyType = encryptionKey.getEType();
            this.keyBytes = encryptionKey.getBytes();
        } catch (Asn1Exception e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public String toString() {
        return "EncryptionKey: keyType=" + this.keyType + " keyBytes (hex dump)=" + ((this.keyBytes == null || this.keyBytes.length == 0) ? " Empty Key" : '\n' + new HexDumpEncoder().encodeBuffer(this.keyBytes) + '\n');
    }

    public int hashCode() {
        if (isDestroyed()) {
            return 17;
        }
        return (37 * ((37 * 17) + Arrays.hashCode(this.keyBytes))) + this.keyType;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KeyImpl)) {
            return false;
        }
        KeyImpl keyImpl = (KeyImpl) obj;
        if (isDestroyed() || keyImpl.isDestroyed() || this.keyType != keyImpl.getKeyType() || !Arrays.equals(this.keyBytes, keyImpl.getEncoded())) {
            return false;
        }
        return true;
    }
}
