package javax.security.auth.kerberos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Principal;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.util.DerValue;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/security/auth/kerberos/KerberosPrincipal.class */
public final class KerberosPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -7374788026156829911L;
    public static final int KRB_NT_UNKNOWN = 0;
    public static final int KRB_NT_PRINCIPAL = 1;
    public static final int KRB_NT_SRV_INST = 2;
    public static final int KRB_NT_SRV_HST = 3;
    public static final int KRB_NT_SRV_XHST = 4;
    public static final int KRB_NT_UID = 5;
    static final int KRB_NT_ENTERPRISE = 10;
    private transient String fullName;
    private transient String realm;
    private transient int nameType;

    public KerberosPrincipal(String str) {
        this(str, 1);
    }

    public KerberosPrincipal(String str, int i2) {
        SecurityManager securityManager;
        try {
            PrincipalName principalName = new PrincipalName(str, i2);
            if (principalName.isRealmDeduced() && !Realm.AUTODEDUCEREALM && (securityManager = System.getSecurityManager()) != null) {
                try {
                    securityManager.checkPermission(new ServicePermission("@" + principalName.getRealmAsString(), LanguageTag.SEP));
                } catch (SecurityException e2) {
                    throw new SecurityException("Cannot read realm info");
                }
            }
            this.nameType = i2;
            this.fullName = principalName.toString();
            this.realm = principalName.getRealmString();
        } catch (KrbException e3) {
            throw new IllegalArgumentException(e3.getMessage());
        }
    }

    public String getRealm() {
        return this.realm;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return getName().hashCode();
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KerberosPrincipal)) {
            return false;
        }
        return getName().equals(((KerberosPrincipal) obj).getName());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            PrincipalName principalName = new PrincipalName(this.fullName, this.nameType);
            objectOutputStream.writeObject(principalName.asn1Encode());
            objectOutputStream.writeObject(principalName.getRealm().asn1Encode());
        } catch (Exception e2) {
            throw new IOException(e2);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        byte[] bArr = (byte[]) objectInputStream.readObject();
        try {
            Realm realm = new Realm(new DerValue((byte[]) objectInputStream.readObject()));
            PrincipalName principalName = new PrincipalName(new DerValue(bArr), realm);
            this.realm = realm.toString();
            this.fullName = principalName.toString();
            this.nameType = principalName.getNameType();
        } catch (Exception e2) {
            throw new IOException(e2);
        }
    }

    @Override // java.security.Principal
    public String getName() {
        return this.fullName;
    }

    public int getNameType() {
        return this.nameType;
    }

    @Override // java.security.Principal
    public String toString() {
        return getName();
    }
}
