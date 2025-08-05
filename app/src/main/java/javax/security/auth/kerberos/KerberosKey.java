package javax.security.auth.kerberos;

import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

/* loaded from: rt.jar:javax/security/auth/kerberos/KerberosKey.class */
public class KerberosKey implements SecretKey, Destroyable {
    private static final long serialVersionUID = -4625402278148246993L;
    private KerberosPrincipal principal;
    private int versionNum;
    private KeyImpl key;
    private transient boolean destroyed = false;

    public KerberosKey(KerberosPrincipal kerberosPrincipal, byte[] bArr, int i2, int i3) {
        this.principal = kerberosPrincipal;
        this.versionNum = i3;
        this.key = new KeyImpl(bArr, i2);
    }

    public KerberosKey(KerberosPrincipal kerberosPrincipal, char[] cArr, String str) {
        this.principal = kerberosPrincipal;
        this.key = new KeyImpl(kerberosPrincipal, cArr, str);
    }

    public final KerberosPrincipal getPrincipal() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return this.principal;
    }

    public final int getVersionNumber() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return this.versionNum;
    }

    public final int getKeyType() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return this.key.getKeyType();
    }

    @Override // java.security.Key
    public final String getAlgorithm() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return this.key.getAlgorithm();
    }

    @Override // java.security.Key
    public final String getFormat() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return this.key.getFormat();
    }

    @Override // java.security.Key
    public final byte[] getEncoded() {
        if (this.destroyed) {
            throw new IllegalStateException("This key is no longer valid");
        }
        return this.key.getEncoded();
    }

    @Override // javax.security.auth.Destroyable
    public void destroy() throws DestroyFailedException {
        if (!this.destroyed) {
            this.key.destroy();
            this.principal = null;
            this.destroyed = true;
        }
    }

    @Override // javax.security.auth.Destroyable
    public boolean isDestroyed() {
        return this.destroyed;
    }

    public String toString() {
        if (this.destroyed) {
            return "Destroyed Principal";
        }
        return "Kerberos Principal " + this.principal.toString() + "Key Version " + this.versionNum + "key " + this.key.toString();
    }

    public int hashCode() {
        if (isDestroyed()) {
            return 17;
        }
        int iHashCode = (37 * ((37 * 17) + Arrays.hashCode(getEncoded()))) + getKeyType();
        if (this.principal != null) {
            iHashCode = (37 * iHashCode) + this.principal.hashCode();
        }
        return (iHashCode * 37) + this.versionNum;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KerberosKey)) {
            return false;
        }
        KerberosKey kerberosKey = (KerberosKey) obj;
        if (isDestroyed() || kerberosKey.isDestroyed() || this.versionNum != kerberosKey.getVersionNumber() || getKeyType() != kerberosKey.getKeyType() || !Arrays.equals(getEncoded(), kerberosKey.getEncoded())) {
            return false;
        }
        if (this.principal == null) {
            if (kerberosKey.getPrincipal() != null) {
                return false;
            }
            return true;
        }
        if (!this.principal.equals(kerberosKey.getPrincipal())) {
            return false;
        }
        return true;
    }
}
