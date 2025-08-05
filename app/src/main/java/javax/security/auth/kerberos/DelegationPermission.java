package javax.security.auth.kerberos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.BasicPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.StringTokenizer;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/security/auth/kerberos/DelegationPermission.class */
public final class DelegationPermission extends BasicPermission implements Serializable {
    private static final long serialVersionUID = 883133252142523922L;
    private transient String subordinate;
    private transient String service;

    public DelegationPermission(String str) {
        super(str);
        init(str);
    }

    public DelegationPermission(String str, String str2) {
        super(str, str2);
        init(str);
    }

    private void init(String str) {
        if (!str.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            throw new IllegalArgumentException("service principal [" + str + "] syntax invalid: improperly quoted");
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, PdfOps.DOUBLE_QUOTE__TOKEN, false);
        this.subordinate = stringTokenizer.nextToken();
        if (stringTokenizer.countTokens() == 2) {
            stringTokenizer.nextToken();
            this.service = stringTokenizer.nextToken();
        } else if (stringTokenizer.countTokens() > 0) {
            throw new IllegalArgumentException("service principal [" + stringTokenizer.nextToken() + "] syntax invalid: improperly quoted");
        }
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof DelegationPermission)) {
            return false;
        }
        DelegationPermission delegationPermission = (DelegationPermission) permission;
        if (this.subordinate.equals(delegationPermission.subordinate) && this.service.equals(delegationPermission.service)) {
            return true;
        }
        return false;
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DelegationPermission)) {
            return false;
        }
        return implies((DelegationPermission) obj);
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public int hashCode() {
        return getName().hashCode();
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new KrbDelegationPermissionCollection();
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(getName());
    }
}
