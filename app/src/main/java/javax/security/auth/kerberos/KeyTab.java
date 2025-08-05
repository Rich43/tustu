package javax.security.auth.kerberos;

import java.io.File;
import java.security.AccessControlException;
import java.util.Objects;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KerberosSecrets;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.RealmException;

/* loaded from: rt.jar:javax/security/auth/kerberos/KeyTab.class */
public final class KeyTab {
    private final File file;
    private final KerberosPrincipal princ;
    private final boolean bound;

    static {
        KerberosSecrets.setJavaxSecurityAuthKerberosAccess(new JavaxSecurityAuthKerberosAccessImpl());
    }

    private KeyTab(KerberosPrincipal kerberosPrincipal, File file, boolean z2) {
        this.princ = kerberosPrincipal;
        this.file = file;
        this.bound = z2;
    }

    public static KeyTab getInstance(File file) {
        if (file == null) {
            throw new NullPointerException("file must be non null");
        }
        return new KeyTab(null, file, true);
    }

    public static KeyTab getUnboundInstance(File file) {
        if (file == null) {
            throw new NullPointerException("file must be non null");
        }
        return new KeyTab(null, file, false);
    }

    public static KeyTab getInstance(KerberosPrincipal kerberosPrincipal, File file) {
        if (kerberosPrincipal == null) {
            throw new NullPointerException("princ must be non null");
        }
        if (file == null) {
            throw new NullPointerException("file must be non null");
        }
        return new KeyTab(kerberosPrincipal, file, true);
    }

    public static KeyTab getInstance() {
        return new KeyTab(null, null, true);
    }

    public static KeyTab getUnboundInstance() {
        return new KeyTab(null, null, false);
    }

    public static KeyTab getInstance(KerberosPrincipal kerberosPrincipal) {
        if (kerberosPrincipal == null) {
            throw new NullPointerException("princ must be non null");
        }
        return new KeyTab(kerberosPrincipal, null, true);
    }

    sun.security.krb5.internal.ktab.KeyTab takeSnapshot() {
        try {
            return sun.security.krb5.internal.ktab.KeyTab.getInstance(this.file);
        } catch (AccessControlException e2) {
            if (this.file != null) {
                throw e2;
            }
            AccessControlException accessControlException = new AccessControlException("Access to default keytab denied (modified exception)");
            accessControlException.setStackTrace(e2.getStackTrace());
            throw accessControlException;
        }
    }

    public KerberosKey[] getKeys(KerberosPrincipal kerberosPrincipal) {
        try {
            if (this.princ != null && !kerberosPrincipal.equals(this.princ)) {
                return new KerberosKey[0];
            }
            EncryptionKey[] serviceKeys = takeSnapshot().readServiceKeys(new PrincipalName(kerberosPrincipal.getName()));
            KerberosKey[] kerberosKeyArr = new KerberosKey[serviceKeys.length];
            for (int i2 = 0; i2 < kerberosKeyArr.length; i2++) {
                Integer keyVersionNumber = serviceKeys[i2].getKeyVersionNumber();
                kerberosKeyArr[i2] = new KerberosKey(kerberosPrincipal, serviceKeys[i2].getBytes(), serviceKeys[i2].getEType(), keyVersionNumber == null ? 0 : keyVersionNumber.intValue());
                serviceKeys[i2].destroy();
            }
            return kerberosKeyArr;
        } catch (RealmException e2) {
            return new KerberosKey[0];
        }
    }

    EncryptionKey[] getEncryptionKeys(PrincipalName principalName) {
        return takeSnapshot().readServiceKeys(principalName);
    }

    public boolean exists() {
        return !takeSnapshot().isMissing();
    }

    public String toString() {
        String string = this.file == null ? "Default keytab" : this.file.toString();
        return !this.bound ? string : this.princ == null ? string + " for someone" : string + " for " + ((Object) this.princ);
    }

    public int hashCode() {
        return Objects.hash(this.file, this.princ, Boolean.valueOf(this.bound));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KeyTab)) {
            return false;
        }
        KeyTab keyTab = (KeyTab) obj;
        return Objects.equals(keyTab.princ, this.princ) && Objects.equals(keyTab.file, this.file) && this.bound == keyTab.bound;
    }

    public KerberosPrincipal getPrincipal() {
        return this.princ;
    }

    public boolean isBound() {
        return this.bound;
    }
}
